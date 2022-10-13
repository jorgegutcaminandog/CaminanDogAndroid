package mx.com.caminandog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.facebook.GraphRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ContenedorRecuperandog_Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    View vista;
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;


    private CameraSource cameraSource;
    private SurfaceView cameraView;


    private String token = "";
    private String tokenanterior = "";
    androidx.appcompat.app.AlertDialog dialogf;
    androidx.appcompat.app.AlertDialog dialogx;
    String id_to_assign;

    Calendar vigencia = Calendar.getInstance();

    Button cancel_surf;


    private RecyclerView mPeopleRV;
    private RecyclerView mperrosRV;
    //private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Recuperandog, ContenedorRecuperandog_Fragment.PetsViewHolder> mPeopleRVAdapter;
    private FirebaseRecyclerAdapter<Perro, ContenedorRecuperandog_Fragment.Pets2ViewHolder> mperosRVAdapter;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Bundle bundle = new Bundle();

    public ContenedorRecuperandog_Fragment() {
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
        vista = inflater.inflate(R.layout.fragment_contenedor_recuperandog, container, false);
        Button compra = vista.findViewById(R.id.btn_comprarPlaca);
        Button compraVigencia = vista.findViewById(R.id.btn_ampliarVigencia);
        LinearLayout datosContacto = vista.findViewById(R.id.btn_datosrecup);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ComprarRecuperandog fragment2 = new ComprarRecuperandog();

                ft.replace(R.id.contenedor, fragment2);
                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                ft.commit();

            }
        });
        compraVigencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                AmpliarVigenciaRecuperandog fragment2 = new AmpliarVigenciaRecuperandog();

                ft.replace(R.id.contenedor, fragment2);
                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                ft.commit();

            }
        });
        datosContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                DatosParaContactoRecuperandog fragment2 = new DatosParaContactoRecuperandog();

                ft.replace(R.id.contenedor, fragment2);
                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                ft.commit();

            }
        });


        Button btn_qr = (Button) vista.findViewById(R.id.btn_leer_code);
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCameraPermission();

            }
        });

        Button vincular_btn = vista.findViewById(R.id.btn_vincular_placa);
        vincular_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder mBuilderf = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                final View mViewf = getLayoutInflater().inflate(R.layout.dialog_vincular_placa, null);
                mBuilderf.setView(mViewf);


                dialogf = mBuilderf.create();
                dialogf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                mperrosRV = (RecyclerView) mViewf.findViewById(R.id.recper_recup_recup2);


                final DatabaseReference perosRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.DOGS_REFERERENCE).child(user.getUid());

                Query perosQuery = perosRef.orderByKey();

                mperrosRV.hasFixedSize();
                mperrosRV.setLayoutManager(new LinearLayoutManager(getContext()));

                FirebaseRecyclerOptions perosOptions = new FirebaseRecyclerOptions.Builder<Perro>().setQuery(perosQuery, Perro.class).build();

                mperosRVAdapter = new FirebaseRecyclerAdapter<Perro, ContenedorRecuperandog_Fragment.Pets2ViewHolder>(perosOptions) {
                    @Override
                    protected void onBindViewHolder(ContenedorRecuperandog_Fragment.Pets2ViewHolder holder, final int position, final Perro model) {


                        DatabaseReference recuperandog_service_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE);
                        //recuperandog_service_ref.child("1").setValue("");
                        Query query_service = recuperandog_service_ref.orderByChild("idPerro").equalTo(model.idPerro);
                        query_service.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()) {
                                    System.out.println("id pego no exixte" + model.getIdPerro());
                                    holder.setTitle(model.getNombre());
                                    holder.setImage(getActivity().getBaseContext(), model.getFoto());
                                    holder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            id_to_assign = model.getIdPerro();
                                            GetCameraPermissionVincular();

                                        }
                                    });
                                } else {
                                    System.out.println("id pego" + model.getIdPerro());
                                    System.out.println();

                                    for (DataSnapshot rec_in : dataSnapshot.getChildren()) {
                                        Recuperandog recuperandog = rec_in.getValue(Recuperandog.class);
                                        if (recuperandog.getQR().equals("")) {
                                            System.out.println("if");
                                            holder.setTitle(model.getNombre());
                                            holder.setImage(getActivity().getBaseContext(), model.getFoto());
                                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    id_to_assign = model.getIdPerro();
                                                    GetCameraPermissionVincular();

                                                }
                                            });
                                        } else {
                                            holder.itemView.getLayoutParams().height = 0;
                                            holder.itemView.requestLayout();
                                            System.out.println("else");
                                        }

                                    }


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public ContenedorRecuperandog_Fragment.Pets2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.elemento_lista_perros2_recuperandog, parent, false);
                        return new ContenedorRecuperandog_Fragment.Pets2ViewHolder(view);
                    }
                };


                dialogf.show();


                Button btn_cancel_rec = mViewf.findViewById(R.id.btn_cancel_rec);
                btn_cancel_rec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogf.dismiss();

                    }
                });

                mperrosRV.setAdapter(mperosRVAdapter);
                mperosRVAdapter.startListening();


            }
        });


        mPeopleRV = (RecyclerView) vista.findViewById(R.id.recper_recup_recup);

        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.RECUPERANDOG_REFERENCE);

        Query personsQuery = personsRef.orderByChild("uid").equalTo(user.getUid());

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Recuperandog>().setQuery(personsQuery, Recuperandog.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Recuperandog, ContenedorRecuperandog_Fragment.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(ContenedorRecuperandog_Fragment.PetsViewHolder holder, final int position, final Recuperandog model) {
                System.out.println(model.getIdPerro());

                if (!model.getQR().equals("")) {

                    //System.out.println(model.getIdPerro()+" <<<>>> "+model.getQR());

                    DatabaseReference recuperandog_service_ref = database.getReference(FirebaseReferences.DOGS_REFERERENCE).child(user.getUid()).child(model.getIdPerro());
                    Query query_service = recuperandog_service_ref;
                    query_service.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Perro perro = dataSnapshot.getValue(Perro.class);

                            holder.setTitle(perro.getNombre());
                            holder.setImage(getActivity().getBaseContext(), perro.getFoto());
                            holder.setNoPlaca(model.getQR());
                            if (model.getLectura().equals("true")) {
                                holder.setColor("rojo");
                            } else if (model.getReportado() == null) {
                                personsRef.child(model.getIdPerro()).child("reportado").setValue("false");
                            }else if (model.getReportado().equals("true")){
                                holder.setColor("azul");
                            }
                            if (model.getServicio().equals("true")) {
                                holder.setVigencia(true);
                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        personsRef.child(model.getIdPerro()).child("lectura").setValue("false");
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        Recuperandog_Informacion_Fragment fragment2 = new Recuperandog_Informacion_Fragment();

                                        bundle.putString("foto", perro.getFoto());
                                        bundle.putString("nombre", perro.getNombre());
                                        bundle.putString("idPerro", model.getIdPerro());
                                        fragment2.setArguments(bundle);

                                        ft.replace(R.id.contenedor, fragment2);
                                        //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                                        ft.commit();
                                    }
                                });
                            } else {
                                holder.setVigencia(false);
                            }







                        /*if (dataSnapshot.exists()){

                            Servicio servicio = dataSnapshot.getValue(Servicio.class);
                            System.out.println("servicio "+servicio.getServicio());


                            if (servicio.getServicio().equals("true")){

                                Paseo_recuperandog paseo_recuperandog = dataSnapshot.getValue(Paseo_recuperandog.class);

                                //System.out.println("servicio "+servicio.getServicio());


                                holder.setTitle(model.getNombre());
                                holder.setImage(getActivity().getBaseContext(), model.getFoto());
                                holder.setRaza(model.getRaza());
                                holder.setEdad(model.getEdad());


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










                            }

                        }else{
                            System.out.println("no hay servicio gratis para el perro");
                            holder.itemView.getLayoutParams().height=0;
                            holder.itemView.requestLayout();
                        }*/


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
            public ContenedorRecuperandog_Fragment.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_perros_recuperandog, parent, false);
                return new ContenedorRecuperandog_Fragment.PetsViewHolder(view);
            }
        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);
        return vista;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (Utilidades.rotacion == 0){
            appBar.removeView(pestanas);
        }*/

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class PetsViewHolder extends RecyclerView.ViewHolder {
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

        public void setVigencia(Boolean vigente) {
            TextView post_edad = (TextView) mView.findViewById(R.id.txt_vigencia_rec);
            CardView cardView = mView.findViewById(R.id.card_recup);

            if (vigente) {
                cardView.setCardBackgroundColor(Color.parseColor("#f3edeb"));
                post_edad.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.azul_caminandog));
                post_edad.setText("Servicio_activo");
            } else {
                cardView.setCardBackgroundColor(Color.parseColor("#a49e99"));
                post_edad.setText("Servicio_inactivo");
            }
        }

        public void setColor(String desc) {
            TextView tag = (TextView) mView.findViewById(R.id.tagrecup);
            if (desc.equals("azul")){
                tag.setBackgroundResource(R.drawable.rounded_button_azul_recup);
                tag.setText("Buscando");
            }else if (desc.equals("rojo")){
                tag.setBackgroundResource(R.drawable.rounded_button_red);
                tag.setText("Alerta");
            }

        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.avatar_perro_img_rec);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
    }

    public static class Pets2ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public Pets2ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.txt_nombre_rec2);
            post_title.setText(title);
        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.avatar_perro_img_rec2);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
    }


    public void initQR() {

        androidx.appcompat.app.AlertDialog.Builder mBuilderx = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        final View mViewx = getLayoutInflater().inflate(R.layout.dialog_vincular_surface, null);
        mBuilderx.setView(mViewx);


        final androidx.appcompat.app.AlertDialog dialogx = mBuilderx.create();
        dialogx.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialogx.show();
        dialogf.dismiss();

        cameraView = (SurfaceView) mViewx.findViewById(R.id.camera_view_rec);


        cancel_surf = mViewx.findViewById(R.id.btn_cancel_surf);
        cancel_surf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogx.dismiss();
            }
        });


        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(getContext())
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        // creo la camara
        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() > 0) {

                    token = barcodes.valueAt(0).displayValue;




                    if (!token.equals(tokenanterior)) {
                        //System.out.println("token "+token);
                        String[]div = token.split("=",2);
                        String idQR = div[1];
                        Log.i("token", token);
                        DatabaseReference recuperandog_vinculacion = database.getReference(FirebaseReferences.CODES_REFERENCE);
                        Query query_vinculacion = recuperandog_vinculacion.orderByChild("id_codigo").equalTo(idQR);
                        query_vinculacion.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //System.out.println(dataSnapshot.getValue());
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot issue2 : dataSnapshot.getChildren()) {
                                        Codigo codigo = issue2.getValue(Codigo.class);
                                        String id_perro = codigo.getId_perro();
                                        if (id_perro.equals("")){

                                            getVigencia(new MyCallback() {
                                                @Override
                                                public void onCallback(long value) {
                                                    System.out.println("vigencia called "+value);
                                                    DatabaseReference recuperandog_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child(id_to_assign);
                                                    Map<String, Object> update = new HashMap<>();
                                                    update.put("fechaVinculacion", ServerValue.TIMESTAMP);
                                                    update.put("numero_codigo", codigo.getNumero_codigo());
                                                    update.put("QR", idQR);
                                                    update.put("uid", user.getUid());
                                                    update.put("idPerro", id_to_assign);
                                                    update.put("primeraLectura", "false");
                                                    update.put("servicio", "true");
                                                    update.put("lectura", "false");
                                                    update.put("reportado", "false");
                                                    update.put("fechaVencimiento", value);//checar nombre de campo
                                                    DatabaseReference codigos_ref = database.getReference(FirebaseReferences.CODES_REFERENCE).child(codigo.getNumero_codigo()+"");
                                                    Map<String, Object> update2 = new HashMap<>();
                                                    update2.put("id_perro", id_to_assign);
                                                    update2.put("fecha_vinculacion", ServerValue.TIMESTAMP);

                                                    dialogx.dismiss();
                                                    dialogf.show();

                                                    AlertDialog.Builder builder = new AlertDialog.Builder ( getContext() );

                                                    builder.setTitle (getResources().getString(R.string.Confirmar_vinculacion));
                                                    builder.setMessage (getResources().getString(R.string.Estás_seguro_de_que_deseas_vincular_este_perrito));
                                                    builder.setPositiveButton ( (getResources().getString(R.string.Aceptar)) ,new DialogInterface.OnClickListener () {
                                                        @Override
                                                        public void onClick(DialogInterface dialog ,int which) {
                                                            dialogf.dismiss();
                                                            codigos_ref.updateChildren(update2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    //mPeopleRVAdapter.notifyDataSetChanged();
                                                                    recuperandog_ref.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            dialogf.dismiss();
                                                                            mPeopleRVAdapter.stopListening();
                                                                            mPeopleRVAdapter.startListening();
                                                                        }
                                                                    });
                                                                }
                                                            });



                                                        }
                                                    });
                                                    builder.setNegativeButton((getResources().getString(R.string.CANCELAR)), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });

                                                    builder.show ();

                                                }
                                            });


                                        }else {
                                            Toast.makeText(getContext(),  (getResources().getString(R.string.Ingresa_un_codigo_no_ocupado)), Toast.LENGTH_LONG).show();

                                        }


                                    }


                                }else {
                                    Toast.makeText(getContext(), (getResources().getString(R.string.Ingresa_un_codigo_valido)), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        /*Intent intent = new Intent(Lector_Activity.this, AyudaActitvity.class);
                        intent.putExtra("valores",token);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                        finish();*/

                        tokenanterior = token;


                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();



                    }else {//   System.out.println("token igual"+tokenanterior);
                    }
                }
            }
        });

    }



    public void getVigencia(MyCallback myCallback) {
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

                        Date date = new Date(timestamp); // *1000 is to convert seconds to milliseconds
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.YEAR, 1);
                        myCallback.onCallback(cal.getTimeInMillis());
                        System.out.println(timestamp+" vigencia "+cal.getTimeInMillis());






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        });

    }

    public interface MyCallback {
        void onCallback(long value);
    }

    public void GetCameraPermission(){

        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);

            Intent intent = new Intent(getActivity(), Lector_Activity.class);
            startActivity(intent);



        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            androidx.appcompat.app.AlertDialog.Builder mBuilderfmsj = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            final View mViewfmsj = getLayoutInflater().inflate(R.layout.dialog_si_no, null);
            mBuilderfmsj.setView(mViewfmsj);


            final androidx.appcompat.app.AlertDialog dialogfmsj = mBuilderfmsj.create();
            dialogfmsj.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

            Button simsj = (Button)mViewfmsj.findViewById(R.id.btn_yes);
            Button nomsj = (Button)mViewfmsj.findViewById(R.id.btn_nel);
            TextView titlemsj = (TextView) mViewfmsj.findViewById(R.id.title_dialod);
            TextView subtitlemsj = (TextView) mViewfmsj.findViewById(R.id.subtitle_dialod);

            titlemsj.setText("");
            subtitlemsj.setText (getResources().getString(R.string.Recuerda_que_para_escanear_la_paquita));

            simsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogfmsj.dismiss();
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(i);


                }
            });

            nomsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogfmsj.dismiss();


                }
            });

            dialogfmsj.show();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }

    }

    public void GetCameraPermissionVincular(){

        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);



            initQR();



        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            androidx.appcompat.app.AlertDialog.Builder mBuilderfmsj = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            final View mViewfmsj = getLayoutInflater().inflate(R.layout.dialog_si_no, null);
            mBuilderfmsj.setView(mViewfmsj);


            final androidx.appcompat.app.AlertDialog dialogfmsj = mBuilderfmsj.create();
            dialogfmsj.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

            Button simsj = (Button)mViewfmsj.findViewById(R.id.btn_yes);
            Button nomsj = (Button)mViewfmsj.findViewById(R.id.btn_nel);
            TextView titlemsj = (TextView) mViewfmsj.findViewById(R.id.title_dialod);
            TextView subtitlemsj = (TextView) mViewfmsj.findViewById(R.id.subtitle_dialod);

            titlemsj.setText("");
            subtitlemsj.setText(getResources().getString(R.string.Recuerda_que_para_vincular_la_paquita_de_tu_perrito_deberás));

            simsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogfmsj.dismiss();
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(i);


                }
            });

            nomsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogfmsj.dismiss();


                }
            });

            dialogfmsj.show();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher2.launch(
                    Manifest.permission.CAMERA);
        }

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetCameraPermission();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private ActivityResultLauncher<String> requestPermissionLauncher2 =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetCameraPermissionVincular();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });







}
