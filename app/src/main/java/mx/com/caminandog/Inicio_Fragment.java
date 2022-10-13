package mx.com.caminandog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.GraphRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import okhttp3.ResponseBody;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.List;

import java.util.Locale;
import java.util.Map;


import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Thread.sleep;

//import com.google.android.gms.location.places.Place;


import com.google.android.libraries.places.api.Places;
import com.squareup.timessquare.CalendarPickerView;


public class Inicio_Fragment extends Fragment implements OnMapReadyCallback  {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    boolean rating;

    //MapView mapView;
    //public GoogleMap gMap;
    View v;
    MapView mapView_casa;
    public GoogleMap gMap_casa;
    //LocationManager locationManager;
    //LocationListener locationListener;
    //GoogleApiClient mGoogleApiClient;
    //Location mLocation;
    Marker mCurrentLocationMarker;
    Marker mCurrentLocationMarker_casa;

    ImageView btn_llam;
    String telPaseador;


    private RecyclerView mPeopleRV;

    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<Chat, Inicio_Fragment.PetsViewHolder> mPeopleRVAdapter;
    private FirebaseRecyclerAdapter<PaseosActivos, PetsViewHolderPaseos> mPeopleRVAdapterPaseos;
    private FirebaseRecyclerAdapter<Chat, MainActivity.PetsViewHolder> mPeopleRVAdapter2;

    private GoogleMap mMap;

    Marker marker;

    LinearLayout linearLayout_inf;
    CardView cardView_sup;
    AsyncTask[] asyncTask_reverse_geo;
    ImageView marker_img;

    List<Address> addresses;

    Bundle ss;


    TextView txt_places;
    TextView txt_places_casa;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    int AUTOCOMPLETE_REQUEST_CODE2 = 2;
    LinearLayout lin_direc;
    LinearLayout lin_ground;
    boolean p1, p2, p3,contestada;



    String numero_interior;
    String numero_interior_ultimo;
    String direccion_ultimo;
    String direccion_casa;
    double latitud_casa;
    double longitud_casa;
    double latitud_ultimo;
    double longitud_ultimo;
    ImageView img_casa;
    TextView casa_word;
    ImageView img_ultimo;
    TextView ultimo_word;

    boolean map_click, siHayMsg;




    public final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //final Geocoder geocoder = new Geocoder( getActivity());

    private Geocoder geocoder;

    TextView btn_casa;
    CardView cardView_casa;
    CardView cardView_ultimo;
    TextView text_int;
    ProgressBar prog_start;
    MapStyleOptions mapStyleOptions;
    Animation slideUp;
    Animation slideDown;
    Animation showbottom;

    ListView listView;

    ArrayAdapter<Elemento_Agenda> adapter2;

    String[] s = {"07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21"};
    String[] sm = {"00", "05", "10", "15", "20", "25",
            "30", "35", "40", "45", "50", "55"};

    String datene;

    int num_dias = 3;//cmabiar variables

    androidx.appcompat.app.AlertDialog.Builder mBuildercasa;
    androidx.appcompat.app.AlertDialog dialogcasa;

    List<Place.Field> fields;

    View mView;

    DatabaseReference comote_ref;

    boolean show_encuesta;

    int color_white ;
    int color_caminandog ;

    TextView txt_gps, chatBadge;

    Button openChat;
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();



    public Inicio_Fragment() {
        // Required empty public constructor
        mBuildercasa = null;
        dialogcasa = null;

    }


    public void centreMapOnLocation(Location location){
        if (location != null){
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.clear();
            //marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,20f));
            //locationManager.removeUpdates(locationListener);
        }else{
            //createLocationRequest();
        }
    }




    //Location loc1
    //Location loc2


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_inicio_, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        linearLayout_inf = (LinearLayout) v.findViewById(R.id.comp_inf);
        cardView_sup = (CardView) v.findViewById(R.id.comp_sup);
        TextView nombre = (TextView) v.findViewById(R.id.nombre_txt_inicio);

        color_white = ContextCompat.getColor(getActivity(), R.color.white);
        color_caminandog = ContextCompat.getColor(getActivity(), R.color.azul_caminandog);

        CheckMessages();


        geocoder = new Geocoder(getContext());
        asyncTask_reverse_geo = new AsyncTask[1];

        txt_gps = v.findViewById(R.id.gps);
        txt_gps.setVisibility(View.INVISIBLE);

        ss = savedInstanceState;


        //loc1 = new Location("");


