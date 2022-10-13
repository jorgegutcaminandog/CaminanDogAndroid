package mx.com.caminandog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Locale;


public class Historic_Paseo_Fragmnt extends Fragment {

    private RecyclerView mPeopleRV;
    //private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Paseo, Historic_Paseo_Fragmnt.CardsViewHolder> mPeopleRVAdapter;





    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;

    public Historic_Paseo_Fragmnt() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View historicview = inflater.inflate(R.layout.fragment_historic__paseo__fragmnt, container, false);

        mPeopleRV = (RecyclerView) historicview.findViewById(R.id.rec_historic);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid());

        Query personsQuery = personsRef.orderByChild("timestamp").startAt(1519970399000L);

        //"size").startAt("9").endAt("10.5")




        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mPeopleRV.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Paseo>().setQuery(personsQuery, Paseo.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Paseo, Historic_Paseo_Fragmnt.CardsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(Historic_Paseo_Fragmnt.CardsViewHolder holder, final int position, final Paseo model) {
                //progressBar.setVisibility(View.GONE);
                holder.setTitle(model.getOrder_id());



                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.setTimeInMillis(model.getTimestamp());
                String date = DateFormat.format("EEEE dd-MM-yyyy HH:mm:ss", cal).toString();
                holder.setHora(date);




                //holder.setHora(model.getCreation_date());
                holder.setnum_perr(model.getNum_perros());
                holder.setCategoria(model.getCategoria());
                //holder.setHora(model.getHora());
                holder.setCalif(model.getCalificacion());
                holder.setCosto(new BigDecimal(model.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                //holder.setCosto(model.getAmount());
                double tiempo_pas = model.getTiempo_paseo();
                System.out.println((getResources().getString(R.string.Tiempo_pass))+tiempo_pas);
                if (tiempo_pas==0.0){
                    holder.setTiempomin("30 min");
                }else if(tiempo_pas==1.0||tiempo_pas==2.0){
                    holder.setTiempo(model.getTiempo_paseo());
                }



                final DatabaseReference estatus_paseo =  FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(model.getOrder_id()).child("estatusPaseo");
                Query oo = estatus_paseo;
                oo.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot issue4 : dataSnapshot.getChildren()) {
                            Estatus estatus = dataSnapshot.getValue( Estatus.class );
                            System.out.println( estatus.getEstatus());
                            String estatusString = estatus.getEstatus();
                            holder.setStatus("Status: "+estatusString);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );




                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Historic_Map fragment2 = new Historic_Map();

                        Bundle bundle = new Bundle();
                        bundle.putString( "order_id",model.getOrder_id() );
                        bundle.putString( "pas_id",model.getId_paseador() );
                        fragment2.setArguments(bundle);

                        ft.replace(R.id.contenedor, fragment2);
                        ft.addToBackStack(null);
                        ft.commit();

                    }
                });


            }

            @Override
            public Historic_Paseo_Fragmnt.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_historic_paseo, parent, false);

                //return new CardsViewHolder(view);
                return  new Historic_Paseo_Fragmnt.CardsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);

        return historicview;
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
        public void setTitle(String title2){
            TextView post_title2 = (TextView)mView.findViewById(R.id.txt_hist_id);
            post_title2.setText((mView.getResources().getString(R.string.ID_paseo)+" "+title2));
            //getResources().getString(R.string.Paseadorr)
            //getActivity.getResourses...
        }

        public void setCategoria(String title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_cat);
            post_title3.setText("Categoria: "+title3);
        }

        public void setnum_perr(Long title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_numper);
            post_title3.setText("Numero de Perrhijos: "+title3);
        }

        public void setDate(String title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_fecha);
            post_title3.setText(title3);
        }

        public void setHora(String title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_hora);
            post_title3.setText(title3);
        }
        public void setCosto(Double title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_costo);
            post_title3.setText("$ "+title3);
        }
        public void setCalif(Double title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_calif);
            post_title3.setText("Calificacion "+title3);
        }
        public void setStatus(String title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_status);
            post_title3.setText(title3);
        }
        public void setTiempo(long title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_tiempo);
            post_title3.setText("Tiempo: "+title3+"h");
        }
        public void setTiempomin(String title3){
            TextView post_title3 = (TextView)mView.findViewById(R.id.txt_hist_tiempo);
            post_title3.setText("Tiempo: "+title3);
        }



        /*public void setImage(Context ct, String image3){
            ImageView post_image3 = (ImageView) mView.findViewById(R.id.img_card_type);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ct).load(image3).into(post_image3);
        }*/


    }
}
