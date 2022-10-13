package mx.com.caminandog;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Paseos_Activos_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Paseos_Activos_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Paseos_Activos_Fragment extends Fragment {



    private RecyclerView mPeopleRV;
    //private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<PaseosActivos, CardsViewHolder> mPeopleRVAdapter;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Paseos_Activos_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Paseos_Activos_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Paseos_Activos_Fragment newInstance(String param1, String param2) {
        Paseos_Activos_Fragment fragment = new Paseos_Activos_Fragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View pasact = inflater.inflate( R.layout.fragment_paseos__activos_, container, false );

        mPeopleRV = (RecyclerView) pasact.findViewById(R.id.rec_paseosrec);



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child( "paseosActivos" );

        Query personsQuery = personsRef.orderByChild("timestamp");

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        mPeopleRV.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<PaseosActivos>().setQuery(personsQuery, PaseosActivos.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<PaseosActivos, CardsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Paseos_Activos_Fragment.CardsViewHolder holder, final int position, final PaseosActivos model) {
                //progressBar.setVisibility(View.GONE);

                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.setTimeInMillis(model.getTimestamp());
                String date = DateFormat.format("EEEE dd-MM-yyyy HH:mm:ss", cal).toString();



                if (model.getEstatus().equals("no_agendado")){
                    holder.setPaseo(model.getOrder_id(),model.getPerrosNombre(),date,model.getNum_perros(),model.getTipo(),model.getNombre_paseador());
                }else {
                    holder.setAgenda(model.getOrder_id(),model.getPerrosNombre(),date,model.getNum_perros(),model.getTipo(),model.getNombre_paseador());
                }

                if (model.isMensaje()){
                    holder.setMessage();
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //if (!model.getEstatus().equals( "agendado" )){
                            FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                            ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            Tacking_map fragment2 = new Tacking_map();

                            Bundle bundle = new Bundle();
                            //bundle.putString( "uid",model.getUid() );
                            //bundle.putString( "nombre",model.getNombre() );
                            bundle.putString( "order_id",model.getOrder_id() );
                            bundle.putString( "fotopaseador",model.getFoto_paseador() );
                            bundle.putString( "nombrepaseador",model.getNombre_paseador() );
                            bundle.putString( "categoria",model.getTipo() );
                            //bundle.putString( "order_id",model.g );
                            fragment2.setArguments(bundle);

                            ft.replace(R.id.contenedor, fragment2);
                            ft.addToBackStack(null);
                            ft.commit();

                        //}


                    }
                });



            }

            @Override
            public Paseos_Activos_Fragment.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_paseos_activos, parent, false);

                //return new CardsViewHolder(view);
                return  new Paseos_Activos_Fragment.CardsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);



        return pasact ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
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

        public void setPaseo (String id,String name,String date,int no_per,String cate,String neme_pas){
            LinearLayout cardView = mView.findViewById(R.id.card);
            cardView.setBackground(ContextCompat.getDrawable(mView.getContext(), R.drawable.dialog_fichas));
            TextView id_ = (TextView)mView.findViewById(R.id.txt_hist_hora);
            id_.setText(id);
            TextView name_ = (TextView)mView.findViewById(R.id.nomtext);
            name_.setText(name);
            TextView date_ = (TextView)mView.findViewById(R.id.opdate);
            date_.setText(date);
            TextView noper_ = (TextView)mView.findViewById(R.id.numperr);
            noper_.setText(""+no_per);
            TextView cate_ = (TextView)mView.findViewById(R.id.cate);
            cate_.setText(cate);
            TextView nem = (TextView)mView.findViewById(R.id.namepas);
            nem.setText(neme_pas);
        }

        public void setAgenda (String id,String name,String date,int no_per,String cate,String neme_pas){
            LinearLayout cardView = mView.findViewById(R.id.card);
            cardView.setBackground(ContextCompat.getDrawable(mView.getContext(), R.drawable.dialog_fichas_agendados));

            TextView id_ = (TextView)mView.findViewById(R.id.txt_hist_hora);
            id_.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            id_.setText(id);
            TextView name_ = (TextView)mView.findViewById(R.id.nomtext);
            name_.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            name_.setText(name);
            TextView date_ = (TextView)mView.findViewById(R.id.opdate);
            date_.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            date_.setText(date);
            TextView noper_ = (TextView)mView.findViewById(R.id.numperr);
            noper_.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            noper_.setText(""+no_per);
            TextView cate_ = (TextView)mView.findViewById(R.id.cate);
            cate_.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            cate_.setText(cate);
            TextView nem = (TextView)mView.findViewById(R.id.namepas);
            nem.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            nem.setText(neme_pas);

            TextView id_l = (TextView)mView.findViewById(R.id.lbl_txt_hist_hora);
            id_l.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));
            TextView name_l = (TextView)mView.findViewById(R.id.lbl_nomtext);
            name_l.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));
            TextView date_l = (TextView)mView.findViewById(R.id.lbl_opdate);
            date_l.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));
            TextView noper_l = (TextView)mView.findViewById(R.id.lbl_numperr);
            noper_l.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));
            TextView cate_l = (TextView)mView.findViewById(R.id.lbl_cate);
            cate_l.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));
            TextView neml = (TextView)mView.findViewById(R.id.lbl_namepas);
            neml.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));

            TextView l1 = (TextView)mView.findViewById(R.id.lin1);
            l1.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            TextView l2 = (TextView)mView.findViewById(R.id.lin2);
            l2.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            TextView l3 = (TextView)mView.findViewById(R.id.lin3);
            l3.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            TextView l4 = (TextView)mView.findViewById(R.id.lin4);
            l4.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            TextView l5 = (TextView)mView.findViewById(R.id.lin5);
            l5.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));
            TextView l6 = (TextView)mView.findViewById(R.id.lin6);
            l6.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.black));

        }

        public void setMessage(){
            TextView post_title2 = (TextView)mView.findViewById(R.id.new_message);
            post_title2.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mView.getContext(), R.drawable.ic_mensaje_nuevo),null);
        }


    }
}
