package mx.com.caminandog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Tacking_map extends Fragment {

    GoogleMap gMap;
    MapView mView;
    Marker mCurrentLocationMarker;
    private LinearLayoutManager mLayoutManager;

    //private static final int REQUEST= 112;

    Chronometer crono;
    Chronometer crono_total;

    RecyclerView rv;
    private RecyclerView mPeopleRV;
    adaptador adapter;
    List<ModelDatosPipiPopo> modelDatosPipiPopos;

    String order_id, uid_pas, categoria, nombrepaseador, urlphoto;


    View v;

    String telPaseador;
    Button btn_llam;
    ProgressBar progressllam;

    private FirebaseRecyclerAdapter<Chat, Tacking_map.PetsViewHolder> mPeopleRVAdapter;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;




    private OnFragmentInteractionListener mListener;

    int future;
    int interval;

    public Tacking_map() {
        // Required empty public constructor
    }

    public static Tacking_map newInstance() {
        Tacking_map fragment = new Tacking_map();
        Bundle args = new Bundle();
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
        checkLocationPermission();

        v = inflater.inflate(R.layout.fragment_tacking_map, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        modelDatosPipiPopos = new ArrayList<>();

        ImageButton fotopas = v.findViewById( R.id.img_pas );
        TextView nombpas = v.findViewById( R.id.nom_txt );
        TextView txt_categoria = v.findViewById( R.id.txt_value_cat);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.Paseos_activos));


        Bundle bundle = this.getArguments();
        order_id = bundle.getString( "order_id" );
        categoria = bundle.getString( "categoria" );
        txt_categoria.setText(categoria);
        urlphoto =bundle.getString( "fotopaseador" );
        Glide.with(getActivity()).load(urlphoto).apply(RequestOptions.circleCropTransform()).into(fotopas);
        nombrepaseador = bundle.getString( "nombrepaseador" );
        nombpas.setText( nombrepaseador );
        interval = 1;
        future = 100;



        crono = v.findViewById( R.id.crono_crono );
        crono_total = v.findViewById( R.id.crono_crono_total);

        //crono.setVisibility( View.INVISIBLE );

        btn_llam = v.findViewById(R.id.btn_llam);
        progressllam = v.findViewById(R.id.progressllam);

        btn_llam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaitBotonLlamada();
                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference().child("Paseadores").child(uid_pas);
                Query query_user = userdata;
                query_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotte) {
                        Paseador paseador = dataSnapshotte.getValue(Paseador.class);
                        telPaseador = paseador.getCelular();
                        GetPermisionCall();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        Button btn_chat = v.findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mensaje_ref = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child( order_id).child( "mensaje" );
                mensaje_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                        if (mensaje != null){
                            if (mensaje.isMensajeUsuario()){
                                mensaje_ref.child("mensajeUsuario").setValue(false);
                                btn_chat.setCompoundDrawablesWithIntrinsicBounds(null,null,null,v.getResources().getDrawable(R.drawable.ic_chat));
                            }
                            final DatabaseReference msjRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child( "paseosActivos" ).child(order_id);
                            Query q1 = msjRef.orderByChild("mensaje");
                            q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    PaseosActivos paseosActivos = dataSnapshot.getValue(PaseosActivos.class);
                                    //System.out.println("s "+dataSnapshot.getValue().toString());
                                    if (paseosActivos != null){
                                        if (paseosActivos.isMensaje()){
                                            msjRef.child("mensaje").setValue(false);
                                        }
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
                ChatFragment();
            }
        });

        final DatabaseReference mensaje_ref = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child( order_id ).child( "mensaje" );
        mensaje_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                if (mensaje!=null){
                    if (mensaje.isMensajeUsuario()){
                        //btn_chat.setCompoundDrawablesWithIntrinsicBounds(null,null,null,v.getResources().getDrawable(R.drawable.ic_chat_con_mensaje));
                        btn_chat.setCompoundDrawablesWithIntrinsicBounds(null,null,null,ContextCompat.getDrawable(v.getContext(),R.drawable.ic_chat_con_mensaje));
                    }else{
                        //chat_btn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,inicio.getResources().getDrawable(R.drawable.ic_chat));
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("perrosEstatus");
        Query query = databaseReference;
        rv = v.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelDatosPipiPopos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelDatosPipiPopo model = snapshot.getValue(ModelDatosPipiPopo.class);
                    modelDatosPipiPopos.add(model);
                }
                adapter = new adaptador(getContext(), modelDatosPipiPopos);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), (getResources().getString(R.string.No_se_han_obtenido_datos)), Toast.LENGTH_SHORT).show();
            }
        });

        mView = v.findViewById(R.id.mapatracking);
        mView.onCreate(savedInstanceState);
        mView.onResume();
        mView.getMapAsync(new OnMapReadyCallback() {
            protected void poooo (){
                new CountDownTimer(future, interval) {



                    public void onTick(long millisUntilFinished) {
                        System.out.println("tick"+future+" >>><<< "+interval);
                        //Log.e("seconds remaining: ","" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        final DatabaseReference estatus_paseo =  FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("estatusPaseo");
                        Query oo = estatus_paseo;
                        oo.addValueEventListener( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                future = 45000;
                                interval = 30000;
                                Estatus estatus = dataSnapshot.getValue( Estatus.class );
                                System.out.println( estatus.getEstatus());
                                String estatusString = estatus.getEstatus();
                                if (estatusString.equals( "progreso" )){
                                    onMapReady(gMap);
                                    crono.setBase( SystemClock.elapsedRealtime() );
                                }else if (estatusString.equals( "terminado" )){
                                    FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    Inicio_Fragment fragment2 = new Inicio_Fragment();
                                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Principal");
                                    ft.replace(R.id.contenedor, fragment2);
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                        //Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();

                    }
                }.start();
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.setMapType( GoogleMap.MAP_TYPE_NORMAL);
                poooo();

                final DatabaseReference actualRef = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("ubicacionActual").child("ubicacion");
                Query queryActual = actualRef;
                queryActual.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        actualRef.removeEventListener( this );

                        try {
                            mCurrentLocationMarker.remove();
                        }catch (Exception e){}


                        final DatabaseReference agendaRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child( "paseosActivos" ).child(order_id);
                        agendaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotf) {
                                PaseosActivos paseosActivos = dataSnapshotf.getValue(PaseosActivos.class);

                                if (paseosActivos.getEstatus().equals("agendado")){

                                }else {

                                    try {

                                        ModeloTracking ubicacionActual = dataSnapshot.getValue(ModeloTracking.class);
                                        Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_pin_paseador);
                                        mCurrentLocationMarker = gMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubicacionActual.getLatitud(), ubicacionActual.getLongitud())).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title( nombrepaseador ));

                                    }catch (Exception e){

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        final DatabaseReference refUsuario = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id);
                        Query queryUbicUsuario = refUsuario;

                        queryUbicUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double latit = 0.0;
                                Double longi = 0.0;

                                Paseo paseo = dataSnapshot.getValue(Paseo.class);

                                uid_pas = paseo.getId_paseador();



                                ModeloTracking ubicacionUsuario = dataSnapshot.getValue(ModeloTracking.class);
                                latit = ubicacionUsuario.getLatitud();
                                longi = ubicacionUsuario.getLongitud();
                                Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_pin_casa);
                                gMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latit, longi)).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title( getResources().getString(R.string.Punto_de_recogida)));
                                gMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng( latit,longi ), 16.0f));

                                double tiempo_paseo = paseo.getTiempo_paseo();
                                if (tiempo_paseo == 1){
                                    crono_total.setBase(SystemClock.elapsedRealtime()-3600000);
                                }else if (tiempo_paseo == 2){
                                    crono_total.setBase(SystemClock.elapsedRealtime()-7200000);
                                }else {
                                    crono_total.setBase(SystemClock.elapsedRealtime()-1800000);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        //gMap.getUiSettings().setZoomControlsEnabled(true);
                        //gMap.setMyLocationEnabled(true);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }



                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("tracking");

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        databaseReference.removeEventListener( this );
                        gMap.clear();
                        final DatabaseReference polyref = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("tracking");
                        Query query_ply = polyref.orderByKey();
                        query_ply.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final DatabaseReference paseadores_hora_ref = FirebaseDatabase.getInstance().getReference( FirebaseReferences.PASEO_USR_REFERERENCE ).child( user.getUid() ).child( order_id );
                                paseadores_hora_ref.child( "actualusuario" ).setValue( ServerValue.TIMESTAMP );
                                final ArrayList<LatLng> point = new ArrayList<>();
                                double lattrack = 0;
                                double lontrack = 0;




                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    ModeloTracking modeloTracking = snapshot.getValue(ModeloTracking.class);
                                    lattrack = modeloTracking.getLatitud();
                                    lontrack = modeloTracking.getLongitud();

                                    point.add(new LatLng(modeloTracking.getLatitud(), modeloTracking.getLongitud()));



                                }
                                gMap.clear();
                                Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_pin_paseador);

                                gMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lattrack, lontrack)).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(nombrepaseador));

                                final DatabaseReference refUsuario = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id);
                                Query queryUbicUsuario = refUsuario;

                                queryUbicUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Double latit = 0.0;
                                        Double longi = 0.0;


                                        ModeloTracking ubicacionUsuario = dataSnapshot.getValue(ModeloTracking.class);
                                        latit = ubicacionUsuario.getLatitud();
                                        longi = ubicacionUsuario.getLongitud();
                                        Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_pin_casa);
                                        gMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latit, longi)).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(getResources().getString(R.string.Punto_de_recogida)));




                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                PolylineOptions pl = new PolylineOptions();
                                pl.addAll(point);
                                pl.width(15).color(getResources().getColor(R.color.azul_caminandog));
                                gMap.addPolyline(pl);
                                final DatabaseReference timerref = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child( order_id );
                                timerref.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            Paseo paseo = dataSnapshot.getValue(Paseo.class);

                                            long tiem = paseo.getTiempo_paseo();
                                            long dif_sec = ((paseo.getActualusuario()-paseo.getInicio())/1000/60);
                                            System.out.println("tiem "+dif_sec);

                                            if (tiem == 1){
                                                if (dif_sec>=60){
                                                    crono.setTextColor( Color.RED );
                                                    System.out.println(getResources().getString(R.string.Tiempo_de_entregar_paseo_1_hr));
                                                }
                                            }else if (tiem == 2){
                                                if (dif_sec>=120){
                                                    crono.setTextColor( Color.RED );
                                                    System.out.println(getResources().getString(R.string.Tiempo_de_entregar_paseo_2_hr));
                                                }
                                            }

                                            crono.setBase( SystemClock.elapsedRealtime()-(paseo.getActualusuario()-paseo.getInicio()) );
                                            crono.start();
                                            //crono.setVisibility( View.VISIBLE );

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                        /*final DatabaseReference polyref = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("tracking");
                        Query query_ply = polyref.orderByKey();
                        query_ply.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final ArrayList<LatLng> point = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    ModeloTracking modeloTracking = snapshot.getValue(ModeloTracking.class);

                                    point.add(new LatLng(modeloTracking.getLatitud(), modeloTracking.getLongitud()));
                                }
                                PolylineOptions pl = new PolylineOptions();
                                pl.addAll(point);
                                pl.width(15).color(getResources().getColor(R.color.colorPrimary));
                                gMap.addPolyline(pl);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return v;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public  Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(v.getContext(), drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println("START");

    }
    /*@Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
        System.out.println("STop");
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
        System.out.println("RESTART");

    }*/

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("PAUSED");


    }

    @Override
    public void onResume() {
        super.onResume();
        //mPeopleRVAdapter.startListening();
    }

    private void ChatFragment(){
        /*android.support.v7.app.AlertDialog.Builder mBuilder_add_code = new android.support.v7.app.AlertDialog.Builder(getActivity());
        final View mView2 = getLayoutInflater().inflate(R.layout.dialog_chat_tracking, null);
        mBuilder_add_code.setView(mView2);

        dialog_add_cod = mBuilder_add_code.create();
        dialog_add_cod.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_add_cod.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog_add_cod.show();*/








        Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_chat_tracking, null);
