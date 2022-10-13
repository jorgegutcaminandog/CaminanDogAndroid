package mx.com.caminandog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import okhttp3.ResponseBody;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.GraphRequest.TAG;

public class Maps_Solic_Pas extends FragmentActivity implements OnMapReadyCallback {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ProgressBar progressBar;

    String order_id_str;

    private FusedLocationProviderClient mFusedLocationClient;

    DatabaseReference mDatabase;
    private Button mbtnMaps;

    int periodo_solicitud = 30000;
    double radio_busqueda = .6;


    int index = 0;

    private GoogleMap mMap;
    Marker myCurrent;

    SupportMapFragment mapFragment;
    private CameraUpdate mCameraUpdate;

    //variables del marcador
    String userData;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UNO");
    GeoFire geofire = new GeoFire(ref);

    DatabaseReference Pasref = FirebaseDatabase.getInstance().getReference("Paseadores");
    DatabaseReference Solref = FirebaseDatabase.getInstance().getReference(FirebaseReferences.SOLICITUDES_REFERENCE);


    String ultimaLlave = "";
    Boolean pressButton = false;

    Timer timer = new Timer();

    String modalidad;
    int cantidadPerros;
    String onlyKey;

    ImageView carga_paseo;

    DatabaseReference observadorConf;


    public static class Datos {
        private Float distancia;
        private String key;

        public Datos() {
        }


        public Datos(String key, float distancia) {
            this.distancia = distancia;
            this.key = key;


        }

        public Float getDistancia() {
            return distancia;
        }

        public void setDistancia(Float distancia) {
            this.distancia = distancia;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    List<Datos> ejemploLista = new ArrayList<Datos>();
    final List<Datos> Lista2 = new ArrayList<Datos>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    private ChildEventListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__solic__pas);
        Toast.makeText(getApplicationContext(),
                (getResources().getString(R.string.Buscandoo)),
                Toast.LENGTH_SHORT).show();

        //carga_paseo = (ImageView)findViewById(R.id.);


        SharedPreferences paymnt_json = getSharedPreferences("PREFS", 0);
        final SharedPreferences.Editor editor = paymnt_json.edit();
        String json = paymnt_json.getString("jsonPago", "");


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Coordenada();


        //Mover ();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                progressBar.setVisibility(View.VISIBLE);
                //startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                finish();

            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                if (Lista2.isEmpty()) {

                    Intent iopi = getIntent();
                    String direccion = iopi.getStringExtra("direccion_direccion");
                    Double latitud = iopi.getExtras().getDouble("latitud_pago");
                    Double longitud = iopi.getExtras().getDouble("longitud_pago");
                    String names_dogs = iopi.getStringExtra("names_dogs");


                    Solref.child(user.getUid()).child("latitud").setValue(latitud);
                    Solref.child(user.getUid()).child("longitud").setValue(longitud);
                    Solref.child(user.getUid()).child("time").setValue(ServerValue.TIMESTAMP);
                    Solref.child(user.getUid()).child("direccion").setValue(direccion);
                    Solref.child(user.getUid()).child("uid").setValue(user.getUid());
                    Solref.child(user.getUid()).child("categoria").setValue(modalidad);
                    Solref.child(user.getUid()).child("num_perros").setValue(cantidadPerros);
                    double x = iopi.getExtras().getDouble("tiempo_paseo");
                    if (x == 1.0 || x == 2.0) {
                        Solref.child(user.getUid()).child("tiempo_paseo").setValue(x);
                    } else {
                        Solref.child(user.getUid()).child("tiempo_paseo").setValue("30 min");
                    }


                    //30 min
                    Solref.child(user.getUid()).child("existencia_paseadores").setValue("No");
                    Solref.child(user.getUid()).child("perrosNombre").setValue(names_dogs);

                    //
                    if (FirebaseReferences.NOTIFICATIONS_CONTROL) {
                        final JSONObject jsonObj_not = new JSONObject();
                        try {
                            jsonObj_not.put("uid", user.getUid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OkHttpClient httpClient = new OkHttpClient();
                        HttpUrl.Builder httpBuider =
                                HttpUrl.parse(Conecta_Caminandog.SOLIC_FUNC).newBuilder();
                        httpBuider.addQueryParameter("text", "" + jsonObj_not);
                        final Request req = new Request.Builder().
                                url(httpBuider.build()).build();
                        httpClient.newCall(req).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e(TAG, "error in getting response from firebase cloud function");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Cound’t get response from cloud function",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                            }

                            private void runOnUiThread(Runnable runnable) {


                            }

                        });
                    } else {
                        System.out.println("notifications disabled");
                    }


                    //
                    try {
                        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;
                        Solref.child(user.getUid()).child("through").setValue("Android " + version);


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        Solref.child(user.getUid()).child("through").setValue("Android // no se pudo leer version");
                    }


                    runOnUiThread(new Runnable() {
                        public void run() {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Maps_Solic_Pas.this);

                            builder.setTitle((getResources().getString(R.string.Buscar_paseador)));
                            builder.setMessage((getResources().getString(R.string.Intenta_mas_tarde_por_favor)));
                            builder.setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Buscar();
                                    startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                    finish();
                                }
                            });

