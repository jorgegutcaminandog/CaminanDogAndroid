package mx.com.caminandog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Codigos_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Codigos_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Codigos_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<CuponUser, Codigos_Fragment.PetsViewHolder> mPeopleRVAdapter;
    private LinearLayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Codigos_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Codigos_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Codigos_Fragment newInstance(String param1, String param2) {
        Codigos_Fragment fragment = new Codigos_Fragment();
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
        View vista = inflater.inflate(R.layout.fragment_codigos_, container, false);
        //TextView txt_compartir = vista.findViewById(R.id.compartir_txt);
        TextView txt_añadir = vista.findViewById(R.id.añadir_txt);
        Button btn_añadir = vista.findViewById(R.id.añadir_btn);
        DatabaseReference cupons = database.getReference(FirebaseReferences.CUPONS_REFERENCE);
        DatabaseReference paseos = database.getReference(FirebaseReferences.PASEO_USR_REFERERENCE);
        DatabaseReference usuario = database.getReference(FirebaseReferences.USER_REFERERENCE).child(user.getUid());
        Query exist_paseo = paseos.child(user.getUid());





        btn_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txt_añadir.getText().toString().equals("")){
                    String cup_ingresado = txt_añadir.getText().toString();
                    Query exist_cupons = cupons.child(cup_ingresado);
                    Query usr_cupon_query = usuario.child("cupones").orderByChild("activo").equalTo(true);
                    Query usr_cupon_query2 = usuario.child("cupones").orderByChild("id").equalTo(cup_ingresado);
                    exist_cupons.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            txt_añadir.setText("");
                            if (dataSnapshot.exists()){
                                Cupon cupon = dataSnapshot.getValue(Cupon.class);
                                if (cupon.getEstatus().equals("activo")){
                                    exist_paseo.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()){
                                                usr_cupon_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            S_dialog (getResources().getString(R.string.Codigo_no_ingresado), getResources().getString(R.string.Solo_puedes_agregar_un_codigo_hasta_que_utilices_o_borres_el_cupon_activo));
                                                        }else {
                                                            usr_cupon_query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()){
                                                                        S_dialog (getResources().getString(R.string.Codigo_invalido), getResources().getString(R.string.El_codigo_de_promoción_ya_se_ha_aplicado_anteriormente));
                                                                    }else {
                                                                        System.out.println((getResources().getString(R.string.Se_agregaria_el_registro)));
                                                                        Map<String, Object> update = new HashMap<>();
                                                                        update.put("activo",true);
                                                                        if (cupon.getUnidad_descuento().toString().equals("$")) {
                                                                            update.put("cifra",cupon.getMonto_descuento());
                                                                        }else {
                                                                            update.put("cifra","operacion");
                                                                        }
                                                                        update.put("nombre",cupon.getNombre());
                                                                        update.put("categoria",cupon.getCategoria());
                                                                        update.put("descripcion",cupon.getDescripcion());
                                                                        update.put("id",cupon.getId());
                                                                        update.put("paseos",cupon.getPaseos());
                                                                        update.put("perros",cupon.getPerros());
                                                                        update.put("tiempo",cupon.getTiempo());
                                                                        update.put("unidad",cupon.getUnidad_descuento());
                                                                        update.put("timestamp_ingreso", ServerValue.TIMESTAMP);

                                                                        usuario.child("cupones").child(cupon.getId()).setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                S_dialog (getResources().getString(R.string.Codigo_valido), getResources().getString(R.string.Codigo_agregado_correctamente));
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }else {
                                                S_dialog (getResources().getString(R.string.Codigo_no_aplicable),getResources().getString(R.string.El_codigo_de_promocion_solo_es_válido_para_nuevos_usuarios));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }else {
                                    S_dialog (getResources().getString(R.string.Codigo_no_aplicable),getResources().getString(R.string.El_codigo_de_promoción_ya_se_ha_aplicado_anteriormente));
                                }


                            }else {
                                //S_dialog(((getResources().getString(R.string.Codigo_invalido))),((getResources().getString(R.string.El_codigo_de_promoción_no_es_valido_o_ha_caducado)));
                                S_dialog(getResources().getString(R.string.Codigo_invalido), getResources().getString(R.string.El_codigo_de_promoción_no_es_valido_o_ha_caducado));
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }else {
                    S_dialog (getResources().getString(R.string.Ingresa_un_Codigo), getResources().getString(R.string.Debes_ingresar_un_codigo));
                }
            }
        });


        mPeopleRV = (RecyclerView) vista.findViewById(R.id.reccod);

        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.USER_REFERERENCE).child(user.getUid());

        Query personsQuery = personsRef.child("cupones");

        mPeopleRV.hasFixedSize();

        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        mPeopleRV.setLayoutManager(mLayoutManager);




        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<CuponUser>().setQuery(personsQuery, CuponUser.class).build();



        mPeopleRVAdapter = new FirebaseRecyclerAdapter<CuponUser, Codigos_Fragment.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(Codigos_Fragment.PetsViewHolder holder, final int position, final CuponUser model) {
                if (model.isActivo()){
                    holder.setBlueCard(model.getNombre(),"Activo",model.getDescripcion());
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder ( getContext() );
                            builder.setTitle (getResources().getString(R.string.Eliminar_cupon));
                            builder.setMessage (getResources().getString(R.string.Estas_seguro_de_que_deseas_eliminar_este_cupon));
                            builder.setPositiveButton ((getResources().getString(R.string.Aceptar)),new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialog ,int which) {

                                    usuario.child("cupones").child(model.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            S_dialog (getResources().getString(R.string.Eliminado), getResources().getString(R.string.Cupon_eliminado_correctamente));
                                        }
                                    });


                                }
                            });
                            builder.setNegativeButton (getResources().getString(R.string.CANCELAR), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.show ();



                        }
                    });
                }else {
                    holder.setWhiteCard(model.getNombre(),(getResources().getString(R.string.Aplicado)),model.getDescripcion());
                }

            }
            @Override
            public Codigos_Fragment.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_codigos, parent, false);
                return  new Codigos_Fragment.PetsViewHolder(view);
            }
        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);









        //txt_compartir.setPaintFlags(txt_compartir.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //txt_añadir.setPaintFlags(txt_añadir.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return vista;
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

    public static class PetsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PetsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setBlueCard(String title, String status, String desc){
            TextView post_title = (TextView)mView.findViewById(R.id.nombre_cod);
            post_title.setText(title);
            post_title.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.white));
            TextView post_raza = (TextView)mView.findViewById(R.id.status_cod);
            post_raza.setText(status);
            post_raza.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mView.getContext(),R.drawable.ic_img_eliminar_cupon), null);
            post_raza.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.white));
            TextView post_edad = (TextView)mView.findViewById(R.id.desc_cod);
            post_edad.setText(desc);
            post_edad.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.white));
            CardView cardView = mView.findViewById(R.id.card_cod);
            cardView.setCardBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.azul_caminandog));

        }

        public void setWhiteCard(String title, String status, String desc){
            TextView post_title = (TextView)mView.findViewById(R.id.nombre_cod);
            post_title.setText(title);
            TextView post_raza = (TextView)mView.findViewById(R.id.status_cod);
            post_raza.setText(status);
            post_raza.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mView.getContext(),R.drawable.ic_img_utilizado_cupon), null);
            TextView post_edad = (TextView)mView.findViewById(R.id.desc_cod);
            post_edad.setText(desc);
        }




    }

    public void S_dialog (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder ( getContext() );
        builder.setTitle ( title );
        builder.setMessage ( message );
        builder.setPositiveButton (getResources().getString(R.string.Aceptar) ,new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog ,int which) {


            }
        });

        builder.show ();

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
}
