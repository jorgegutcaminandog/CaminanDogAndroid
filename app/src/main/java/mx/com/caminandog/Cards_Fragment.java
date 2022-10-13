package mx.com.caminandog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;
import io.conekta.conektasdk.Card;


import static com.facebook.GraphRequest.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cards_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cards_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cards_Fragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String customer_id;

    int MY_SCAN_REQUEST_CODE = 0;

    private RecyclerView mPeopleRV;

    private FirebaseRecyclerAdapter<Card_caminandog, Cards_Fragment.CardsViewHolder> mPeopleRVAdapter;








    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Cards_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cards_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Cards_Fragment newInstance(String param1, String param2) {
        Cards_Fragment fragment = new Cards_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vistacards = inflater.inflate(R.layout.fragment_cards_, container, false);




        mPeopleRV = (RecyclerView) vistacards.findViewById(R.id.rec_cards_rv);



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final DatabaseReference customerid_ref= FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.CARDS_REFERERENCE).child(user.getUid()).child( "customer" );
        Query querycutom = customerid_ref;

        querycutom.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issue: dataSnapshot.getChildren()){
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    customer_id = customer.getId();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.CARDS_REFERERENCE).child(user.getUid()).child( "tarjetas" );

       Query personsQuery = personsRef.orderByKey().limitToFirst(9);






        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Card_caminandog>().setQuery(personsQuery, Card_caminandog.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Card_caminandog, Cards_Fragment.CardsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Cards_Fragment.CardsViewHolder holder, final int position, final Card_caminandog model) {



                try {
                    //progressBar.setVisibility(View.GONE);
                    holder.setTitle(model.getCard_number());
                    //holder.setDate(model.getExpiration_month()+"/"+model.expiration_year);
                    if (model.getBrand().contains("VISA")||model.getBrand().contains("visa")){
                        holder.setImage(getContext(),"android.resource://" + getActivity().getPackageName() +"/"+R.drawable.img_tarjeta_visaxxxhdpi);
                    }else if (model.getBrand().contains("MC")||model.getBrand().contains("mastercard")){
                        holder.setImage(getContext(),"android.resource://" + getActivity().getPackageName() +"/"+R.drawable.img_tarjeta_masterxxxhdpi);
                    }else if (model.getBrand().contains("AMERICAN_EXPRESS")||model.getBrand().contains("american_express")){
                        holder.setImage(getContext(),"android.resource://" + getActivity().getPackageName() +"/"+R.drawable.img_tarjeta_americanxxxhdpi);

                    }

                }catch (Exception e){System.out.println((getResources().getString(R.string.Problema)));}



                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_elim_card, null);
                        mBuilder.setView(mView);
                        final TextView resultt = ((TextView) mView.findViewById(R.id.res_txt_elim));
                        final TextView title = ((TextView) mView.findViewById(R.id.title_Txt));
                        final Button btn_res = ((Button) mView.findViewById(R.id.acep_btn_elim));
                        final Button btn_can = ((Button) mView.findViewById(R.id.btn_elim_cancel));
                        final ProgressBar prog = ((ProgressBar) mView.findViewById(R.id.progres_elim));
                        prog.setVisibility( View.INVISIBLE );
                        title.setVisibility( View.INVISIBLE );
                        resultt.setText( (getResources().getString(R.string.Estas_seguro_de_eliminar_la_tarjeta)) );
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
                        dialog.show();

                        btn_can.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        } );

                        btn_res.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{

                                    prog.setVisibility( View.VISIBLE );
                                    title.setVisibility( View.VISIBLE );
                                    resultt.setVisibility( View.INVISIBLE );
                                    btn_can.setVisibility( View.INVISIBLE );
                                    btn_res.setVisibility( View.INVISIBLE );


                                    final JSONObject jsonObj2 = new JSONObject();
                                    try {

                                        jsonObj2.put("customer",customer_id );
                                        jsonObj2.put("card", model.getId());
                                        jsonObj2.put("uid",user.getUid());


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                    }

                                    OkHttpClient httpClient = new OkHttpClient();
                                    HttpUrl.Builder httpBuider =
                                            HttpUrl.parse(Conecta_Caminandog.DELETE_FUNC).newBuilder();
                                    httpBuider.addQueryParameter("text",""+jsonObj2);
                                    final Request req = new Request.Builder().
                                            url(httpBuider.build()).build();
                                    httpClient.newCall(req).enqueue(new Callback() {
                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            ResponseBody responseBody = response.body();
                                            String resp = "";
                                            if (!response.isSuccessful()) {
                                                //dialog.dismiss();

                                                this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(),
                                                                (getResources().getString(R.string.No_hubo_respuesta)),
                                                                Toast.LENGTH_SHORT).show();

                                                    }
                                                });


                                            } else {
                                                try {
                                                    resp = responseBody.string();
                                                    dialog.dismiss();

                                                    System.out.println("resp "+resp);


                                                    if (resp.equals( (getResources().getString(R.string.Correcto)))){

                                                        this.runOnUiThread( new Runnable() {
                                                            public void run() {
                                                                Toast.makeText(getContext(), (getResources().getString(R.string.Eliminado_exitosamente)), Toast.LENGTH_LONG).show();


                                                            }
                                                        });


                                                    }else {

                                                        this.runOnUiThread( new Runnable() {
                                                            public void run() {
                                                                Toast.makeText(getContext(), (getResources().getString(R.string.Ocurrio_un_error_al_elminar_la_tarjeta)), Toast.LENGTH_LONG).show();

                                                            }
                                                        });

                                                    }


                                                } catch (IOException e) {
                                                    resp = "Problem in getting discount info";
                                                    Log.e(TAG, "Problem in reading response " + e);
                                                }
                                            }
                                            runOnUiThread(responseRunnable(resp));

                                        }

                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            Log.e(TAG, "error in getting response from firebase cloud function");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(),
                                                            "Cound’t get response from cloud function",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }



                                        private void runOnUiThread(Runnable runnable) {

                                            //texttok.setText(""+req);

                                        }

                                    });
                                } catch (Exception e){Toast.makeText(getContext(), (getResources().getString(R.string.Ocurrio_un_error)), Toast.LENGTH_LONG).show();}

                            }
                        } );




                    }
                });


            }

            @Override
            public Cards_Fragment.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_card_pagos, parent, false);

                //return new CardsViewHolder(view);
                return  new Cards_Fragment.CardsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);





        final Button scancard = (Button) vistacards.findViewById(R.id.scan_btn);

        scancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(vistacards);
            }
        });








        return vistacards;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



















    private Integer getInteger(final String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    private Runnable responseRunnable(final String responseStr){
        Runnable resRunnable = new Runnable(){
            public void run(){
                Toast.makeText(getContext()
                        ,responseStr,
                        Toast.LENGTH_SHORT).show();
            }
        };
        return resRunnable;
    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(getContext(), CardIOActivity.class);



        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO,true);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON,false);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);


        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);




                if (scanResult.isExpiryValid()) {
                    if (scanResult.cvv != null) {
                        //
                        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_procesando, null);
                        mBuilder.setView(mView);
                        final TextView resultt = ((TextView) mView.findViewById(R.id.res_txt));
                        final Button btn_res = ((Button) mView.findViewById(R.id.acep_btn));
                        final ProgressBar prog = ((ProgressBar) mView.findViewById(R.id.progres_res));
                        btn_res.setVisibility( View.INVISIBLE );
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
                        dialog.show();

                        Conekta.setPublicKey( Conecta_Caminandog.PUB_KEY );
                        //Conekta.setApiVersion("1.0.0");                       //optional
                        Conekta.collectDevice(getActivity());

                        Card card = new Card(scanResult.cardholderName, scanResult.cardNumber, scanResult.cvv, String.valueOf(scanResult.expiryMonth), String.valueOf(scanResult.expiryYear));
                        Token token = new Token(getActivity());

                        token.onCreateTokenListener( new Token.CreateToken(){
                            @Override
                            public void onCreateTokenReady(JSONObject data) {
                                try {

                                    String resultado = (String) data.get( "object" );

                                    if (resultado.equals("token")){
                                        System.out.println(data.get( "id" ));
                                        //enviar funcion



                                        JSONObject jsonObj = new JSONObject();
                                        try {
                                            jsonObj.put("token_id", data.get( "id" ));
                                            jsonObj.put("uid",user.getUid());

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }






                                        OkHttpClient httpClient = new OkHttpClient();
                                        HttpUrl.Builder httpBuider =
                                                HttpUrl.parse(Conecta_Caminandog.CREATE_FUNC).newBuilder();
                                        httpBuider.addQueryParameter("text",""+jsonObj);
                                        final Request req = new Request.Builder().
                                                url(httpBuider.build()).build();
                                        httpClient.newCall(req).enqueue(new Callback() {
                                            @Override
                                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                ResponseBody responseBody = response.body();
                                                String resp = "";
                                                if (!response.isSuccessful()) {

                                                    Log.e(TAG, "fail response from firebase cloud function");
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(),
                                                                    "Cound’t get response from cloud function",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {

                                                    resp = responseBody.string();
                                                    if (resp.equals( "correcto" )){
                                                        dialog.dismiss();

                                                    }

                                                }
                                                runOnUiThread(responseRunnable(resp));

                                            }

                                            @Override
                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                Log.e(TAG, "error in getting response from firebase cloud function");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(),
                                                                "Cound’t get response from cloud function",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }



                                            private void runOnUiThread(Runnable runnable) {

                                                //texttok.setText(""+req);

                                            }});








                                    }else if (resultado.equals("error")){
                                        prog.setVisibility( View.INVISIBLE );
                                        resultt.setText( data.get("message_to_purchaser").toString() );
                                        btn_res.setVisibility( View.VISIBLE );
                                        btn_res.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        } );
                                    }

                                    //Send the id to the webservice.
                                } catch (Exception err) {
                                    //Do something on error
                                }
                            }
                        });

                        token.create(card);
                    }

                }


            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            //cardnoTextView.setText(resultDisplayStr);

        }
        // else handle other activity results
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();

    }
    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }


    public static class CardsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public CardsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title2){
            TextView post_title2 = (TextView)mView.findViewById(R.id.card_num_pagos);
            post_title2.setText("************"+title2);
        }






        public void setImage(Context ct, String image3){
            ImageView post_image3 = (ImageView) mView.findViewById(R.id.img_card_type_pagos);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ct).load(image3).into(post_image3);
        }


    }




}