                            builder.show();
                            timer.cancel();


                            System.out.println((getResources().getString(R.string.No_hay_paseadores)));
                        }
                    });

                } else {
                    pressButton = true;

                    //ejemploLista.sort((s1, s2) -> Math.round ( s1.distancia ) - Math.round ( s2.distancia ));
                    ejemploLista = Lista2;


                    Collections.sort(ejemploLista, (s1, s2) -> Math.round(s1.distancia) - Math.round(s2.distancia));

                    //ejemploLista.get( 0 );

                    for (int i = 0; i <= ejemploLista.size() - 1; i++) {
                        System.out.println("antes de metodo" + ejemploLista.get(i).key);

                    }

                    timer.scheduleAtFixedRate(new TimerTask() {

                                                  @Override
                                                  public void run() {
                                                      System.out.println("timer delay 5000   period 20000");
                                                      if (Lista2.isEmpty()) {

                                                          //Toast.makeText(getApplicationContext(), "No hay paseaores", Toast.LENGTH_LONG).show();

                                                      } else {
                                                          Buscar();
                                                      }


                                                  }
                                              },
                            0, periodo_solicitud);
                }

            }
        }, 15000);


    }

    public void Buscar() {


        int tamañodelarray = ejemploLista.size();

        System.out.println("tamaño del array: " + tamañodelarray);
        System.out.println("ultimallave: " + ultimaLlave + " Index: " + index);


        if (ultimaLlave.isEmpty()) {

            System.out.println((getResources().getString(R.string.Vacio_no_cambia_solicitud_ni_estatus)));
        } else {
            System.out.println((getResources().getString(R.string.Cambia_solicitud_y_estatus)));
            int solicitud = 0;
            int estatus = 0;
            Pasref.child(ultimaLlave).child("estatus").setValue(estatus);
            Pasref.child(ultimaLlave).child("solicitud").child("solicitud").setValue(solicitud);

        }


        try {
            ejemploLista.get(index);
            onlyKey = ejemploLista.get(index).key;


        } catch (Exception e) {
            System.out.println((getResources().getString(R.string.Arreglo_vacio)));
        }


        if (index == tamañodelarray) {
            pressButton = false;
            ultimaLlave = "";
            timer.cancel();
            //Coordenada ();

            Intent iopi = getIntent();
            String direccion = iopi.getStringExtra("direccion_direccion");
            Double latitud = iopi.getExtras().getDouble("latitud_pago");
            Double longitud = iopi.getExtras().getDouble("longitud_pago");
            String names_dogs = iopi.getStringExtra("names_dogs");

            Solref.child(user.getUid()).child("latitud").setValue(latitud);
            Solref.child(user.getUid()).child("longitud").setValue(longitud);
            Solref.child(user.getUid()).child("time").setValue(ServerValue.TIMESTAMP);
            Solref.child(user.getUid()).child("direccion").setValue(direccion);
            Solref.child(user.getUid()).child("uid").setValue(user.getUid());
            Solref.child(user.getUid()).child("categoria").setValue(modalidad);
            Solref.child(user.getUid()).child("num_perros").setValue(cantidadPerros);
            double x = iopi.getExtras().getDouble("tiempo_paseo");
            if (x == 1.0 || x == 2.0) {
                Solref.child(user.getUid()).child("tiempo_paseo").setValue(x);
            } else {
                Solref.child(user.getUid()).child("tiempo_paseo").setValue("30 min");
            }
            Solref.child(user.getUid()).child("existencia_paseadores").setValue("Si");
            Solref.child(user.getUid()).child("perrosNombre").setValue(names_dogs);

            //
            if (FirebaseReferences.NOTIFICATIONS_CONTROL) {
                final JSONObject jsonObj_not = new JSONObject();
                try {
                    jsonObj_not.put("uid", user.getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient httpClient = new OkHttpClient();
                HttpUrl.Builder httpBuider =
                        HttpUrl.parse(Conecta_Caminandog.SOLIC_FUNC).newBuilder();
                httpBuider.addQueryParameter("text", "" + jsonObj_not);
                final Request req = new Request.Builder().
                        url(httpBuider.build()).build();
                httpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "error in getting response from firebase cloud function");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Cound’t get response from cloud function",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                    }

                    private void runOnUiThread(Runnable runnable) {


                    }

                });
            } else {
                System.out.println("notifications disabled");
            }


            //
            try {
                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                Solref.child(user.getUid()).child("through").setValue("Android " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Solref.child(user.getUid()).child("through").setValue("Android // no se pudo leer version");
            }

            Maps_Solic_Pas.this.runOnUiThread(new Runnable() {
                public void run() {
                    observadorConf.removeEventListener(mListener);
                    timer.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Maps_Solic_Pas.this);
                    builder.setCancelable(false);
                    builder.setTitle((getResources().getString(R.string.Buscar_paseador)));
                    builder.setMessage((getResources().getString(R.string.No_pudimos_contactar_con_un_paseador_adecuado)));
                    builder.setMessage((getResources().getString(R.string.Quieres_repetir_la_busqueda)));
                    builder.setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                            //startActivity(new Intent(Maps_Solic_Pas.this, Maps_Solic_Pas.class));
                            //finish();

                        }
                    });
                    builder.setNegativeButton((getResources().getString(R.string.Cancelar)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                            finish();
                            //fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).addToBackStack(null).commit();
                            //getSupportActionBar().setTitle("Mi Cuenta");


                        }
                    });
                    builder.show();
                }
            });


            /*runOnUiThread(new Runnable() {
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder ( Maps_Solic_Pas.this );
                    builder.setTitle ( "No hay Paseadores " );
                    builder.setMessage ( "Intenta mas Tarde Porfavor" );
                    builder.setPositiveButton ( "Aceptar" ,new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog ,int which) {
                            startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                            finish ();
                        }
                    } );
                    builder.show ();
                    timer.cancel ();

                    System.out.println ("No hay Paseadores");
                }
            });*/


        } else {

            Intent iopi = getIntent();
            String orderid_intent = iopi.getStringExtra("order_id_pago");
            System.out.println(orderid_intent);
            String perrosintent = iopi.getStringExtra("perros_pago");
            String direccion = iopi.getStringExtra("direccion_direccion");
            Double latitud = iopi.getExtras().getDouble("latitud_pago");
            Double longitud = iopi.getExtras().getDouble("longitud_pago");


            System.out.println("arreglo dentro de buscar: " + ejemploLista.get(index).key);
            System.out.println("turno index " + index);

            DatabaseReference Pasref = database.getReference(FirebaseReferences.PASEADOR_REFERENCE);
            Query queryy = Pasref.orderByChild("idPaseador").equalTo(onlyKey);


            queryy.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //MEtodo de Obtencion de datos
                        Paseador paseador = snapshot.getValue(Paseador.class);


                        int estatus = paseador.getEstatus();
                        final String categoria = paseador.getCategoria();

                        final int cantidadPerrosFB = paseador.getCantidadPerros();


                        if (modalidad.equals(categoria) || (categoria.equals(""))) {

                            int totalPerros = cantidadPerros + cantidadPerrosFB;
                            if (totalPerros <= 4) {


                                if (estatus == 0) {


                                    //ref = Pasref.getDatabase ().getReference ();
                                    //Pasref.child ( "Paseadores" ).child ( ejemploLista.get ( 0 ).key );
                                    //Pasref = ref.child ( "Paseadores" ).child ( ejemploLista.get ( 0 ).key ).child ( "solicitud" );

                                    //solicitud = 1;
                                    estatus = 1;

                                    try {

                                        //onlyKey="TzsgVsAHV2NQklFplsvVZHGWbA63";

                                        mDatabase.child(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("confirmar").child("confirmar").setValue(0)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        final DatabaseReference Paseadores_ref2 = database.getReference(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("solicitud").child("solicitud");
                                                        Paseadores_ref2.setValue(1);
                                                        System.out.println("" + Paseadores_ref2);

                                                        final DatabaseReference Paseadores_ref3 = database.getReference(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("estatus");
                                                        Paseadores_ref3.setValue(1);
                                                        System.out.println("" + Paseadores_ref3);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Write failed
                                                        // ...
                                                    }
                                                });


                                    } catch (Exception e) {
                                        System.out.println((getResources().getString(R.string.No_cambia_solicitud_y_estatus)) + e);
                                    }

                                    System.out.println( (getResources().getString(R.string.Paseador_actual_con_el_que_se_trabaja))+ onlyKey);

                                    try {


                                        //mListener = mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener()

                                        observadorConf = database.getReference(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("confirmar");
                                        mListener = observadorConf.addChildEventListener(new ChildEventListener() {


                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                observadorConf.removeEventListener(mListener);
                                                System.out.println((getResources().getString(R.string.Aqui)));
                                                System.out.println(dataSnapshot);

                                                final DatabaseReference Conf = database.getReference(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("confirmar");
                                                Query query = Conf.orderByChild("confirmar");


                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Confirmar confirmar = dataSnapshot.getValue(Confirmar.class);
                                                        System.out.println("Aqui opo" + confirmar.getConfirmar());

                                                        if (confirmar.getConfirmar() == 1) {

                                                            Toast.makeText(Maps_Solic_Pas.this, (getResources().getString(R.string.Procesando_pago)), Toast.LENGTH_LONG).show();


                                                            timer.cancel();

                                                            final DatabaseReference Paseadores_ref5 = database.getReference(FirebaseReferences.PASEADOR_REFERENCE).child(onlyKey).child("confirmar").child("confirmar");
                                                            Paseadores_ref5.setValue(0);

                                                            try {

                                                                final JSONObject jsonObj = new JSONObject();
                                                                try {


                                                                    cantidadPerros = iopi.getExtras().getInt("numPerros_pago");
                                                                    modalidad = iopi.getStringExtra("modalidad_pago");


                                                                    jsonObj.put("uid", user.getUid());
                                                                    jsonObj.put("amount", iopi.getExtras().getDouble("amount"));
                                                                    jsonObj.put("id_card", iopi.getStringExtra("id_card"));
                                                                    jsonObj.put("customer_id", iopi.getStringExtra("customer_id"));
                                                                    jsonObj.put("calificacion", 5.0);
                                                                    jsonObj.put("categoria", iopi.getStringExtra("modalidad_pago"));
                                                                    jsonObj.put("id_paseador", onlyKey);
                                                                    jsonObj.put("numero_perros", iopi.getExtras().getInt("numero_perros"));
                                                                    jsonObj.put("perros", iopi.getStringExtra("perros"));
                                                                    jsonObj.put("tiempo_paseo", iopi.getExtras().getDouble("tiempo_paseo"));
                                                                    jsonObj.put("latitud", iopi.getExtras().getDouble("latitud_pago"));
                                                                    jsonObj.put("longitud", iopi.getExtras().getDouble("longitud_pago"));
                                                                    jsonObj.put("direccion", iopi.getStringExtra("direccion_direccion"));
                                                                    jsonObj.put("monto_paseador", iopi.getExtras().getDouble("monto_paseador"));
                                                                    jsonObj.put("perrosNombre", iopi.getStringExtra("names_dogs"));
                                                                    jsonObj.put("descuento_exist", iopi.getStringExtra("descuento_exist"));


                                                                } catch (JSONException e) {
                                                                    Toast.makeText(Maps_Solic_Pas.this, "Error", Toast.LENGTH_LONG).show();
                                                                    e.printStackTrace();
                                                                }

                                                                System.out.println("" + jsonObj);


                                                                OkHttpClient httpClient = new OkHttpClient();
                                                                HttpUrl.Builder httpBuider =
                                                                        HttpUrl.parse(Conecta_Caminandog.ORDER_FUNC).newBuilder();
                                                                httpBuider.addQueryParameter("text", "" + jsonObj);
                                                                final Request req = new Request.Builder().
                                                                        url(httpBuider.build()).build();
                                                                httpClient.newCall(req).enqueue(new Callback() {
                                                                    @Override
                                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                                        Log.e(TAG, "error in getting response from firebase cloud function");
                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(getApplicationContext(),
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

                                                                            Maps_Solic_Pas.this.runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {

                                                                                    Log.e(TAG, "fail response from firebase cloud function");
                                                                                    Toast.makeText(getApplication(),
                                                                                            (getResources().getString(R.string.El_servidor_rechazo_la_conexión)),
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    finish();

                                                                                }
                                                                            });


                                                                        } else {
                                                                            try {
                                                                                resp = responseBody.string();
                                                                                System.out.println(resp);

                                                                                if (resp.contains("error")) {

                                                                                    String[] res = resp.split(",");
                                                                                    String resx = res[0];
                                                                                    String res_message = res[1];

                                                                                    Maps_Solic_Pas.this.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                            System.out.println((getResources().getString(R.string.error_funcion_no_se_proceso_el_pago)));
                                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Maps_Solic_Pas.this);
                                                                                            builder.setCancelable(false);
                                                                                            builder.setTitle((getResources().getString(R.string.Pago_no_realizado)));
                                                                                            builder.setMessage(res_message);
                                                                                            builder.setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                    //progressBar.setVisibility( View.INVISIBLE );
                                                                                                    //startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                                                                                    finish();

                                                                                                }
                                                                                            });
                                                                                            builder.setNegativeButton((getResources().getString(R.string.Cancelar)), new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                                    startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                                                                                    finish();
                                                                                                    //fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).addToBackStack(null).commit();
                                                                                                    //getSupportActionBar().setTitle("Mi Cuenta");


                                                                                                }
                                                                                            });

                                                                                            builder.show();
                                                                                        }
                                                                                    });


                                                                                } else {

                                                                                    String[] respuesta = resp.split(",");
                                                                                    String mensaje = respuesta[0];
                                                                                    String ord_id_func = respuesta[1];
                                                                                    String fotopas = respuesta[2];
                                                                                    String nombrepas = respuesta[3];
                                                                                    String numpas = respuesta[4];

                                                                                    Maps_Solic_Pas.this.runOnUiThread(new Runnable() {
                                                                                        public void run() {

                                                                                            Toast.makeText(getApplicationContext(),
                                                                                                    (getResources().getString(R.string.Pago_procesado_correctamente)),
                                                                                                    Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });

                                                                                    Intent intento = new Intent(Maps_Solic_Pas.this, MainActivity.class);
                                                                                    intento.putExtra("EXTRA", "openFragment");
                                                                                    intento.putExtra("order_id", ord_id_func);
                                                                                    intento.putExtra("fotopaseador", fotopas);
                                                                                    intento.putExtra("nombrepaseador", nombrepas);
                                                                                    startActivity(intento);
                                                                                    finish();


                                                                                }


                                                                            } catch (IOException e) {
                                                                                resp = "Problem in getting payment info";
                                                                                Log.e(TAG, "Problem in reading response " + e);
                                                                            }
                                                                        }
                                                                        runOnUiThread(responseRunnable(resp));


                                                                    }

                                                                    private void runOnUiThread(Runnable runnable) {


                                                                    }

                                                                });


                                                                //query1.removeEventListener(  );


                                                            } catch (Exception e) {
                                                                Toast.makeText(Maps_Solic_Pas.this, (getResources().getString(R.string.Ocurrio_un_error_al_procesar_el_pago)) + e, Toast.LENGTH_LONG).show();
                                                            }


                                                        } else {
                                                            System.out.println("confirmar no igual a 1");
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                                //String keyy = dataSnapshot.getKey ();
                                                //System.out.println ( keyy );
                                                // System.out.println ( "cambiado " + keyy );


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
                                        observadorConf.removeEventListener(this);


                                    } catch (Exception e) {
                                    }
                                }

                            } else {
                                System.out.println((getResources().getString(R.string.Tiene_mas_perros)) + onlyKey);
                            }


                        } else {
                            System.out.println((getResources().getString(R.string.La_categoria_no_coincide)) + onlyKey);


                        }


                    }
                    ultimaLlave = onlyKey;
                    index = index + 1;
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });//hbjhb

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent iopi = getIntent();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(iopi.getExtras().getDouble("latitud_pago"), iopi.getExtras().getDouble("longitud_pago")), 16.0f));

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);


        mMap.setMapStyle(mapStyleOptions);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng ubic = new LatLng(19.432511, -99.133137);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubic, 10));





        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                androidx.appcompat.app.AlertDialog.Builder mBuilder_x = new androidx.appcompat.app.AlertDialog.Builder(Maps_Solic_Pas.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);//,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
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

                dialog_x.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                txt_info.setText((getResources().getString(R.string.Estamos_contactando_con_un_paseador)));
                txt_perros.setText(iopi.getStringExtra( "names_dogs" ));

                Glide.with(getApplicationContext()).load(R.raw.gif_paseo).apply(RequestOptions.circleCropTransform()).into(carga_paseo);




                dialog_x.show();

            }
        }, 3500);




    }
    public void Coordenada() {

        Intent iopi = getIntent();

        Double latitud = iopi.getExtras().getDouble( "latitud_pago" );
        Double longitud = iopi.getExtras().getDouble( "longitud_pago" );
        System.out.println("longitud pago  "+longitud);
        cantidadPerros = iopi.getExtras().getInt( "numPerros_pago" );
        modalidad = iopi.getStringExtra( "modalidad_pago" );

        GeoQuery geoQuery = geofire.queryAtLocation ( new GeoLocation( latitud ,longitud ) ,radio_busqueda );

        geoQuery.addGeoQueryEventListener ( new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key ,final GeoLocation location) {

                System.out.println ( String.format ( "Key %s entered the search area at [%f,%f]" ,key ,location.latitude ,location.longitude ) );


                final  Query query = Pasref.orderByChild ("idPaseador" ).equalTo ( key ); //Consulta Firebase
                query.addListenerForSingleValueEvent (new ValueEventListener () {

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot snapshot : dataSnapshot.getChildren ()) { //MEtodo de Obtencion de datos
                                final Paseador paseador = snapshot.getValue (Paseador.class);
                                int estatus = paseador.getEstatus ();
                                final  String categoria = paseador.getCategoria ();

                                final int cantidadPerrosFB = paseador.getCantidadPerros ();






                                if ( modalidad.equals ( categoria) || (categoria.equals ("") )){



                                    int totalPerros =  cantidadPerros + cantidadPerrosFB;
                                    if ( totalPerros <= 4) {


                                        if (estatus == 0) { //Marcadores de los paseadores activos
                                            LatLng Ubicacion = new LatLng ( latitud ,longitud );
                                            mMap.addMarker ( new MarkerOptions().position ( Ubicacion ).title ((getResources().getString(R.string.Yo))).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                                            MarkerOptions markerOptions = new MarkerOptions ();
                                            markerOptions.position ( new LatLng ( location.latitude ,location.longitude ) );
                                            markerOptions.title ( key );
                                            markerOptions.getPosition ();
                                            mMap.addMarker ( markerOptions );


                                            Location locationA = new Location ( (getResources().getString(R.string.Punto_A)));

                                            locationA.setLatitude ( Ubicacion.latitude );
                                            locationA.setLongitude ( Ubicacion.longitude );

                                            Location locationB = new Location ( (getResources().getString(R.string.Punto_B)));

                                            locationB.setLatitude ( location.latitude );
                                            locationB.setLongitude ( location.longitude );
                                            float distance = locationA.distanceTo ( locationB );

                                            if (pressButton.equals ( false )){
                                                Lista2.add ( new Datos ( key ,distance ) );
                                            }


                                        }
                                    }
                                }
                            }



                        }
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println( "The read failed: " + databaseError.getCode());

                    }
                } );

            }



            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key ,GeoLocation location) {

            }


            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        } );

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */


    }







    public void Mover() {
        GeoQuery geoQuery = geofire.queryAtLocation ( new GeoLocation ( 19.4994493 ,-99.2350665 ) ,.6 );

        geoQuery.addGeoQueryEventListener ( new GeoQueryEventListener () {
            @Override
            public void onKeyEntered(String key ,GeoLocation location) {

            }

            @Override
            public void onKeyExited(String key) {


            }

            @Override
            public void onKeyMoved(String key ,GeoLocation location) {
                mMap.clear ();

                Coordenada ();


            }


            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        } );


    }

    private Runnable responseRunnable(final String responseStr){
        Runnable resRunnable = new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext()
                        ,responseStr,
                        Toast.LENGTH_SHORT).show();
                System.out.println(responseStr);
            }
        };
        return resRunnable;
    }

    @Override
    protected void onDestroy() {
        //observadorConf.child("confirmar").removeListener(mListener);

        //observadorConf.removeEventListener( mListener );

        super.onDestroy();
        //observadorConf.removeEventListener( mListener );
        if (mListener == null){
            System.out.println("null listener observer");

        }else{
            System.out.println("else destroy");
            database.getReference ( FirebaseReferences.PASEADOR_REFERENCE ).child ( onlyKey ).child ("confirmar" ).removeEventListener( mListener );

        }

        //mDatabase.child("confirmar").removeEventListener( mListener );






    }
}