        //loc2 = new Location("");

        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json); //chechar crash al inicio

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        openChat = (Button) v.findViewById(R.id.openchat);
        chatBadge = (TextView) v.findViewById(R.id.textBadge);
        chatBadge.setVisibility(View.GONE);
        openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
                ChatList();

            }
        });

        //start check datos

        final DatabaseReference users_ref = database.getReference(FirebaseReferences.USER_REFERERENCE);
        final DatabaseReference users_ref2 = database.getReference(FirebaseReferences.USER_REFERERENCE).child( user.getUid() );


        final Query checkregister = users_ref.orderByChild("uid").equalTo(user.getUid());
        checkregister.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot issue3:dataSnapshot.getChildren()){
                        final Usuario usuario = issue3.getValue(Usuario.class);
                        if (usuario.getNombre().isEmpty()||usuario.getApellido_Paterno().isEmpty()||usuario.getTelefono1().isEmpty()||usuario.getEmail().isEmpty()){
                            completeRegister();
                        }
                    }
                }else{
                    users_ref2.child("uid").setValue(user.getUid());
                    users_ref2.child("email").setValue(user.getEmail());
                    users_ref2.child("nombre").setValue("");
                    users_ref2.child("apellido_Paterno").setValue("");
                    users_ref2.child("apellido_Materno").setValue("");
                    users_ref2.child("telefono1").setValue("");
                    users_ref2.child("telefono2").setValue("");
                    users_ref2.child("dia").setValue("");
                    users_ref2.child("mes").setValue("");
                    users_ref2.child("anio").setValue("");

                    try {
                        Thread.sleep( 2000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final Query checkregister = users_ref.orderByChild("uid").equalTo(user.getUid());
                    checkregister.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                for (DataSnapshot issue3:dataSnapshot.getChildren()){
                                    Usuario usuario = issue3.getValue(Usuario.class);
                                    if (usuario.getNombre().isEmpty()){
                                        completeRegister();
                                    }
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

        //end checkdatos





        btn_casa = (TextView) v.findViewById(R.id.btn_casa);
        cardView_casa = (CardView) v.findViewById(R.id.card_casa);
        cardView_ultimo = (CardView) v.findViewById(R.id.card_ultimo);
        img_casa = (ImageView) v.findViewById(R.id.casa_img);
        casa_word = (TextView) v.findViewById(R.id.word_casa);

        img_ultimo = (ImageView) v.findViewById(R.id.img_ultimo);
        ultimo_word = (TextView) v.findViewById(R.id.word_ultimo);

        text_int = (TextView) v.findViewById(R.id.int_text);

        TextView txt_ultimo = (TextView) v.findViewById(R.id.direccion_ultimo);


        txt_places = (TextView) v.findViewById(R.id.txt_direc1_princ);

        lin_direc = (LinearLayout) v.findViewById(R.id.linear_direcc);
        lin_direc.setVisibility(View.INVISIBLE);

        lin_ground = (LinearLayout) v.findViewById(R.id.linear_ground);
        lin_ground.setVisibility(View.INVISIBLE);


        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        showbottom = AnimationUtils.loadAnimation(getContext(), R.anim.slide_from_bottom);


        prog_start = (ProgressBar) v.findViewById(R.id.progress_inicio);



        if (FirebaseReferences.NOTIFICATIONS_CONTROL) {
            System.out.println("notifications enabled");
        } else {
            System.out.println("notifications disabled");
        }

        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        final View mView = getLayoutInflater().inflate(R.layout.dialog_confirmar, null);
        mBuilder.setView(mView);

        final Button btn_si = ((Button) mView.findViewById(R.id.btn_si));
        final Button btn_no = ((Button) mView.findViewById(R.id.btn_no));
        final TextView txt_dialog = (TextView) mView.findViewById(R.id.txt_dialog_confirm);
        final ProgressBar prog = ((ProgressBar) mView.findViewById(R.id.progressBarxyz));
        prog.setVisibility(View.INVISIBLE);

        final androidx.appcompat.app.AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        comote_ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid());

        Query comoteQuery = comote_ref.orderByChild("menteraste");

        comoteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                if (dataSnapshot.exists()){

                    prog_start.setVisibility(View.INVISIBLE);


                    nombre.setText(getResources().getString(R.string.Hola) + usuario.getNombre());
//
                    direccion_casa = usuario.getDireccion();
                    latitud_casa = usuario.getLatitud();
                    longitud_casa = usuario.getLongitud();

                    //Toast.makeText(getContext(), "direc_cas"+direccion_casa , Toast.LENGTH_LONG).show();

                    if (direccion_casa == null) {

                        btn_casa.setText(getResources().getString(R.string.Define_una_ubicacion));

                    } else {

                        if (direccion_casa.contains(";")) {
                            String[] parts = direccion_casa.split(";");
                            direccion_casa = parts[0];
                            numero_interior = parts[1];
                        }

                        btn_casa.setText(direccion_casa);
                        latitud_casa = usuario.getLatitud();
                        longitud_casa = usuario.getLongitud();

                    }


                    if (!dataSnapshot.child("menteraste").exists()) {
                        comote_ref.child("menteraste").setValue("");

                    } else {

                        if (usuario.getMenteraste().equals("")) {


                            if (!show_encuesta){
                                show_encuesta = true;
                                String[] itemss = {"Facebook / Instagram", "Medio Impreso", "Recomendación de conocidos", "Algún promotor de Caminandog", "Establecimientos"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(false);
                                builder.setTitle("¿Como nos conociste?");
                                final ListView list = new ListView(getActivity());
                                list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, itemss));
                                builder.setView(list);
                                final AlertDialog dlg = builder.create();
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
                                        comote_ref.child("menteraste").setValue("" + itemss[pos]);
                                        Toast.makeText(getContext(), "¡Gracias!", Toast.LENGTH_LONG).show();
                                        dlg.dismiss();
                                    }
                                });
                                dlg.show();

                            }



                        }
                    }

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference personsRef_calif = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid());

        Query personsQuery_calif = personsRef_calif.orderByChild("fin").endBefore(90000000000000L).limitToLast(1);

        personsQuery_calif.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot issue2 : dataSnapshot.getChildren()) {
                    Paseo paseo = issue2.getValue(Paseo.class);
                    String key = issue2.getKey();
                    //System.out.println("estatus "+paseo.);
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis(paseo.getFin());
                    String date = android.text.format.DateFormat.format("EEEE dd-MM-yyyy HH:mm:ss", cal).toString();


                    final DatabaseReference estatus_paseo = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid() + "/" + key + "/estatusPaseo");
                    Query oo = estatus_paseo;
                    //System.out.println("<<<<key>>>>  "+key+"\n <<<<<ref>>>>> "+estatus_paseo+"\n <<<<<<query>>>>>> "+oo);
                    oo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            Estatus estatus = dataSnapshot2.getValue(Estatus.class);
                            System.out.println("<<<<estatus>>>>  " + estatus.getEstatus()+" orderid "+key+" fecha de fin "+date+" calificado: "+paseo.getVistocalif());

                            if (estatus.getEstatus().equals("terminado")) {
                                //System.out.println("mostrar dialog calif");



                                //agregar consulta de url de paseador

                                final DatabaseReference paseadorRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.PASEADOR_REFERENCE).child(paseo.getId_paseador());
                                final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child(key);
                                Query personsQuery = personsRef;
                                personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {


                                       // for (DataSnapshot issue2 : dataSnapshot3.getChildren()) {
                                            Paseo paseo = dataSnapshot3.getValue(Paseo.class);

                                            //String key = issue2.getKey();
                                            try {
                                                if (paseo.getVistocalif().equals("done")) {
                                                    //System.out.println("equals done" + key);


                                                } else {
                                                    AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(getContext());
                                                    View mView2 = getLayoutInflater().inflate(R.layout.dialog_calif, null);
                                                    mBuilder2.setView(mView2);
                                                    final AlertDialog dialogou = mBuilder2.create();
                                                    dialogou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                                    //calif
                                                    ImageView avatar_walker = (ImageView) mView2.findViewById(R.id.imageView_walker);
                                                    personsRef.child("vistocalif").setValue("done");
                                                    dialogou.show();

                                                    Query ee = paseadorRef;
                                                    ee.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Paseador paseador = dataSnapshot.getValue(Paseador.class);
                                                            //System.out.println("<<<<foto>>>> " + paseador.getDirecfoto() + "\n<<<<nombre>>>> " + paseador.getNombre());

                                                            Glide.with(getContext()).load(paseador.getDirecfoto()).apply(RequestOptions.circleCropTransform()).into(avatar_walker);
                                                            final RatingBar ratingBar1 = (RatingBar) mView2.findViewById(R.id.ratingBar);
                                                            final TextView txt_name = (TextView) mView2.findViewById(R.id.total_txt);
                                                            final TextView txt_namePerr = (TextView) mView2.findViewById(R.id.nombreperrcalif);
                                                            txt_namePerr.setText(paseo.getPerrosNombre());
                                                            txt_name.setText(paseador.getNombre()+" "+paseador.getApellidopa()+" "+paseador.getApellidoma());
                                                            ratingBar1.setStepSize(1);
                                                            ratingBar1.setRating(3);

                                                            ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                                @Override
                                                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                                                    rating = true;

                                                                }
                                                            });





                                                            RadioButton sip1 = mView2.findViewById(R.id.p1siRadio);
                                                            RadioButton nop1 = mView2.findViewById(R.id.p1noRadio);
                                                            RadioButton sip2 = mView2.findViewById(R.id.p2siRadio);
                                                            RadioButton nop2 = mView2.findViewById(R.id.p2noRadio);
                                                            RadioButton sip3 = mView2.findViewById(R.id.p3siRadio);
                                                            RadioButton nop3 = mView2.findViewById(R.id.p3noRadio);





                                                            Button enviar = (Button) mView2.findViewById(R.id.btn_send);

                                                            enviar.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    if (sip1.isChecked()||nop1.isChecked()){
                                                                        contestada = true;
                                                                        if (sip1.isChecked()){
                                                                            p1=true;
                                                                        }else {
                                                                            p1=false;
                                                                        }
                                                                    }else{
                                                                        p1 = true;
                                                                    }
                                                                    if (sip2.isChecked()||nop2.isChecked()){
                                                                        contestada = true;
                                                                        if (sip2.isChecked()){
                                                                            p2=true;
                                                                        }else {
                                                                            p2=false;
                                                                        }
                                                                    }else{
                                                                        p2 = true;
                                                                    }
                                                                    if (sip3.isChecked()||nop3.isChecked()){
                                                                        contestada = true;
                                                                        if (sip3.isChecked()){
                                                                            p3=true;
                                                                        }else {
                                                                            p3=false;
                                                                        }
                                                                    }else{
                                                                        p3 = true;
                                                                    }
                                                                    final JSONObject jsonObj2 = new JSONObject();
                                                                    try {


                                                                        jsonObj2.put("uid", user.getUid());
                                                                        jsonObj2.put("uid_paseador", paseo.id_paseador);
                                                                        jsonObj2.put("calificacion", ratingBar1.getRating());
                                                                        jsonObj2.put("order_id", paseo.getOrder_id());
                                                                        jsonObj2.put("p1", p1);
                                                                        jsonObj2.put("p2", p2);
                                                                        jsonObj2.put("p3", p3);
                                                                        jsonObj2.put("promediar", rating);
                                                                        jsonObj2.put("contestada", contestada);

                                                                        System.out.println("promediar "+rating);


                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();

                                                                    }

                                                                    OkHttpClient httpClient = new OkHttpClient();
                                                                    HttpUrl.Builder httpBuider =
                                                                            HttpUrl.parse(Conecta_Caminandog.CALIF_FUNC).newBuilder();
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
                                                                                    dialog.dismiss();

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

                                                                    dialogou.hide();
                                                                }
                                                            });
                                                            //end calif
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                }

                                            } catch (Exception e) {
                                                personsRef.child(key).child("vistocalif").setValue("");

                                            }


                                       // }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                            direccion_ultimo = paseo.getDireccion();

                            if (direccion_ultimo.contains(";")) {
                                String[] parts = direccion_ultimo.split(";");
                                direccion_ultimo = parts[0];
                                numero_interior_ultimo = parts[1];
                            }


                            txt_ultimo.setText(paseo.getDireccion());
                            latitud_ultimo = paseo.getLatitud();
                            longitud_ultimo = paseo.getLongitud();


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

        marker_img = (ImageView) v.findViewById(R.id.imageView2_markey);
        marker_img.setVisibility(View.INVISIBLE);



        //agenda








        Button agendad = (Button)v.findViewById(R.id.btnEnviarDireccion_paquete);


        agendad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendad.setClickable(false);

                androidx.appcompat.app.AlertDialog.Builder mBuilderf = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                final View mViewf = getLayoutInflater().inflate(R.layout.dialog_agenda, null);
                mBuilderf.setView(mViewf);


                final androidx.appcompat.app.AlertDialog dialogf = mBuilderf.create();
                dialogf.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

                final List<Date> days_selected = new ArrayList<>();
                final List<String> days_selected2 = new ArrayList<>();

                Button btn_3dias =(Button)mViewf.findViewById(R.id.tres_dias);
                Button btn_5dias =(Button)mViewf.findViewById(R.id.cinco_dias);
                Button btn_10dias =(Button)mViewf.findViewById(R.id.diez_dias);






                listView = (ListView) mViewf.findViewById(R.id.list2);
                ArrayList<Elemento_Agenda> moviesList = new ArrayList<>();



                adapter2 = new MovieAdapter(getActivity(),moviesList);
                listView.setAdapter(adapter2);


                final ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, s);
                final ArrayAdapter<String> adpm = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, sm);

                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value

                        String itemValue;
                        //String  itemValue    = (String) listView.getItemAtPosition(position);

                        itemValue = adapter2.getItem(position).getmName();


                        // Show Alert
                        // Toast.makeText(getContext(), itemValue , Toast.LENGTH_LONG).show();


                        adapter2.remove(adapter2.getItem(position));
                        adapter2.notifyDataSetChanged();

                        //adapter2.remove(itemValue);
                        //adapter2.notifyDataSetChanged();

                        if (days_selected2.contains( itemValue )){
                            days_selected2.remove( itemValue );

                        }

                    }

                });


                if (text_int.getText().toString().contains(";")){
                    text_int.setError("Por favor elimina todos los ; de esta casilla");
                    prog_start.setVisibility(View.INVISIBLE);
                    agendad.setClickable(true);

                    text_int.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(text_int, InputMethodManager.SHOW_IMPLICIT);
                }else if (text_int.getText().toString().trim().equals("")
                        ||text_int.getText().toString().contains(";")){



                    text_int.setError("Por favor ingresa el numero exterior y/o interior del domicilio, de no existir coloca SN");
                    prog_start.setVisibility(View.INVISIBLE);
                    agendad.setClickable(true);

                    text_int.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(text_int, InputMethodManager.SHOW_IMPLICIT);




                } else if (txt_places.getText().toString().equals("")||txt_places.getText().toString().equals(" ")){
                    txt_places.setError("Por favor ingresa la calle o el nombre del lugar");
                    prog_start.setVisibility(View.INVISIBLE);
                    agendad.setClickable(true);

                    txt_places.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_places, InputMethodManager.SHOW_IMPLICIT);

                }else {

                    prog_start.setVisibility(View.VISIBLE);

                    //
                    final DatabaseReference timestamp_Ref = FirebaseDatabase.getInstance().getReference().child("sessions");
                    timestamp_Ref.child( "actual" ).setValue( ServerValue.TIMESTAMP );

                    Query timestamp_Query = timestamp_Ref;

                    timestamp_Query.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Sessions sessions = dataSnapshot.getValue(Sessions.class);

                            long timestamp = sessions.getActual();
                            //long timestamp = 1619490415000L;

                            Date dateNextDay12hrs;
                            dateNextDay12hrs = new java.util.Date(timestamp);

                            Calendar caldateNextDay12Hrs = Calendar.getInstance();
                            caldateNextDay12Hrs.setTimeInMillis( timestamp );
                            String dateNexDay12HrsString = "";
                            //System.out.println(caldateNextDay12Hrs.get( Calendar.HOUR_OF_DAY ));

                            if (caldateNextDay12Hrs.get( Calendar.HOUR_OF_DAY )<21){
                                dateNexDay12HrsString = ""+caldateNextDay12Hrs.get( Calendar.YEAR )+"/"+(caldateNextDay12Hrs.get( Calendar.MONTH )
                                        +1)+"/"+(caldateNextDay12Hrs.get( Calendar.DAY_OF_MONTH )+1)+" "+06+":"+00 ;
                            }/*else if (caldateNextDay12Hrs.get( Calendar.DAY_OF_MONTH  ) == 1 && caldateNextDay12Hrs.get( Calendar.HOUR_OF_DAY )<21){//cambiar hora y preguntar
                                dateNexDay12HrsString = ""+caldateNextDay12Hrs.get( Calendar.YEAR )+"/"+(caldateNextDay12Hrs.get( Calendar.MONTH )
                                        +1)+"/"+(caldateNextDay12Hrs.get( Calendar.DAY_OF_MONTH )+1)+" "+12+":"+00 ;//cambiar hora y preguntar
                            }*/else {
                                dateNexDay12HrsString = ""+caldateNextDay12Hrs.get( Calendar.YEAR )+"/"+(caldateNextDay12Hrs.get( Calendar.MONTH )
                                        +1)+"/"+(caldateNextDay12Hrs.get( Calendar.DAY_OF_MONTH )+1)+" "+12+":"+00 ;
                            }



                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                            try {
                                Date dt2 = dateFormat.parse(dateNexDay12HrsString);
                                dateNextDay12hrs.setTime( dt2.getTime() );
                                //Toast.makeText(getContext(), ""+datenew+" j "+date, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                System.out.println(""+e );
                                Toast.makeText(getContext(), "no se pudo format la fecha", Toast.LENGTH_SHORT).show();
                            }



                            dialogf.show();

                            dialogf.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    agendad.setClickable(true);
                                    prog_start.setVisibility(View.INVISIBLE);

                                }
                            });



                            CalendarPickerView calendar = (CalendarPickerView) mViewf.findViewById( R.id.calendarView2 );
                            final Calendar cal = Calendar.getInstance();
                            Date hoy = new java.util.Date(timestamp);
                            Calendar caldia = Calendar.getInstance();
                            caldia.setTime(hoy);
                            int dia = caldia.get(Calendar.DAY_OF_WEEK);
                            int hora = caldia.get(Calendar.HOUR_OF_DAY);
                            Date dateinit;
                            Date datefinit3;
                            /*if(dia==1&&hora>=18){
                                dateinit = new java.util.Date(timestamp+172800000);
                                datefinit3 = new java.util.Date((timestamp)+777600000);
                            }else{
                                dateinit = new java.util.Date(timestamp+86400000);
                                datefinit3 = new java.util.Date((timestamp)+691200000);
                            }*/
                            dateinit = new java.util.Date(timestamp+86400000);
                            datefinit3 = new java.util.Date((timestamp)+691200000);
                            Date datefinit5 = new java.util.Date((timestamp)+1382400000);//
                            Date datefinit10 = new java.util.Date((timestamp)+2592000000l);//



                            System.out.println(dia+" <<>> "+hora+" <<>> "+hoy);

                            Button cont = (Button)mViewf.findViewById(R.id.button_send2);
                            Button cancel = (Button)mViewf.findViewById(R.id.button_cancel);
                            ProgressBar progress_agenda = (ProgressBar) mViewf.findViewById(R.id.progressBar_dialog_agenda);
                            progress_agenda.setVisibility(View.INVISIBLE);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogf.dismiss();
                                }
                            });




                            cont.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    progress_agenda.setVisibility(View.VISIBLE);

                                    if (days_selected2.size()==num_dias){

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
                                        subtitlemsj.setText("Si necesitas modificar alguna fecha ya agendada, ayúdanos a reagendar mínimo con una hora de antelación al paseo. Una vez que el pasedor llegue a tu domicilio se dará por hecho");
                                        simsj.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogfmsj.dismiss();
                                                cont.setClickable(false);
                                                //Toast.makeText( getContext (),text_int.getText()+" "+txt_places.getText(),Toast.LENGTH_LONG).show();

                                                String diasEleccion = "";






                                                for(int i = 0; i < days_selected2.size() ; i++){
                                                    diasEleccion = diasEleccion+days_selected2.get(i)+",";
                                                    synchronized (this) {
                                                        String finalDiasEleccion = diasEleccion;
                                                        getActivity().runOnUiThread(new Runnable()
                                                        {
                                                            public void run()
                                                            {
                                                                //System.out.println(finalDiasEleccion);
                                                            }
                                                        });
                                                    }
                                                    try {
                                                        sleep(100);
                                                        progress_agenda.setVisibility(View.INVISIBLE);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                                Intent intent = new Intent( Inicio_Fragment.this.getActivity (),Configuracion_Paseo.class);
                                                intent.putExtra( "latitud_inicio",mCurrentLocationMarker.getPosition().latitude);
                                                intent.putExtra("longitud_inicio",mCurrentLocationMarker.getPosition().longitude);
                                                intent.putExtra("paquete",true);
                                                intent.putExtra("direccion_direccion",txt_places.getText()+";"+text_int.getText());
                                                intent.putExtra("fecha",sessions.getActual());
                                                intent.putExtra("dias",num_dias);
                                                intent.putExtra("diasEleccion",diasEleccion);


                                                System.out.println("def "+diasEleccion);



                                                prog_start.setVisibility(View.INVISIBLE);

                                                dialogf.dismiss();

                                                cont.setClickable(true);
                                                startActivity(intent);

                                            }
                                        });

                                        nomsj.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogfmsj.dismiss();
                                                progress_agenda.setVisibility(View.GONE);

                                            }
                                        });

                                        dialogfmsj.show();
                                    }else if (days_selected2.size()>num_dias){
                                        Toast.makeText(getContext(), "selecciona solamente "+num_dias+" dias.", Toast.LENGTH_SHORT).show();
                                        progress_agenda.setVisibility(View.INVISIBLE);

                                    }else{
                                        Toast.makeText(getContext(), "selecciona almenos "+num_dias+" dias.", Toast.LENGTH_SHORT).show();
                                        progress_agenda.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                            btn_3dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button));
                            btn_3dias.setTextColor(getApplicationContext().getColor(R.color.white));

                            btn_5dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                            btn_5dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                            btn_10dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                            btn_10dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));


                            btn_3dias.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btn_3dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button));
                                    btn_3dias.setTextColor(getApplicationContext().getColor(R.color.white));

                                    btn_5dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_5dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                                    btn_10dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_10dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                                    num_dias=3;
                                    calendar.init(dateinit,datefinit3).inMode( CalendarPickerView.SelectionMode.SINGLE );


                                    for (int i = adapter2.getCount() - 1; i >= 0; i--) {
                                        if (adapter2.getItem(i).getTimest()>datefinit3.getTime()){
                                            String itemValue;
                                            itemValue = adapter2.getItem(i).getmName();
                                            if (days_selected2.contains( itemValue )){
                                                days_selected2.remove( itemValue );
                                            }
                                            adapter2.remove(adapter2.getItem(i));
                                            adapter2.notifyDataSetChanged();
                                        }

                                    }


                                }
                            });

                            btn_5dias.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btn_5dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button));
                                    btn_5dias.setTextColor(getApplicationContext().getColor(R.color.white));

                                    btn_3dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_3dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                                    btn_10dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_10dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));
                                    num_dias=5;
                                    calendar.init(dateinit,datefinit5).inMode( CalendarPickerView.SelectionMode.SINGLE);

                                    for (int i = adapter2.getCount() - 1; i >= 0; i--) {
                                        if (adapter2.getItem(i).getTimest()>datefinit5.getTime()){
                                            String itemValue;
                                            itemValue = adapter2.getItem(i).getmName();
                                            if (days_selected2.contains( itemValue )){
                                                days_selected2.remove( itemValue );
                                            }
                                            adapter2.remove(adapter2.getItem(i));
                                            adapter2.notifyDataSetChanged();
                                        }

                                    }


                                }
                            });

                            btn_10dias.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btn_10dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button));
                                    btn_10dias.setTextColor(getApplicationContext().getColor(R.color.white));

                                    btn_3dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_3dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                                    btn_5dias.setBackground(getApplicationContext().getDrawable(R.drawable.rounded_button_grey));
                                    btn_5dias.setTextColor(getApplicationContext().getColor(R.color.gris_texto));

                                    num_dias=10;
                                    calendar.init(dateinit,datefinit10).inMode( CalendarPickerView.SelectionMode.SINGLE);


                                }
                            });






                            if (num_dias==3){
                                calendar.init(dateinit,datefinit3).inMode( CalendarPickerView.SelectionMode.SINGLE );
                            }else if( num_dias == 5 ) {
                                calendar.init(dateinit,datefinit5).inMode( CalendarPickerView.SelectionMode.SINGLE);
                            }else if( num_dias == 10){
                                calendar.init(dateinit,datefinit10).inMode( CalendarPickerView.SelectionMode.SINGLE);
                            }



                            calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                                @Override
                                public void onDateSelected(Date date) {

                                    listView.setVisibility(View.VISIBLE);

                                    final Calendar calselected = Calendar.getInstance();
                                    calselected.setTime( date );

                                    //Toast.makeText(getContext(), "days selected"+days_selected.size()+" num dias"+num_dias, Toast.LENGTH_SHORT).show();

                                    //if (calendar.getSelectedDates().size()>=num_dias){
                                    if (days_selected2.size()>=num_dias){
                                        //analize dialog
                                        Toast.makeText(getContext(), "mas de "+num_dias+" no permitido ", Toast.LENGTH_SHORT).show();
                                    }else {
                                        // Toast.makeText(Agenda.this, "permitido", Toast.LENGTH_SHORT).show();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Selecciona una hora para: "+calselected.get( Calendar.YEAR )+"/"+(calselected.get( Calendar.MONTH )
                                                +1)+"/"+calselected.get( Calendar.DAY_OF_MONTH ));

                                        View holder=View.inflate(getActivity().getBaseContext(), R.layout.spinners, null);
                                        builder.setView(holder);
                                        final Spinner spinner1 = (Spinner) holder.findViewById(R.id.spinner1);
                                        spinner1.setAdapter(adp);
                                        final Spinner spinner2 = (Spinner) holder.findViewById(R.id.spinner2);
                                        spinner2.setAdapter(adpm);



                                        // Set up the buttons
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String datenew =""+calselected.get( Calendar.YEAR )+"/"+(calselected.get( Calendar.MONTH )
                                                        +1)+"/"+calselected.get( Calendar.DAY_OF_MONTH )+" "+String.valueOf( spinner1.getSelectedItem() )+":"+String.valueOf( spinner2.getSelectedItem()) ;



                                                //SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa",Locale.ENGLISH);

                                                try {
                                                    Date dt2 = dateFormat.parse(datenew);

                                                    date.setTime( dt2.getTime() );
                                                    //Toast.makeText(getContext(), ""+datenew+" j "+date, Toast.LENGTH_SHORT).show();

                                                } catch (Exception e) {
                                                    System.out.println(""+e );

                                                    Toast.makeText(getContext(), "no se pudo format la fecha", Toast.LENGTH_SHORT).show();

                                                }

                                                if (date.getTime()<dateNextDay12hrs.getTime()){
                                                    System.out.println("fecha seleccionada antes de las 12pm del dia siguente <<>> "+date.toString()+" <<>> "+dateNextDay12hrs.toString());
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                    builder.setTitle("Ohh no!");
                                                    builder.setMessage("Desafortunadamente no podemos encontrar un DogLover para ti a la hora seleccionada, intenta agendar despues de las 12:00pm");
                                                    builder.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();

                                                        }
                                                    });
                                                    builder.show();
                                                }else  {
                                                    System.out.println("fecha en rango "+date.toString()+" <<>> "+dateNextDay12hrs.toString());


                                                    days_selected.add( date );

                                                    //dias_txt.setText( "" );

                            /*for (int i = 0; i < days_selected.size(); i++) {
                                System.out.println(days_selected.get(i));

                                dias_txt.append( days_selected.get(i).toString()+"\n" );
                            }*/
                                                    for (int i = 0; i < days_selected.size(); i++) {
                                                        cal.setTime( days_selected.get(i) );



                                                        String dia = "dia";
                                                        switch (cal.get( Calendar.DAY_OF_WEEK )){
                                                            case  1:
                                                                dia = "domingo";
                                                                break;

                                                            case  2:
                                                                dia = "lunes";
                                                                break;

                                                            case  3:
                                                                dia = "martes";
                                                                break;

                                                            case  4:
                                                                dia = "miercoles";
                                                                break;

                                                            case  5:
                                                                dia = "jueves";
                                                                break;

                                                            case  6:
                                                                dia = "viernes";
                                                                break;

                                                            case  7:
                                                                dia = "sabado";
                                                                break;
                                                        }

                                                        String mes = "mes";
                                                        switch  (cal.get( Calendar.MONTH ) +1){
                                                            case  1:
                                                                mes = "enero";
                                                                break;

                                                            case  2:
                                                                mes = "febrero";
                                                                break;

                                                            case  3:
                                                                mes = "marzo";
                                                                break;

                                                            case  4:
                                                                mes = "abril";
                                                                break;

                                                            case  5:
                                                                mes = "mayo";
                                                                break;

                                                            case  6:
                                                                mes = "junio";
                                                                break;

                                                            case  7:
                                                                mes = "julio";
                                                                break;

                                                            case  8:
                                                                mes = "agosto";
                                                                break;

                                                            case  9:
                                                                mes = "septiembre";
                                                                break;

                                                            case  10:
                                                                mes = "octubre";
                                                                break;

                                                            case  11:
                                                                mes = "noviembre";
                                                                break;

                                                            case  12:
                                                                mes = "diciembre";
                                                                break;
                                                        }

                                                        String sMinutes = String.valueOf(cal.get( Calendar.MINUTE));
                                                        switch (cal.get( Calendar.MINUTE)){
                                                            case 0:
                                                                sMinutes = String.format("%02d", cal.get( Calendar.MINUTE));
                                                                break;
                                                            case 5:
                                                                sMinutes = String.format("%02d", cal.get( Calendar.MINUTE));
                                                                break;
                                                        }

                                                        String sHoras = String.valueOf(cal.get( Calendar.HOUR_OF_DAY));
                                                        switch (cal.get( Calendar.HOUR_OF_DAY)){
                                                            case 6:
                                                                sHoras = String.format("%02d", cal.get( Calendar.HOUR_OF_DAY));
                                                                break;
                                                            case 7:
                                                                sHoras = String.format("%02d", cal.get( Calendar.HOUR_OF_DAY));
                                                                break;
                                                            case 8:
                                                                sHoras = String.format("%02d", cal.get( Calendar.HOUR_OF_DAY));
                                                                break;
                                                            case 9:
                                                                sHoras = String.format("%02d", cal.get( Calendar.HOUR_OF_DAY));
                                                                break;
                                                        }
                                                        //sábado 04 mayo 2019 a las 12:02
                                                        datene="";
                                                        //datene =""+dia+" "+cal.get( Calendar.DAY_OF_MONTH )+" "+mes+" "+cal.get( Calendar.YEAR )+" a las "+cal.get( Calendar.HOUR_OF_DAY)+":"+cal.get( Calendar.MINUTE) ;
                                                        datene =""+dia+" "+cal.get( Calendar.DAY_OF_MONTH )+" "+mes+" "+cal.get( Calendar.YEAR )+" a las "+sHoras+":"+sMinutes ;

                                                        System.out.println( datene );








                                                        //dias_txt.append( datene+"\n" );

                                                    }

                                                    //adapter2.clear();


                                                    listView.animate().alpha(18f);
                                                    listView.clearAnimation();
                                                    days_selected2.add(datene);

                                                    moviesList.add(new Elemento_Agenda(datene,date.getTime()));
                                                    adapter2.notifyDataSetChanged();
                                                    //adapter2.setNotifyOnChange(true);
                                                    //listView.notify();
                                                }






                                            }

                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                        builder.show();








                                    }

                                }

                                @Override
                                public void onDateUnselected(Date date) {

                                }
                            });







                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            prog_start.setVisibility(View.INVISIBLE);
                            agendad.setVisibility(View.VISIBLE);

                        }
                    } );

                }



            }
        });






        //end agenda






        return v;
    }

    private void CheckMessages(){

        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("paseosActivos");

        Query personsQuery = personsRef;

        personsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {


                final DatabaseReference ChatsCamRef = FirebaseDatabase.getInstance().getReference().child("Chats_usuario_caminandog").child("tabla_chat").child(user.getUid());
                Query query = ChatsCamRef;

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshott) {
                        if (dataSnapshot.exists()){
                            siHayMsg = true;
                        }

                        int contador = 0;
                        if (dataSnapshott.exists()){
                            MensajesCaminandog mensajesCaminandog = dataSnapshott.getValue(MensajesCaminandog.class);
                            if (!mensajesCaminandog.isMensaje_sin_responder()){
                                contador++;
                            }else{
                                //contador--;
                            }
                        }

                        for (DataSnapshot issue2 : dataSnapshot.getChildren()) {
                            PaseosActivos paseosActivos = issue2.getValue(PaseosActivos.class);
                            System.out.println("chats "+paseosActivos.isMensaje());
                            if (paseosActivos.isMensaje()){
                                contador++;
                            }


                        }

                        if (contador <= 0){
                            chatBadge.setVisibility(View.GONE);
                            openChat.setBackgroundResource(R.drawable.ic_chat_inicio);
                        }else {

                            chatBadge.setVisibility(View.VISIBLE);
                            chatBadge.setText(contador+"");
                            openChat.setBackgroundResource(R.drawable.ic_chatbadgewith);
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    public void completeRegister(){



        Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_complete_register, null);
        customDialog = new Dialog(getActivity(), R.style.CustomDialogRegister);
        customDialog.setContentView(customView);
        customDialog.setCancelable(false);
        customDialog.show();

        EditText txtName = customDialog.findViewById(R.id.txt_c_name);
        EditText txtAp = customDialog.findViewById(R.id.txt_c_apMat);
        EditText txtEmail = customDialog.findViewById(R.id.txt_c_email);
        EditText txtTel = customDialog.findViewById(R.id.txt_c_tel);
        Button saveBtn = customDialog.findViewById(R.id.guardarReg);



        if (!user.getEmail().equals("")){
            txtEmail.setEnabled(false);
            txtEmail.setText(user.getEmail());
        }
        txtName.setText(user.getDisplayName());
        txtTel.setText(user.getPhoneNumber());


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = txtName.getText().toString().trim();
                String apellido = txtAp.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String telefono = txtTel.getText().toString().trim();

                if (nombre.equals("")){
                    txtName.setError("Este campo es obligatorio");
                    return;
                }else if(apellido.equals("")){
                    txtAp.setError("Este campo es obligatorio");
                    return;
                }else if (email.equals("")){
                    txtEmail.setError("Este campo es obligatorio");
                    return;
                }else if (telefono.equals("")){
                    txtTel.setError("Este campo es obligatorio");
                    return;
                }else if (telefono.length() < 10){
                    txtTel.setError("Ingresa un numero de 10 digitos");
                    return;
                }else {
                    final Map<String, Object> registro = new HashMap<>();
                    registro.put("nombre", nombre);
                    registro.put("apellido_Paterno", apellido);
                    registro.put("email",email);
                    registro.put("telefono1",telefono);
                    //registro.put("proveedor",user.getMetadata().toString());


                    System.out.println(registro.toString());

                    DatabaseReference addClaveRef = database.getReference("Usuarios").child(user.getUid());
                    addClaveRef.updateChildren(registro).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            customDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }




            }
        });









    }

    public void ChatList(){



        Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_chat_list, null);
        customDialog = new Dialog(getActivity(), R.style.CustomDialog2);
        customDialog.setContentView(customView);
        customDialog.show();

        TextView txtUltimoMsg = customDialog.findViewById(R.id.chatlistUltimoMCam);
        TextView txtUltimoMsgFecha = customDialog.findViewById(R.id.chatlistFechaCam);
        ImageView txtImgMsg = customDialog.findViewById(R.id.chatlistImgMsgCam);
        LinearLayout startChatCam = customDialog.findViewById(R.id.chatlistLinLay);

        TextView txtNoPaseosg = (TextView)customDialog.findViewById(R.id.nopaseostitle);
        TextView txtNoPaseosS = (TextView)customDialog.findViewById(R.id.nopaseosSubtitle);
        if (siHayMsg){
            txtNoPaseosg.setVisibility(View.GONE);
            txtNoPaseosS.setVisibility(View.GONE);
        }




        startChatCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatFragmentCaminandog();
            }
        });

        final DatabaseReference ChatsCamRef = FirebaseDatabase.getInstance().getReference().child("Chats_usuario_caminandog").child("tabla_chat").child(user.getUid());
        Query query = ChatsCamRef;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    MensajesCaminandog mensajesCaminandog = dataSnapshot.getValue(MensajesCaminandog.class);
                    txtUltimoMsg.setText(mensajesCaminandog.getUltimo_texto_mensaje());
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis( mensajesCaminandog.getHora_ultimo_msj());
                    String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
                    txtUltimoMsgFecha.setText(date);
                    if (!mensajesCaminandog.isMensaje_sin_responder()){
                        txtImgMsg.setVisibility(View.VISIBLE);
                    }else {
                        txtImgMsg.setVisibility(View.GONE);
                        txtUltimoMsgFecha.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gris_caminandog));
                    }
                }else {
                    txtImgMsg.setVisibility(View.GONE);
                    txtUltimoMsgFecha.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gris_caminandog));
                }




            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });


        mPeopleRV = (RecyclerView) customView.findViewById(R.id.recchatp);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("paseosActivos");

        Query personsQuery = personsRef;

        //mPeopleRV.hasFixedSize();


        mPeopleRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mPeopleRV.setLayoutManager(mLayoutManager);
        mPeopleRV.setNestedScrollingEnabled(false);
        mPeopleRV.getRecycledViewPool().setMaxRecycledViews(0, 0);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<PaseosActivos>().setQuery(personsQuery, PaseosActivos.class).build();

        mPeopleRVAdapterPaseos = new FirebaseRecyclerAdapter<PaseosActivos, Inicio_Fragment.PetsViewHolderPaseos>(personsOptions) {

            @Override
            protected void onBindViewHolder(final Inicio_Fragment.PetsViewHolderPaseos holder, final int position, final PaseosActivos model) {
                holder.setNombre(model.getNombre_paseador());
                holder.setImage(getActivity().getBaseContext(),model.foto_paseador);
                holder.setTexto(model.getUltimo_msj_texto());
                Calendar cal = Calendar.getInstance(Locale.getDefault());
                System.out.println("timestamp ultimo msj "+model.getUltimo_msj_timestamp());
                if (model.getUltimo_msj_timestamp()==0){
                    holder.mView.findViewById(R.id.chatlistFecha).setVisibility(View.GONE);
                }
                cal.setTimeInMillis( model.getUltimo_msj_timestamp());
                String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
                holder.setFecha(date);//"dd/MM/yy HH:mm"
                Calendar cal2 = Calendar.getInstance(Locale.getDefault());
                cal2.setTimeInMillis( model.getTimestamp());
                String date3 = android.text.format.DateFormat.format("EEEE dd/MM/yy", cal2).toString();
                String date2 = android.text.format.DateFormat.format("hh:mm a", cal2).toString();
                holder.setFechaTitulo("Paseo del "+date3+" a las "+date2);
                holder.setImageMsg(model.isMensaje());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChatFragment(model.getNombre_paseador(),model.getOrder_id(),model.foto_paseador);
                    }
                });
            }

            @Override
            public Inicio_Fragment.PetsViewHolderPaseos onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_chat_list, parent, false);

                //return new PetsViewHolder(view);
                return  new Inicio_Fragment.PetsViewHolderPaseos(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapterPaseos);



        mPeopleRVAdapterPaseos.startListening();

        mPeopleRVAdapterPaseos.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(mPeopleRV, null, mPeopleRVAdapterPaseos.getItemCount());

            }
        });

        Button btn_cancel = customView.findViewById(R.id.cancel_chat);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeopleRVAdapterPaseos.stopListening();
                customDialog.dismiss();
            }
        });



    }

    public void WaitBotonLlamada(){
        if (btn_llam.getVisibility()==View.VISIBLE){

            btn_llam.setVisibility(View.GONE);
        }else {
            btn_llam.setVisibility(View.VISIBLE);

        }
    }

    private void ChatFragment(String nombrepaseador ,String order_id, String urlphoto){



        final DatabaseReference PaseosUsuariosRef = FirebaseDatabase.getInstance().getReference().child("Paseos_usuarios").child(user.getUid()).child(order_id);
        Query query = PaseosUsuariosRef;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                Paseo paseo = dataSnapshot.getValue(Paseo.class);

                Dialog customDialog;
                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                View customView = inflater.inflate(R.layout.dialog_chat_tracking, null);
                customDialog = new Dialog(getContext(), R.style.CustomDialog);
                customDialog.setContentView(customView);
                customDialog.show();

                TextView txt_name_chat = (TextView) customView.findViewById(R.id.name_chat_txt);
                ImageView fotopas_chat = (ImageView) customView.findViewById( R.id.foto_chat );
                Glide.with(getActivity()).load(urlphoto).apply(RequestOptions.circleCropTransform()).into(fotopas_chat);
                txt_name_chat.setText(nombrepaseador);

                btn_llam =  customDialog.findViewById(R.id.btn_llamcha);

                btn_llam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WaitBotonLlamada();
                        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference().child("Paseadores").child(paseo.getId_paseador());
                        Query query_user = userdata;
                        query_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotte) {
                                Paseador paseador = dataSnapshotte.getValue(Paseador.class);
                                telPaseador = paseador.getCelular();

                                //GetPermisionCall();

                                if (GetPermisionCall()) {
                                    WaitBotonLlamada();
                                    Intent i = new Intent(Intent.ACTION_CALL);
                                    i.setData(Uri.parse("tel:"+telPaseador));
                                    startActivity(i);
                                }else {
                                    WaitBotonLlamada();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


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

                mPeopleRVAdapter = new FirebaseRecyclerAdapter<Chat, Inicio_Fragment.PetsViewHolder>(personsOptions) {
                    @Override
                    protected void onBindViewHolder(final Inicio_Fragment.PetsViewHolder holder, final int position, final Chat model) {


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
                            String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
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
                            String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
                            txt_time.setText(date);
                            txt_time2.setVisibility(View.GONE);
                            txt_msj.setBackground(ContextCompat.getDrawable(holder.mView.getContext(),R.drawable.chat_gris));
                        }
                        //holder.setImage(getApplicationContext(), model.getFoto());//
                        System.out.println(model.getMensaje());






                    }

                    @Override
                    public Inicio_Fragment.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.elemento_chat, parent, false);

                        //return new PetsViewHolder(view);
                        return  new Inicio_Fragment.PetsViewHolder(view);
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
                            System.out.println("etsa vacio");
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
                                jsonObj_not.put("uidPaseador",paseo.getId_paseador());
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

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });



    }

    private void ChatFragmentCaminandog(){
        /*android.support.v7.app.AlertDialog.Builder mBuilder_add_code = new android.support.v7.app.AlertDialog.Builder(getActivity());
        final View mView2 = getLayoutInflater().inflate(R.layout.dialog_chat_tracking, null);
        mBuilder_add_code.setView(mView2);

        dialog_add_cod = mBuilder_add_code.create();
        dialog_add_cod.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_add_cod.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog_add_cod.show();*/










        Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_chat_soporte, null);
