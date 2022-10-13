package mx.com.caminandog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Perrhijos_Recuperandog_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Perrhijos_Recuperandog_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perrhijos_Recuperandog_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Perro, Perrhijos_Recuperandog_Fragment.PetsViewHolder> mPeopleRVAdapter;

    ProgressBar prog;
    Bundle bundle = new Bundle(  );


    public Perrhijos_Recuperandog_Fragment() {
        // Required empty public constructor
    }


    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference paseoRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid());
    final DatabaseReference paseoRef2 = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios");
    Query query = paseoRef.orderByKey();

    Calendar cal_start = Calendar.getInstance();
    Calendar cal_end = Calendar.getInstance();
    int vig;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perrhijos_Recuperandog_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Perrhijos_Recuperandog_Fragment newInstance(String param1, String param2) {
        Perrhijos_Recuperandog_Fragment fragment = new Perrhijos_Recuperandog_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =  inflater.inflate(R.layout.fragment_perrhijos__recuperandog_, container, false);

        final DatabaseReference timestamp_Ref = FirebaseDatabase.getInstance().getReference().child("sessions");
        timestamp_Ref.child( "actual" ).setValue( ServerValue.TIMESTAMP );

        Query timestamp_Query = timestamp_Ref;

        timestamp_Query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sessions sessions = dataSnapshot.getValue(Sessions.class);
                long timestamp = sessions.getActual();



                Date date = new Date(timestamp); // *1000 is to convert seconds to milliseconds

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);


                cal_start.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)-1,1,0,0,0);
                cal_end.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),(cal.get(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH)),23,59,58);

                System.out.println("<<<< Fecha Actual: "+cal.get(Calendar.DAY_OF_MONTH)+" / "+cal.get(Calendar.MONTH)+" / "+cal.get(Calendar.YEAR)+" >>>> \n"+
                        "Fecha de inicio "+cal_start.get(Calendar.YEAR)+"/"+cal_start.get(Calendar.MONTH)+"/"+cal_start.get(Calendar.DAY_OF_MONTH)+" <<>> "+cal_start.getTimeInMillis()+"\n " +
                        "Fecha de termino "+cal_end.get(Calendar.YEAR)+"/"+cal_end.get(Calendar.MONTH)+"/"+cal_end.get(Calendar.DAY_OF_MONTH)+" <<>> "+cal_end.getTimeInMillis());

                //

                DatabaseReference vig_ref = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.CONFIGURACION_REFERENCE);
                Query query_vig = vig_ref;
                query_vig.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                        Configuracion config = dataSnapshot3.getValue(Configuracion.class);
                        vig=config.getDiasRecuperandog();
                        System.out.println("vige "+vig);
                        mPeopleRV.setAdapter(mPeopleRVAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





                //





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        prog = (ProgressBar) vista.findViewById(R.id.progress_recup);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Perros");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) vista.findViewById(R.id.recper_recup);



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Perros").child(user.getUid());
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Perros").child(user.getUid());

        Query personsQuery = personsRef.orderByKey();




        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));



        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Perro>().setQuery(personsQuery, Perro.class).build();

        //System.out.println(" ffvfvf "+personsOptions.getSnapshots());
        if(personsOptions.getSnapshots().isEmpty()){
            prog.setVisibility(View.GONE);
        }else {
            prog.setVisibility(View.VISIBLE);
        }

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Perro, Perrhijos_Recuperandog_Fragment.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(Perrhijos_Recuperandog_Fragment.PetsViewHolder holder, final int position, final Perro model) {
                //DatabaseReference recuperandog_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child( model.getIdPerro() ).child("paseos");


                /*DatabaseReference recuperandog_service_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child( model.getIdPerro() );
                Query query_service = recuperandog_service_ref.orderByChild("servicio");
                query_service.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Servicio servicio = dataSnapshot.getValue(Servicio.class);
                            if (servicio.getServicio().equals("true")){

                                Paseo_recuperandog paseo_recuperandog = dataSnapshot.getValue(Paseo_recuperandog.class);

                                System.out.println("servicio "+servicio.getServicio());

                                prog.setVisibility(View.VISIBLE);
                                holder.setTitle(model.getNombre());
                                holder.setImage(getActivity().getBaseContext(), model.getFoto());
                                holder.setRaza(model.getRaza());
                                holder.setEdad(model.getEdad());
                                prog.setVisibility(View.GONE);

                                try {

                                    if (paseo_recuperandog.getLectura().equals("true")){
                                        holder.setBusqueda();
                                        holder.mView.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                recuperandog_service_ref.child("lectura").setValue("false");




                                                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                Recuperandog_Informacion_Fragment fragment2 = new Recuperandog_Informacion_Fragment();

                                                bundle.putString( "foto",model.getFoto());
                                                bundle.putString( "nombre",model.getNombre());
                                                bundle.putString( "idPerro",model.getIdPerro());
                                                fragment2.setArguments(bundle);

                                                ft.replace(R.id.contenedor, fragment2);
                                                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                                ft.commit();


                                            }
                                        });

                                    }else if (paseo_recuperandog.getPrimeraLectura().equals("true")){
                                        holder.setCorazon();

                                        holder.mView.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {




                                                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                Recuperandog_Informacion_Fragment fragment2 = new Recuperandog_Informacion_Fragment();

                                                bundle.putString( "foto",model.getFoto());
                                                bundle.putString( "nombre",model.getNombre());
                                                bundle.putString( "idPerro",model.getIdPerro());
                                                fragment2.setArguments(bundle);

                                                ft.replace(R.id.contenedor, fragment2);
                                                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                                ft.commit();


                                            }
                                        });

                                    }

                                }catch (Exception e ){}










                            }else {
                                //System.out.println("servicio "+servicio.getServicio());

                                DatabaseReference recuperandog_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child( model.getIdPerro() );
                                Query query = recuperandog_ref.child("paseos").orderByChild("fecha").startAt(cal_start.getTimeInMillis()).endAt(cal_end.getTimeInMillis());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()){
                                            System.out.println(">>>> "+dataSnapshot.getChildrenCount()+" <<<< "+" >>>> "+model.getIdPerro()+" <<<< "+" >>>> "+dataSnapshot.getKey()+" <<<<");
                                            Query queryrecup = recuperandog_ref.orderByKey();
                                            queryrecup.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapsho) {
                                                    Paseo_recuperandog paseo_recuperandog = dataSnapsho.getValue(Paseo_recuperandog.class);
                                                    System.out.println("lectura "+paseo_recuperandog.getLectura()+" primera lectura "+paseo_recuperandog.getPrimeraLectura());

                                                    //

                                                    if (dataSnapshot.getChildrenCount()>=vig) {

                                                        prog.setVisibility(View.VISIBLE);
                                                        holder.setTitle(model.getNombre());
                                                        holder.setImage(getActivity().getBaseContext(), model.getFoto());
                                                        holder.setRaza(model.getRaza());
                                                        holder.setEdad(model.getEdad());
                                                        prog.setVisibility(View.GONE);

                                                        System.out.println("lectura "+paseo_recuperandog.getLectura()+" primera lectura "+paseo_recuperandog.getPrimeraLectura());


                                                        try{

                                                            if (paseo_recuperandog.getLectura().equals("true")){
                                                                holder.setBusqueda();


                                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        recuperandog_service_ref.child("lectura").setValue("false");
                                                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                                        Recuperandog_Informacion_Fragment fragment2 = new Recuperandog_Informacion_Fragment();

                                                                        bundle.putString( "foto",model.getFoto());
                                                                        bundle.putString( "nombre",model.getNombre());
                                                                        bundle.putString( "idPerro",model.getIdPerro());
                                                                        fragment2.setArguments(bundle);

                                                                        ft.replace(R.id.contenedor, fragment2);
                                                                        //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                                                        ft.commit();

                                                                    }
                                                                });

                                                            }else if (paseo_recuperandog.getPrimeraLectura().equals("true")){

                                                                holder.setCorazon();

                                                                holder.mView.setOnClickListener(new View.OnClickListener() {

                                                                    @Override
                                                                    public void onClick(View v) {




                                                                        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                                                                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                                        Recuperandog_Informacion_Fragment fragment2 = new Recuperandog_Informacion_Fragment();

                                                                        bundle.putString( "foto",model.getFoto());
                                                                        bundle.putString( "nombre",model.getNombre());
                                                                        bundle.putString( "idPerro",model.getIdPerro());
                                                                        fragment2.setArguments(bundle);

                                                                        ft.replace(R.id.contenedor, fragment2);
                                                                        //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                                                        ft.commit();


                                                                    }
                                                                });

                                                            }

                                                        }catch (Exception e){
                                                            System.out.println("catch "+e);

                                                        }





                                                    }else {
                                                        System.out.println("el perro no alcanzo los minimos");
                                                        holder.itemView.getLayoutParams().height=0;
                                                        holder.itemView.requestLayout();

                                                    }

                                                    //

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });




                                        }else{

                                            System.out.println("no hay paseos en esta fecha para el perro");
                                            holder.itemView.getLayoutParams().height=0;
                                            holder.itemView.requestLayout();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        }else{
                            System.out.println("no hay servicio gratis para el perro");
                            holder.itemView.getLayoutParams().height=0;
                            holder.itemView.requestLayout();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/






            }
            @Override
            public Perrhijos_Recuperandog_Fragment.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_perros_recuperandog, parent, false);



                //return new PetsViewHolder(view);
                return  new Perrhijos_Recuperandog_Fragment.PetsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }
        };











        return vista;
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

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //mPeopleRVAdapter.stopListening();
    }



    public static class PetsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PetsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }






        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.txt_nombre_perro);
            post_title.setText(title);
        }

        public void setRaza(String desc){
            TextView post_raza = (TextView)mView.findViewById(R.id.txt_raza_perro);
            post_raza.setText(desc);
        }
        public void setEdad(String desc){
            TextView post_edad = (TextView)mView.findViewById(R.id.txt_edad_perro);
            post_edad.setText(desc);
        }





        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.avatar_perro_img);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
    }
}