// Build the dialog
        customDialog = new Dialog(getContext(), R.style.CustomDialog);
        customDialog.setContentView(customView);
        customDialog.show();

        TextView txt_name_chat = (TextView) customView.findViewById(R.id.name_chat_txt);
        ImageView fotopas_chat = (ImageView) customView.findViewById( R.id.foto_chat );
        Glide.with(getActivity()).load(urlphoto).apply(RequestOptions.circleCropTransform()).into(fotopas_chat);
        txt_name_chat.setText(nombrepaseador);


        mPeopleRV = (RecyclerView) customView.findViewById(R.id.recchat);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child(order_id).child("chat");

        Query personsQuery = personsRef.orderByKey();

        //mPeopleRV.hasFixedSize();


        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        mPeopleRV.setLayoutManager(mLayoutManager);
        mPeopleRV.setNestedScrollingEnabled(false);
        mPeopleRV.getRecycledViewPool().setMaxRecycledViews(0, 0);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(personsQuery, Chat.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Chat, Tacking_map.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Tacking_map.PetsViewHolder holder, final int position, final Chat model) {


                String name = model.getUid();
                TextView txt_msj2 = (TextView)holder.mView.findViewById(R.id.msj_chat2);
                TextView txt_time2 = (TextView)holder.mView.findViewById(R.id.name_chat2);
                TextView txt_msj = (TextView)holder.mView.findViewById(R.id.msj_chat);
                TextView txt_time = (TextView)holder.mView.findViewById(R.id.name_chat);
                TextView txt_abuse = (TextView)customView.findViewById(R.id.abuse_text);
                txt_abuse.setVisibility(View.GONE);


                if (name.equals(user.getUid())){
                    RelativeLayout cardView = holder.mView.findViewById(R.id.layout_msg_container);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    cardView.setLayoutParams(params);
                    txt_msj2.setText(model.getMensaje());
                    txt_msj.setVisibility(View.GONE);
                    txt_msj2.setBackground(ContextCompat.getDrawable(holder.mView.getContext(),R.drawable.chat_azul));
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis( model.getTimestamp());
                    String date = DateFormat.format("HH:mm", cal).toString();
                    txt_time2.setText(date);
                    txt_time.setVisibility(View.GONE);
                }else {
                    RelativeLayout cardView = holder.mView.findViewById(R.id.layout_msg_container);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    cardView.setLayoutParams(params);
                    txt_msj.setText(model.getMensaje());
                    txt_msj2.setVisibility(View.GONE);
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis( model.getTimestamp());
                    String date = DateFormat.format("HH:mm", cal).toString();
                    txt_time.setText(date);
                    txt_time2.setVisibility(View.GONE);
                    txt_msj.setBackground(ContextCompat.getDrawable(holder.mView.getContext(),R.drawable.chat_gris));
                }
                //holder.setImage(getApplicationContext(), model.getFoto());//
                System.out.println(model.getMensaje());






            }

            @Override
            public Tacking_map.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_chat, parent, false);

                //return new PetsViewHolder(view);
                return  new Tacking_map.PetsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);



        mPeopleRVAdapter.startListening();

        mPeopleRVAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(mPeopleRV, null, mPeopleRVAdapter.getItemCount());

            }
        });




        EditText editText = customView.findViewById(R.id.txt_chat);
        TextView txt_enviando = (TextView)customView.findViewById(R.id.enviando_txt);

        Button btn_send = customView.findViewById(R.id.send_chat);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text;
                text = editText.getText().toString().trim();
                if (text.isEmpty()){
                    System.out.println((getResources().getString(R.string.Esta_vacio)));
                    editText.setText("");
                }else {
                    txt_enviando.setVisibility(View.VISIBLE);
                    btn_send.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_mensaje_para_enviar),null);
                    InputMethodManager imm = (InputMethodManager) customView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    getView().getWindowToken();
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


                    DatabaseReference writeRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child(order_id);

                    String key = writeRef.child("chat").push().getKey();

                    final JSONObject jsonObj_not = new JSONObject();
                    try {
                        jsonObj_not.put("uidUsuarioPaseo",user.getUid());
                        jsonObj_not.put("mensaje",editText.getText().toString());
                        jsonObj_not.put("uidPaseador",uid_pas);
                        jsonObj_not.put("order_id",order_id);
                        jsonObj_not.put("enviarA","Paseadores");
                        jsonObj_not.put("child_key",key);
                        System.out.println(key);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    //
                    OkHttpClient httpClient = new OkHttpClient();
                    HttpUrl.Builder httpBuider =
                            HttpUrl.parse(FirebaseReferences.CHAT_REFERERENCE).newBuilder();
                    httpBuider.addQueryParameter("text",""+jsonObj_not);
                    final Request req = new Request.Builder().
                            url(httpBuider.build()).build();
                    httpClient.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Cambiar controles
                                    editText.setText("");
                                    btn_send.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_mensaje_enviado),null);
                                    txt_enviando.setVisibility(View.GONE);
                                }
                            });

                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
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


                        }

                    });

                }


            }
        });



        Button btn_cancel = customView.findViewById(R.id.cancel_chat);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleRVAdapter.stopListening();
                customDialog.dismiss();
            }
        });

    }

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







    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext (),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity (),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(getContext ())
                        .setTitle(getResources().getString(R.string.Permiso_de_ubicacion))
                        .setMessage(getResources().getString(R.string.Desea_activar_su_ubicacion))
                        .setPositiveButton(getResources().getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                .create()
                .show();
            } else {
                ActivityCompat.requestPermissions(getActivity (),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



    public void WaitBotonLlamada(){
        if (btn_llam.getVisibility()==View.VISIBLE){
            progressllam.setVisibility(View.VISIBLE);
            btn_llam.setVisibility(View.GONE);
        }else if (progressllam.getVisibility()==View.VISIBLE){
            btn_llam.setVisibility(View.VISIBLE);
            progressllam.setVisibility(View.GONE);
        }
    }

    public class adaptador extends RecyclerView.Adapter<adaptador.MyHolder>{
        List<ModelDatosPipiPopo> datosPipiPopos;
        Context ctx;


        public adaptador(Context ctx, List<ModelDatosPipiPopo> datosPipiPopos){
            this.ctx = ctx;
            this.datosPipiPopos = datosPipiPopos;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View view = inflater.inflate(R.layout.card_pipi_popo, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final ModelDatosPipiPopo modelDatosPipiPopo = datosPipiPopos.get(position);
            holder.textViewNombre.setText(modelDatosPipiPopo.getNombre());
            Glide.with(ctx).load(modelDatosPipiPopo.getFoto()).apply( RequestOptions.circleCropTransform()).into(holder.imageViewFoto);
            holder.textViewNumPopo.setText(modelDatosPipiPopo.getPopo().toString());
            holder.textViewNumPipi.setText(modelDatosPipiPopo.getPipi().toString());
        }

        @Override
        public int getItemCount() {
            return modelDatosPipiPopos.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            TextView textViewNombre, textViewNumPipi, textViewNumPopo;
            ImageView imageViewFoto;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                textViewNombre = itemView.findViewById(R.id.txtNombrePerro);
                textViewNumPipi = itemView.findViewById(R.id.txtNumPipi);
                textViewNumPopo = itemView.findViewById(R.id.txtNumPopo);
                imageViewFoto = itemView.findViewById(R.id.imgPerro);
            }
        }
    }


    public static class PetsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PetsViewHolder(View itemViewx){
            super(itemViewx);
            mView = itemViewx;
        }


        public void setColor(String msj,String time){

        }

        public void setColor2(String msj,String time){

        }



    }

    public void callPhone(){
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+telPaseador));
        startActivity(i);
        WaitBotonLlamada();
    }

    public void GetPermisionCall(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {

            callPhone();

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            WaitBotonLlamada();

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
            subtitlemsj.setText(getResources().getString(R.string.Recuerda_que_para_tener_comunicacion_por_telefono));
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
                    Manifest.permission.CALL_PHONE);

        }

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetPermisionCall();
                } else {
                    WaitBotonLlamada();
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
                    subtitlemsj.setText(getResources().getString(R.string.Recuerda_que_para_tener_comunicacion_por_telefono));
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
                }
            });



}