// Build the dialog
        customDialog = new Dialog(getContext(), R.style.CustomDialog);
        customDialog.setContentView(customView);
        customDialog.show();

        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Chats_usuario_caminandog").child("configChat");
        Query query = dataRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                ConfigChat configChat = dataSnapshot.getValue(ConfigChat.class);
                TextView txt_name_chat = (TextView) customView.findViewById(R.id.name_chat_txt);
                ImageView fotopas_chat = (ImageView) customView.findViewById( R.id.foto_chat );
                Glide.with(getContext()).load(configChat.getFoto()).apply(RequestOptions.circleCropTransform()).into(fotopas_chat);
                txt_name_chat.setText(configChat.getNombre());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });




        mPeopleRV = (RecyclerView) customView.findViewById(R.id.recchat);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Chats_usuario_caminandog").child("chats").child(user.getUid());

        Query personsQuery = personsRef.orderByChild("timestamp").startAt(1209600000);

        //mPeopleRV.hasFixedSize();


        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        mPeopleRV.setLayoutManager(mLayoutManager);
        mPeopleRV.setNestedScrollingEnabled(false);
        mPeopleRV.getRecycledViewPool().setMaxRecycledViews(0, 0);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(personsQuery, Chat.class).build();

        mPeopleRVAdapter2 = new FirebaseRecyclerAdapter<Chat, MainActivity.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final MainActivity.PetsViewHolder holder, final int position, final Chat model) {


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
                    String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
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
                    String date = android.text.format.DateFormat.format("hh:mm a", cal).toString();
                    txt_time.setText(date);
                    txt_time2.setVisibility(View.GONE);
                    txt_msj.setBackground(ContextCompat.getDrawable(holder.mView.getContext(),R.drawable.chat_gris));
                }
                //holder.setImage(getApplicationContext(), model.getFoto());//
                System.out.println(model.getMensaje());






            }

            @Override
            public MainActivity.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_chat, parent, false);

                //return new PetsViewHolder(view);
                return  new MainActivity.PetsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter2);



        mPeopleRVAdapter2.startListening();

        mPeopleRVAdapter2.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(mPeopleRV, null, mPeopleRVAdapter2.getItemCount());

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
                    System.out.println("etsa vacio");
                    editText.setText("");
                }else {
                    txt_enviando.setVisibility(View.VISIBLE);
                    btn_send.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_mensaje_para_enviar),null);
                    InputMethodManager imm = (InputMethodManager) customView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    customView.getWindowToken();
                    imm.hideSoftInputFromWindow(customView.getWindowToken(), 0);


                    DatabaseReference writeRef = FirebaseDatabase.getInstance().getReference().child("Chats_usuario_caminandog").child("chats").child(user.getUid());

                    String key = writeRef.push().getKey();

                    final JSONObject jsonObj_not = new JSONObject();
                    try {
                        jsonObj_not.put("uidUsuario",user.getUid());
                        jsonObj_not.put("mensaje",editText.getText().toString());
                        jsonObj_not.put("emisor_mensaje","usuario");
                        jsonObj_not.put("child_key",key);
                        System.out.println(key);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    //
                    OkHttpClient httpClient = new OkHttpClient();
                    HttpUrl.Builder httpBuider =
                            HttpUrl.parse(FirebaseReferences.CHAT_REFERERENCE_CAMINANDOG).newBuilder();
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
                mPeopleRVAdapter2.stopListening();
                customDialog.dismiss();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //createLocationRequest();
        mMap = googleMap;
        mMap.setMinZoomPreference(6.0f);

        final Dialog dialogw = new Dialog(getActivity());
        dialogw.setCancelable(false);
        //DismissLoading();

        View viewww  = getLayoutInflater().inflate(R.layout.dialog_si_no, null);
        dialogw.setContentView(viewww);

        dialogw.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = (TextView) viewww.findViewById(R.id.title_dialod);
        TextView subtitle = (TextView) viewww.findViewById(R.id.subtitle_dialod);

        title.setText( "Gps Desactivado" );
        subtitle.setText( "Por favor activa el Gps de tu dispositivo" );

        Button si = (Button)viewww.findViewById(R.id.btn_yes);
        Button no = (Button)viewww.findViewById(R.id.btn_nel);

        no.setVisibility( View.GONE );
        si.setText( "activar" );

        si.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } );

        //

        //




        LatLng midLatLng = mMap.getCameraPosition().target;

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng midLatLng = mMap.getCameraPosition().target;
                if (marker!=null) marker.setPosition(midLatLng);

            }
        });


        //



        //gMap.setPadding(0,0,0,mapView.getHeight());


        mMap.setMapStyle(mapStyleOptions);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        lin_direc.setVisibility(View.VISIBLE);
        lin_direc.startAnimation(slideUp);

        lin_ground.setVisibility(View.VISIBLE);
        lin_ground.startAnimation(showbottom);

        //lin_direc.animate().translationY(getView().getHeight());


        mMap.setPadding(0, cardView_sup.getHeight() - 15, 0, linearLayout_inf.getHeight() + 15);


        mMap.clear();


        mMap.getUiSettings().setZoomControlsEnabled(true);



        Places.initialize(getApplicationContext(), "AIzaSyAzVCOxZQYQuMrA6lfgJC8hOM0zyYcasdY");


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAzVCOxZQYQuMrA6lfgJC8hOM0zyYcasdY");
        }


        // Initialize the AutocompleteSupportFragment.

        //AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
        //      getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);


        txt_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the autocomplete intent.
                txt_places.setText("");
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("MX")
                        .build(getActivity());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

        cardView_ultimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(latitud_ultimo + " <<<<>>>> " + longitud_ultimo);
                if (latitud_ultimo != 0 && longitud_ultimo != 0) {

                    mMap.clear();
                    LatLng latLng2 = new LatLng(latitud_ultimo, longitud_ultimo);
                    //gMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng1,18.0f));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 18.0f));


                    cardView_ultimo.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_caminandog));
                    img_ultimo.setImageResource(R.drawable.img_ultimo_positivoxxxhdpi);
                    casa_word.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_caminandog));

                    ultimo_word.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    text_int.setText(numero_interior_ultimo);
                    cardView_casa.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    img_casa.setImageResource(R.drawable.img_casa_negativoxxxhdpi);

                }


            }
        });

        cardView_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (direccion_casa == null) {

                    agregar_casa();

                } else {
                    mMap.clear();
                    LatLng latLng1 = new LatLng(latitud_casa, longitud_casa);
                    //gMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng1,18.0f));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 18.0f));

                    cardView_ultimo.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    img_ultimo.setImageResource(R.drawable.img_ultimo_negativoxxxhdpi);
                    ultimo_word.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_caminandog));


                    cardView_casa.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_caminandog));
                    img_casa.setImageResource(R.drawable.img_casa_positivoxxxhdpi);
                    casa_word.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    text_int.setText(numero_interior);

                }


            }
        });

        cardView_casa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (direccion_casa != null) {
                    agregar_casa();
                }


                return false;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);
                if (!map_click) {
                    mMap.setPadding(0, cardView_sup.getHeight() - 15, 0, linearLayout_inf.getHeight() + 15);
                    marker_img.setPadding(0, cardView_sup.getHeight() - 160, 0, linearLayout_inf.getHeight() + 15);
                } else {
                    mMap.setPadding(0, 0, 0, linearLayout_inf.getHeight() + 15);
                    //marker_img.setPadding(0,160,0,linearLayout_inf.getHeight()+15);

                }
                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);

                //gMap.clear();





                asyncTask_reverse_geo[0] = new ReverseGeocoding().execute(mMap.getCameraPosition().target);



                mCurrentLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(mMap.getCameraPosition().target).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));

                marker_img.setVisibility(View.INVISIBLE);


            }
        });




        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                //System.out.println(asyncTask_reverse_geo[0].getStatus());

                if (asyncTask_reverse_geo[0] != null){
                    asyncTask_reverse_geo[0].cancel(true);
                }

                //asyncTask_reverse_geo[0].cancel(true);

                cardView_casa.setCardBackgroundColor(color_white);//posible crash
                img_casa.setImageResource(R.drawable.img_casa_negativoxxxhdpi);
                casa_word.setTextColor(color_caminandog);

                cardView_ultimo.setCardBackgroundColor(color_white);
                img_ultimo.setImageResource(R.drawable.img_ultimo_negativoxxxhdpi);
                ultimo_word.setTextColor(color_caminandog);

                text_int.setText("");

                if (map_click) {
                    mMap.setPadding(0, 0, 0, linearLayout_inf.getHeight() + 15);
                    marker_img.setPadding(0, 0, 0, 330);
                    marker_img.setVisibility(View.VISIBLE);
                } else {
                    mMap.setPadding(0, cardView_sup.getHeight() - 15, 0, linearLayout_inf.getHeight() + 15);
                    marker_img.setVisibility(View.VISIBLE);
                }
                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);
                mMap.clear();


            }
        });




        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (map_click) {
                    map_click = false;
                    mMap.setPadding(0, cardView_sup.getHeight() - 15, 0, linearLayout_inf.getHeight() + 15);

                } else {
                    map_click = true;
                    mMap.setPadding(0, 0, 0, linearLayout_inf.getHeight() + 15);
                    mMap.clear();
                    mCurrentLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(mMap.getCameraPosition().target).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                }

                if (lin_direc.getVisibility() == View.VISIBLE) {
                    lin_direc.setVisibility(View.INVISIBLE);
                    lin_direc.startAnimation(slideDown);

                } else {
                    lin_direc.setVisibility(View.VISIBLE);
                    lin_direc.startAnimation(slideUp);

                }


            }
        });





        Button btnExtraerDireccion = (Button) v.findViewById(R.id.btnEnviarDireccion);

        btnExtraerDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prog_start.setVisibility(View.VISIBLE);
                btnExtraerDireccion.setClickable(false);

                final DatabaseReference check_email = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid());

                if (text_int.getText().toString().contains(";")){
                    text_int.setError("Por favor elimina todos los ; de esta casilla");
                    prog_start.setVisibility(View.INVISIBLE);
                    btnExtraerDireccion.setClickable(true);

                    text_int.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(text_int, InputMethodManager.SHOW_IMPLICIT);
                }else if (text_int.getText().toString().trim().equals("")
                        ||text_int.getText().toString().contains(";")){



                    text_int.setError("Por favor ingresa el numero exterior y/o interior del domicilio, de no existir coloca SN");
                    prog_start.setVisibility(View.INVISIBLE);
                    btnExtraerDireccion.setClickable(true);

                    text_int.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(text_int, InputMethodManager.SHOW_IMPLICIT);




                }else if (txt_places.getText().toString().equals("")||txt_places.getText().toString().equals(" ")){
                    txt_places.setError("Por favor ingresa la calle o el nombre del lugar");
                    prog_start.setVisibility(View.INVISIBLE);
                    btnExtraerDireccion.setClickable(true);

                    txt_places.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_places, InputMethodManager.SHOW_IMPLICIT);

                }else {

                    //Toast.makeText( getContext (),text_int.getText()+" "+txt_places.getText(),Toast.LENGTH_LONG).show();

                    Intent intent = new Intent( Inicio_Fragment.this.getActivity (),Configuracion_Paseo.class);
                    intent.putExtra( "latitud_inicio",mCurrentLocationMarker.getPosition().latitude);
                    intent.putExtra("longitud_inicio",mCurrentLocationMarker.getPosition().longitude);
                    intent.putExtra("paquete",false);
                    intent.putExtra("direccion_direccion",txt_places.getText()+";"+text_int.getText());
                    intent.putExtra("dias",1);
                    //intent.putExtra("fecha",0);
                    btnExtraerDireccion.setClickable(true);

                    prog_start.setVisibility(View.INVISIBLE);
                    startActivity(intent);


                }


            }
        });

        //

        GetLocation();

    }




    public void agregar_casa(){
        mBuildercasa = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        mView = getLayoutInflater().inflate(R.layout.dialog_add_casa, null);
        mBuildercasa.setView(mView);
        mBuildercasa.setCancelable(false);
        dialogcasa = mBuildercasa.create();
        dialogcasa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //dialog.setCancelable(false);
        dialogcasa.show();

        txt_places_casa = (TextView) mView.findViewById(R.id.txt_direc1_princ_casa);
        Button cancel = (Button)mView.findViewById(R.id.btn_cancel_add_casa);
        Button save = (Button)mView.findViewById(R.id.btn_send_casa);
        TextView interior_txt = (TextView) mView.findViewById(R.id.int_text_casa);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setClickable(false);

                if (interior_txt.getText().toString().contains(";")){
                    interior_txt.setError("Por favor elimina todos los ; de esta casilla");
                    //prog_start.setVisibility(View.INVISIBLE);
                    save.setClickable(true);

                    interior_txt.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(interior_txt, InputMethodManager.SHOW_IMPLICIT);
                }else if (interior_txt.getText().toString().equals("")
                        ||txt_places.getText().toString().equals("")){

                    interior_txt.setError("Por favor ingresa el numero exterior y/o interior del domicilio, de no existir coloca SN");

                    save.setClickable(true);


                    interior_txt.requestFocus();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    if (lin_direc.getVisibility()==View.INVISIBLE){
                        lin_direc.setVisibility(View.VISIBLE);
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(interior_txt, InputMethodManager.SHOW_IMPLICIT);




                }else {



                   comote_ref.child("direccion").setValue(txt_places_casa   .getText().toString()+";"+interior_txt.getText().toString());
                   comote_ref.child("latitud").setValue(gMap_casa.getCameraPosition().target.latitude);
                   comote_ref.child("longitud").setValue(gMap_casa.getCameraPosition().target.longitude);

                   dialogcasa.dismiss();



                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcasa.dismiss();
            }
        });

        ImageView marker_img_casa = (ImageView)mView.findViewById(R.id.imageView2_markey_casa);
        marker_img_casa.setVisibility(View.INVISIBLE);

        final AsyncTask[] asyncTask_reverse_geo_casa = new AsyncTask[1];



        mapView_casa = mView.findViewById( R.id.mapaView_casa);
        mapView_casa.onCreate(ss);
        mapView_casa.onResume();
        mapView_casa.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap_casa = googleMap;
                Places.initialize(getApplicationContext(), "AIzaSyAzVCOxZQYQuMrA6lfgJC8hOM0zyYcasdY");

                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyAzVCOxZQYQuMrA6lfgJC8hOM0zyYcasdY");
                }


                gMap_casa.clear();
                mCurrentLocationMarker_casa = gMap_casa.addMarker(new MarkerOptions ()
                        .position(mMap.getCameraPosition().target).draggable(false).icon( BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                gMap_casa.getUiSettings().setZoomControlsEnabled(true);
                gMap_casa.moveCamera( CameraUpdateFactory.newLatLngZoom( mMap.getCameraPosition().target,18.0f));

                gMap_casa.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {







                    }
                });

                gMap_casa.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {


                        asyncTask_reverse_geo_casa[0].cancel(true);
                        gMap_casa.clear();
                        marker_img_casa.setVisibility(View.VISIBLE);


                    }
                });


                gMap_casa.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {



                        gMap_casa.clear();




                        mCurrentLocationMarker_casa = gMap_casa.addMarker(new MarkerOptions ()
                                .position(gMap_casa.getCameraPosition().target).draggable(false).icon( BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));

                        marker_img_casa.setVisibility(View.INVISIBLE);





                        asyncTask_reverse_geo_casa[0] = new ReverseGeocoding2().execute(gMap_casa.getCameraPosition().target);








                    }
                });

                gMap_casa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {



                    }
                });


                txt_places_casa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Autocomplete.IntentBuilder(
                                AutocompleteActivityMode.FULLSCREEN, fields)
                                .setCountry("MX")
                                .build(getActivity());
                        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE2);

                    }
                });



            }
        });
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach ( context );
        if (context instanceof OnFragmentInteractionListener) {

        } else {
            throw new RuntimeException ( context.toString ()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach ();

    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onPause() {
        super.onPause();
        //locationManager.removeUpdates(locationListener);
        if( locationCallback != null){
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        //locationManager.removeUpdates(locationListener);
        if( locationCallback != null){
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult( requestCode,permissions,grantResults,this);
    }*/





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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());


                mMap.clear();
                txt_places.setError(null);
                txt_places.setText(" "+place.getAddress());


                LatLng latLng = place.getLatLng();
                mCurrentLocationMarker = mMap.addMarker(new MarkerOptions ()
                        .position(mMap.getCameraPosition().target).draggable(false).icon( BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng,18.0f));

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else if(requestCode == AUTOCOMPLETE_REQUEST_CODE2){
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());


                gMap_casa.clear();
                txt_places_casa.setText(" "+place.getAddress());


                LatLng latLng = place.getLatLng();
                mCurrentLocationMarker = gMap_casa.addMarker(new MarkerOptions ()
                        .position(gMap_casa.getCameraPosition().target).draggable(false).icon( BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                gMap_casa.getUiSettings().setZoomControlsEnabled(true);
                gMap_casa.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng,18.0f));


                //btn_casa.setText(place.getAddress());




                //dialog.setCanceledOnTouchOutside(false);





            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    private class ReverseGeocoding extends AsyncTask<LatLng, Integer, Long> {
        protected Long doInBackground(LatLng... latlngs) {


            int count = latlngs.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {

                try {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                addresses = geocoder.getFromLocation(latlngs [0].latitude,latlngs [0].longitude, 1);
                                if (addresses.size() > 0){
                                    txt_places.setText(" "+addresses.get(0).getAddressLine(0));
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Stuff that updates the UI

                        }
                    });

                }catch (Exception e){

                }






                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {

            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

    private class ReverseGeocoding2 extends AsyncTask<LatLng, Integer, Long> {
        protected Long doInBackground(LatLng... latlngs) {


            int count = latlngs.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {

                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                addresses = geocoder.getFromLocation(latlngs [0].latitude,latlngs [0].longitude, 1);
                                if (addresses.size() > 0){
                                    txt_places.setText(" "+addresses.get(0).getAddressLine(0));
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Stuff that updates the UI

                        }
                    });

                }catch (Exception e){

                }






                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {

            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String result = data.toURI();
            capturedImageUri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                selectedImagePath = getRealPathFromURIPath(capturedImageUri, MainActivity.this);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capturedImageUri);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 400, true);
                    photoImage.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera), CAMERA_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (Utilidades.rotacion == 0){
            appBar.removeView(pestanas);
        }*/

    }

    public class MovieAdapter extends ArrayAdapter<Elemento_Agenda> {

        private Context mContext;
        private List<Elemento_Agenda> moviesList = new ArrayList<>();

        public MovieAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Elemento_Agenda> list) {
            super(context, 0 , list);
            mContext = context;
            moviesList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.elemento_dia_agenda,parent,false);

            Elemento_Agenda currentMovie = moviesList.get(position);


            TextView name = (TextView) listItem.findViewById(R.id.textView_name);
            name.setText(currentMovie.getmName());



            return listItem;
        }
    }

    public static class PetsViewHolderPaseos extends RecyclerView.ViewHolder{

        View mView;
        public PetsViewHolderPaseos(View itemViewx){
            super(itemViewx);
            mView = itemViewx;
        }


        public void setNombre (String nombre){
            TextView id_ = (TextView)mView.findViewById(R.id.chatlistNombre);
            id_.setText(nombre);
        }
        public void setTexto (String nombre){
            TextView id_ = (TextView)mView.findViewById(R.id.chatlistUltimoM);
            id_.setText(nombre);
        }
        public void setFecha (String nombre){
            TextView id_ = (TextView)mView.findViewById(R.id.chatlistFecha);
            id_.setText(nombre);
        }
        public void setFechaTitulo (String nombre){
            TextView id_ = (TextView)mView.findViewById(R.id.chatlistFechaTitulo);
            id_.setText(nombre);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.chatlistImgPas);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
        public void setImageMsg(boolean x){
            ImageView post_image = (ImageView) mView.findViewById(R.id.chatlistImgMsg);
            TextView id_ = (TextView)mView.findViewById(R.id.chatlistFecha);
            if(x){
                post_image.setVisibility(View.VISIBLE);

            }else {
                post_image.setVisibility(View.GONE);
                id_.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gris_caminandog));

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







    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetLocation();
                } else {
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
                    subtitlemsj.setText("Recuerda que para tener una precisión del lugar donde requieres tu(s) paseo(s) o utilizar las funciones de tu placa de Recuperandog es necesario que compartas con CaminanDog tu ubicación");
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

    public void GetLocation(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
            System.out.println("obtener ubicacion");
            mMap.setMyLocationEnabled(true);

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }else{
                        final DatabaseReference ubicRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.USER_REFERERENCE).child(user.getUid());

                        Ubic track2 = new Ubic(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude() );
                        ubicRef.child("ubicacion").setValue( track2 );
                        System.out.println("location "+locationResult.getLastLocation().getLatitude()+","+locationResult.getLastLocation().getLongitude());
                        centreMapOnLocation(locationResult.getLastLocation());
                        if (fusedLocationClient != null) {
                            //fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            };

            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    centreMapOnLocation(location);
                    final DatabaseReference ubicRef = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.USER_REFERERENCE).child(user.getUid());

                    Ubic track2 = new Ubic(location.getLatitude(), location.getLongitude() );
                    ubicRef.child("ubicacion").setValue( track2 );

                } else {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            });







            /*fusedLocationClient.getCurrentLocation(1000,ct).addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                System.out.println("fusedlocation "+location.getLatitude()+","+location.getLongitude());
                                centreMapOnLocation(location,"Your Location");
                            }
                        }
                    });*/

            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener, null);
            //locationManager.requestSingleUpdate( LocationManager.NETWORK_PROVIDER, locationListener, null );

            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,100,locationListener);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000,100,locationListener);

            //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //centreMapOnLocation(lastKnownLocation,"Your Location");
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
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
            subtitlemsj.setText("Recuerda que para tener una precisión del lugar donde requieres tu(s) paseo(s) o utilizar las funciones de tu placa de Recuperandog es necesario que compartas con CaminanDog tu ubicación");
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
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public boolean GetPermisionCall(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {

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
            subtitlemsj.setText("Recuerda que para tener comunicación con tu paseador deberas aceptar el permiso.");
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



            return false;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CALL_PHONE);
            return false;
        }

    }







    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("before switch");

    }*/





}


