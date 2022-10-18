package mx.com.caminandog;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.GraphRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AmpliarVigenciaRecuperandog extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<Recuperandog, AmpliarVigenciaRecuperandog.PetsViewHolder> mPeopleRVAdapter;
    private Spinner spinner;
    String customer_id;
    ArrayList<String> arraylist1= new ArrayList<String>();
    public int plaquitasGlobal = 0;
    double vigencia;
    double montoApagar;
    TextView total;
    ImageView carga_paseo;
    long fechaActual;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ampliar_vigencia_recuperandog, container, false);

        spinner = vista.findViewById(R.id.spinner_servicesrecupV);
        total = vista.findViewById(R.id.txt_total_recupV);

        mPeopleRV = (RecyclerView) vista.findViewById(R.id.recyclerAmp);

        final DatabaseReference costos = database.getReference(FirebaseReferences.COSTO_REFERERENCE).child("Recuperandog");
        final Query query = costos;

        fill_spinner();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Costos costos = dataSnapshot.getValue(Costos.class);
                vigencia = costos.getVigencia();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.RECUPERANDOG_REFERENCE);

        Query personsQuery = personsRef.orderByChild("uid").equalTo(user.getUid());

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Recuperandog>().setQuery(personsQuery, Recuperandog.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Recuperandog, AmpliarVigenciaRecuperandog.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(AmpliarVigenciaRecuperandog.PetsViewHolder holder, final int position, final Recuperandog model) {
                //System.out.println(model.getIdPerro());

                if (!model.getQR().equals("")) {

                    //System.out.println(model.getIdPerro()+" <<<>>> "+model.getQR());

                    DatabaseReference recuperandog_service_ref = database.getReference(FirebaseReferences.DOGS_REFERERENCE).child(user.getUid()).child(model.getIdPerro());
                    Query query_service = recuperandog_service_ref;
                    query_service.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Perro perro = dataSnapshot.getValue(Perro.class);
                            ImageButton buttonmas = holder.mView.findViewById(R.id.masVig);
                            ImageButton buttonmenos = holder.mView.findViewById(R.id.menosVig);


                            holder.setTitle(perro.getNombre());
                            holder.setImage(getActivity().getBaseContext(), perro.getFoto());
                            holder.setNoPlaca(model.getQR());
                            holder.setVigencia(model.getFechaVencimiento());

                            buttonmas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.setSumPlaq(true,model.getIdPerro());


                                }
                            });

                            buttonmenos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.setSumPlaq(false,model.getIdPerro());


                                }
                            });











                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    holder.itemView.getLayoutParams().height = 0;
                    holder.itemView.requestLayout();
                }
            }

            @Override
            public AmpliarVigenciaRecuperandog.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_perros_recuperandogcompravig, parent, false);
                return new AmpliarVigenciaRecuperandog.PetsViewHolder(view);
            }
        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);

        //fill_spinner();
        getFechaActual();

        Button compraVigencia = vista.findViewById(R.id.btn_pagarRec);
        compraVigencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> set = new HashSet<>();
                for(int i =0; i < arraylist1.size(); i++) {
                    if (set.contains(arraylist1.get(i))) {
                        System.out.println(arraylist1.get(i)+" is duplicated");
                    }else {
                        set.add(arraylist1.get(i));
                    }
                }
                System.out.println(set);
                compraVigencia.setClickable(false);
                TextView txt =(TextView)vista.findViewById(R.id.textView_id_spinner);
                //System.out.println(txt.getText());

                if (txt.getText().toString().equals("")){
                    Toast.makeText(getActivity(),(getResources().getString(R.string.Para_continuar_es_necesario_que_ingreses_una_tarjeta)), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),(getResources().getString(R.string.Da_clic_en_el_boton_de_agregar)), Toast.LENGTH_LONG).show();
                    compraVigencia.setClickable(true);
                }else{
                    //Toast.makeText(Configuracion_Paseo.this, txt.getText(), Toast.LENGTH_LONG).show();



                    androidx.appcompat.app.AlertDialog.Builder mBuilder_x = new androidx.appcompat.app.AlertDialog.Builder(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);//,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                    final View mView_x = getLayoutInflater().inflate(R.layout.dialog_info_agenda, null);
                    mBuilder_x.setView(mView_x);
                    mBuilder_x.setCancelable( false );

                    carga_paseo = (ImageView)mView_x.findViewById(R.id.imageView_walker);

                    TextView txt_info = (TextView)mView_x.findViewById(R.id.txt_text_carga);
                    TextView txt_perros = (TextView)mView_x.findViewById(R.id.txt_perros_carga);

                    final androidx.appcompat.app.AlertDialog dialog_x = mBuilder_x.create();
                    //dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

                    Drawable d = new ColorDrawable(Color.BLACK); d.setAlpha(200);
                    dialog_x.getWindow().setBackgroundDrawable(d);

                    txt_info.setText (getResources().getString(R.string.Estamos_tramitando_la_orden_de_pago_de_vigencia_para_tus_plaquitas));


                    Glide.with(getApplicationContext()).load(R.raw.gif_agenda).apply(RequestOptions.circleCropTransform()).into(carga_paseo);



                    dialog_x.show();

                    //pagar aqui
                    final JSONObject jsonObj2 = new JSONObject();
                    try {


                        jsonObj2.put("uid", user.getUid());
                        jsonObj2.put("id_card", txt.getText());
                        jsonObj2.put("amount", vigencia);
                        jsonObj2.put("customer_id", customer_id);
                        jsonObj2.put("cantidad",plaquitasGlobal);



                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    OkHttpClient httpClient = new OkHttpClient();
                    HttpUrl.Builder httpBuider =
                            HttpUrl.parse(Conecta_Caminandog.ORDER_COMPRA_VIGENCIA_PLAQUITA).newBuilder();
                    httpBuider.addQueryParameter("text", "" + jsonObj2);
                    final Request req = new Request.Builder().
                            url(httpBuider.build()).build();
                    httpClient.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(GraphRequest.TAG, "error in getting response from firebase cloud function");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "Cound’t get response from cloud function",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

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
                                    System.out.println(resp);



                                    if (resp.contains( "error" )){

                                        String[] res = resp.split( "," );
                                        String resx= res[0];
                                        String res_message = res[1];

                                        getActivity().runOnUiThread( new Runnable() {
                                            public void run() {
                                                System.out.println (getResources().getString(R.string.error_funcion_no_se_proceso_el_pago));
                                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder ( getActivity() );
                                                builder.setTitle (getResources().getString(R.string.Pago_no_realizado));
                                                builder.setMessage ( res_message );
                                                builder.setPositiveButton (getResources().getString(R.string.Aceptar) ,new DialogInterface.OnClickListener () {
                                                    @Override
                                                    public void onClick(DialogInterface dialog ,int which) {
                                                        //progressBar.setVisibility( View.INVISIBLE );
                                                        dialog_x.dismiss();
                                                        compraVigencia.setClickable(true);

                                                    }
                                                } );
                                                builder.setNegativeButton( (getResources().getString(R.string.CANCELAR)), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        //startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                                        //finish();
                                                        //fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).addToBackStack(null).commit();
                                                        //getSupportActionBar().setTitle("Mi Cuenta");
                                                        compraVigencia.setClickable(true);


                                                    }
                                                } );
                                                builder.show ();
                                            }
                                        });

                                    }else {

                                        //System.out.println(resp);

                                        String[] respuesta = resp.split( "," );
                                        String mensaje= respuesta[0];
                                        if (mensaje.equals( "correcto" )){


                                            for (int i=0;i<arraylist1.size();i++) {
                                                addVigencia(arraylist1.get(i));
                                                System.out.println("exec "+i+" id "+arraylist1.get(i));
                                            }

                                            getActivity().runOnUiThread( new Runnable() {
                                                public void run() {

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            new AlertDialog.Builder(getActivity())
                                                                    .setTitle(getResources().getString(R.string.Compra_exitosa))
                                                                    .setMessage(getResources().getString(R.string.La_vigencia_ha_sido_agregada_a_tus_plaquitas_GRACIAS_por_tu_compra))
                                                                    .setCancelable(false)
                                                                    .setPositiveButton(getResources().getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.dismiss();
                                                                            dialog_x.dismiss();
                                                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                                            ContenedorRecuperandog_Fragment fragment2 = new ContenedorRecuperandog_Fragment();

                                                                            ft.replace(R.id.contenedor, fragment2);
                                                                            //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                                                            ft.commit();
                                                                        }
                                                                    })

                                                                    .show();
                                                        }
                                                    }, 2500);

                                                }
                                            });



                                        }

                                    }

                                } catch (IOException e) {
                                    resp = "Problem in getting payment info";
                                    Log.e(TAG, "Problem in reading response " + e);
                                }
                            }
                            runOnUiThread(responseRunnable(resp));

                        }

                        private void runOnUiThread(Runnable runnable) {

                            //texttok.setText(""+req);

                        }

                    });
                    //end pagar aqui


                }

            }
        });

        /*Button compraVigencia = vista.findViewById(R.id.btncompraVig);
        compraVigencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject jsonObj2 = new JSONObject();
                try {
                    jsonObj2.put("uid", user.getUid());
                    jsonObj2.put("id_card", "src_2mdUskBPG2i6PmfGU");
                    jsonObj2.put("amount", 200);
                    jsonObj2.put("customer_id", "cus_2mZH6fPBMbKcVhpSf");
                    jsonObj2.put("cantidad",2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient httpClient = new OkHttpClient();
                HttpUrl.Builder httpBuider =
                        HttpUrl.parse(Conecta_Caminandog.ORDER_COMPRA_VIGENCIA_PLAQUITA).newBuilder();
                httpBuider.addQueryParameter("text", "" + jsonObj2);
                final Request req = new Request.Builder().
                        url(httpBuider.build()).build();
                httpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(GraphRequest.TAG, "error in getting response from firebase cloud function");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Cound’t get response from cloud function",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
                                            "No hubo respuesta",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            try {
                                resp = responseBody.string();
                                System.out.println("resp " + resp);
                                if (resp.equals("correcto")) {
                                    this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(), "Eliminado exitosamente!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(), "no es correcto", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                resp = "Problem in getting discount info";
                                Log.e(GraphRequest.TAG, "Problem in reading response " + e);
                            }
                        }
                        runOnUiThread(responseRunnable(resp));
                    }
                    private void runOnUiThread(Runnable runnable) {
                        //texttok.setText(""+req);
                    }
                });
            }
        });*/

        return vista;
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

    public class PetsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PetsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.txt_nombre_rec);
            post_title.setText(title);
        }

        public void setNoPlaca(String desc) {
            TextView post_raza = (TextView) mView.findViewById(R.id.txt_no_placa);
            post_raza.setText(desc);
        }

        public void setVigencia(long vigencia) {
            TextView post_edad = (TextView) mView.findViewById(R.id.txt_vigencia_rec);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTimeInMillis(vigencia);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            post_edad.setText(date);
        }

        public void setSumPlaq(boolean val,String id){
            TextView txt = mView.findViewById(R.id.txtPlacasVig);
            int num = Integer.parseInt(txt.getText().toString());
            if (val){
                if (num==1){

                }else{
                    num++;
                    plaquitasGlobal++;
                    montoApagar = vigencia*plaquitasGlobal;
                    total.setText(montoApagar+"");
                    arraylist1.add(id);
                }



            }else{
                if (num==0){

                }else{
                    num--;
                    plaquitasGlobal--;
                    montoApagar = vigencia*plaquitasGlobal;
                    total.setText(montoApagar+"");
                    arraylist1.remove(id);

                }

            }
            txt.setText(num+"");
        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.avatar_perro_img_rec);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
    }

    public void fill_spinner(){

        //tarjetas spinner

        final DatabaseReference cards_ref = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.CARDS_REFERERENCE).child(user.getUid());

        Query customerquery = cards_ref.child("customer");

        customerquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                final List<Integer> imageArray = new ArrayList<Integer>();
                final List<String> idList = new ArrayList<String>();

                if (dataSnapshot.exists()){

                    //System.out.println("snap exists customer");

                    Customer customer = dataSnapshot.getValue(Customer.class);
                    customer_id = customer.getId();

                    Query cardsQuery = cards_ref.child("tarjetas").orderByValue().limitToFirst(9);

                    cardsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                            if (dataSnapshot.exists()){

                                //System.out.println("snap exists tarjetas");


                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    String titlename = ""+dataSnapshot1.child("card_number").getValue(String.class);
                                    titleList.add(titlename);

                                    String idname = ""+dataSnapshot1.child("id").getValue(String.class);
                                    idList.add(idname);

                                    String brand = dataSnapshot1.child("brand").getValue(String.class);
                                    switch(brand) {
                                        case "VISA":
                                            imageArray.add(R.drawable.img_tarjeta_visaxxxhdpi);
                                            break;
                                        case "visa":
                                            imageArray.add(R.drawable.img_tarjeta_visaxxxhdpi);
                                            break;
                                        case "AMERICAN_EXPRESS":
                                            imageArray.add(R.drawable.img_tarjeta_americanxxxhdpi);
                                            break;
                                        case "american_express":
                                            imageArray.add(R.drawable.img_tarjeta_americanxxxhdpi);
                                            break;
                                        case "MC":
                                            imageArray.add(R.drawable.img_tarjeta_masterxxxhdpi);
                                            break;
                                        case "mastercard":
                                            imageArray.add(R.drawable.img_tarjeta_masterxxxhdpi);
                                            break;
                                        default:
                                            imageArray.add(R.drawable.img_tarjeta_sin_marcaxxxhdpi);
                                            break;
                                    }
                                }

                            }else{

                                //System.out.println("no existe tarjeta");

                                String titlename = "****";
                                titleList.add(titlename);

                                String idname = "";
                                idList.add(idname);

                                imageArray.add(R.drawable.img_tarjeta_sin_marcaxxxhdpi);

                            }


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {

                                    AmpliarVigenciaRecuperandog.SpinnerAdapter adapter = new AmpliarVigenciaRecuperandog.SpinnerAdapter(getContext(), R.layout.spinner_custom_cards, titleList, idList, imageArray);
                                    spinner.setAdapter(adapter);

                                    //Collections.sort(titleList);
                                    adapter.notifyDataSetChanged();


                                }
                            }, 1500);




                            //() = { R.drawable.amex, R.drawable.mastercard, R.drawable.visa};




                /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Configuracion_Paseo.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);*/
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    //System.out.println("no existe customer");
                    String titlename = "****";
                    titleList.add(titlename);

                    String idname = "";
                    idList.add(idname);

                    imageArray.add(R.drawable.img_tarjeta_sin_marcaxxxhdpi);


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            AmpliarVigenciaRecuperandog.SpinnerAdapter adapter = new AmpliarVigenciaRecuperandog.SpinnerAdapter(getContext(), R.layout.spinner_custom_cards, titleList, idList, imageArray);
                            spinner.setAdapter(adapter);

                            //Collections.sort(titleList);
                            adapter.notifyDataSetChanged();


                        }
                    }, 1500);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        private Context ctx;
        private List<String> contentArray;
        private List<String> idcontentArray;
        private List<Integer>  imageArray;

        public SpinnerAdapter(Context context, int resource, List<String> objects, List<String> idobjects , List<Integer> imageArray) {
            super(context,  R.layout.spinner_custom_cards, R.id.textView_spinner, objects);
            this.ctx = context;
            this.contentArray = objects;
            this.idcontentArray = idobjects;
            this.imageArray = imageArray;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.spinner_custom_cards, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.textView_spinner);
            textView.setText(contentArray.get(position));

            TextView textViewid = (TextView) row.findViewById(R.id.textView_id_spinner);
            textViewid.setText(idcontentArray.get(position));

            ImageView imageView = (ImageView)row.findViewById(R.id.imageView_spinner);
            imageView.setImageResource(imageArray.get(position));

            return row;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();

    }

    public void getFechaActual() {
        DatabaseReference timestamp_Ref = FirebaseDatabase.getInstance().getReference().child("sessions");
        timestamp_Ref.child( "actual" ).setValue( ServerValue.TIMESTAMP ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Query timestamp_Query = timestamp_Ref;

                timestamp_Query.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Sessions sessions = dataSnapshot.getValue(Sessions.class);
                        long timestamp = sessions.getActual();
                        fechaActual =timestamp;


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        });

    }



    public void addVigencia(String id){
        System.out.println("method exec "+id);
        final DatabaseReference recup = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child(id);
        final Query query = recup;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recuperandog recuperandog = dataSnapshot.getValue(Recuperandog.class);
                long vencimiento = recuperandog.getFechaVencimiento();

                if (fechaActual>vencimiento){
                    Date date = new Date(fechaActual); // *1000 is to convert seconds to milliseconds
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.YEAR, 1);
                    System.out.println("query "+id+" old val "+vencimiento+" new val "+cal.getTimeInMillis());
                    DatabaseReference adVig = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.RECUPERANDOG_REFERENCE);
                    adVig.child( id ).child("fechaVencimiento").setValue( cal.getTimeInMillis() ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }else {
                    Date date = new Date(vencimiento); // *1000 is to convert seconds to milliseconds
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.YEAR, 1);
                    System.out.println("query "+id+" old val "+vencimiento+" new val "+cal.getTimeInMillis());
                    DatabaseReference adVig = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.RECUPERANDOG_REFERENCE);
                    adVig.child( id ).child("fechaVencimiento").setValue( cal.getTimeInMillis() ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    class Id{

        String id;
        int años;
        Id(String id,int años){
            this.id=id;
            this.años=años;
        }
    }
}