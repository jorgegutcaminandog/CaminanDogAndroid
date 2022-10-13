package mx.com.caminandog;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;


public class Configuracion_Paseo extends AppCompatActivity {

    private static final int REQUEST= 112;

    private List<Person> persons;
    private List<Time> times;
    MapView mapView;
    public GoogleMap gMap;
    String direccion;
    Double latitud;
    Double longitud;
    LinearLayout xx;
    LinearLayout xy;
    private RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<Perro, Configuracion_Paseo.PetsViewHolder> mPeopleRVAdapter;
    private ProgressBar progressBar;
    final List<String> selected_time = new ArrayList<>();
    private Spinner spinner;
    int selected;
    Animation slideRight;
    Animation slideLeft;
    String id_cod_descuento;
    String desparasitacion,primeraVac,segundaVac,terceraVac,antipulgas;

    String edad;
    String comportamiento;
    String padecimiento;
    String raza;
    private static final int GALLERY_INTENT = 1;
    private static final int TAKE_PICTURE = 2;
    private static final int MY_SCAN_REQUEST_CODE = 3;
    private StorageReference mstorach;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageButton mUpload;
    private ProgressDialog mprogress;
    String inserturl = "";
    AlertDialog dialog;
    Uri uri;
    byte[] data_pick;
    String currentPhotoPath;

    double tiempo;
    int dias;


    String perro_id = "";
    AlertDialog.Builder mBuilder;

    DatabaseReference personsRef1;

    double media_val = 0.6322;
    double noventa_val = 1.5;


    final List<String> selected_dogs = new ArrayList<>();
    final List<String> names_dogs = new ArrayList<>();

    int num_perros;

    //Button pagar_btn;
    Boolean paquete;

    String categoria;

    TextView first_line_txt;
    TextView first_line_value_txt;
    TextView second_line_txt;
    TextView descuento_value_text;
    TextView subtotal_value_text;
    TextView iva_value_text;
    TextView total_value_text;
    TextView glob_cost_txt;
    TextView cup_num_txt;
    TextView cup_txt;
    TextView cupon_txt;
    TextView desc_word;


    double costoServicio = 0.0;
    double descuentoInterno = 0.0;
    double cuponDescuento = 0.0;
    double subtotalCompra = 0.0;
    double ivaDeCompra = 0.0;
    double totalApagar = 0.0;
    double porcentajeDescuento = 0.0;

    double iva;



    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    androidx.appcompat.app.AlertDialog dialog_add_cod;


    double costounitario;





    double monto_paseador;

    String idsperros;
    String namesperros;
    String customer_id;
    String diasEleccion;


    //double ivatot;
    //double total=0;
    //double totalsiniva=0;

    ImageView carga_paseo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__configuracion_paseo);

        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        builder2.detectFileUriExposure();

        personsRef1 = FirebaseDatabase.getInstance().getReference().child("Perros");


        spinner = (Spinner)findViewById(R.id.spinner_services);

        slideRight = AnimationUtils.loadAnimation(this, R.anim.show_from_right);
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        window.setStatusBarColor(getColor(R.color.white));

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);

        Button pagar_btn = (Button)findViewById(R.id.btn_pagar);











        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.CANCELAR));


        //add card

        final ImageButton scancard = (ImageButton) findViewById(R.id.scan_btn_conf);

        scancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(Configuracion_Paseo.this, CardIOActivity.class);



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
        });


        //add card end






        ImageButton btn_add_dog = (ImageButton)findViewById(R.id.btn_add_dog);
        btn_add_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inserturl="";
                mBuilder = new AlertDialog.Builder(Configuracion_Paseo.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_add_perro, null);
                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //dialog.setCancelable(false);
                dialog.show();

                //dialog.setCanceledOnTouchOutside(false);




                //ImageView avatar_walker = (ImageView) mView.findViewById(R.id.imageView);
                //upload image
                mUpload = (ImageButton) mView.findViewById(R.id.up_foto);
                Button eliminar = (Button) mView.findViewById(R.id.eliminardog_btn);
                Button cancelar = (Button) mView.findViewById(R.id.btn_cancel_add);
                //mUpload.setVisibility(View.GONE);
                mstorach = FirebaseStorage.getInstance().getReference();
                mprogress = new ProgressDialog(Configuracion_Paseo.this);
                eliminar.setVisibility(View.GONE);

                mUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                Configuracion_Paseo.this);
                        myAlertDialog.setTitle(getResources().getString(R.string.Sube_una_foto));
                        myAlertDialog.setMessage(getResources().getString(R.string.Como_quieres_realizar_esta_accion));

                        myAlertDialog.setPositiveButton((getResources().getString(R.string.Galeria)),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        PickPictureActivityResultLauncher.launch(intent);


                                    }
                                });

                        myAlertDialog.setNegativeButton((getResources().getString(R.string.Camara)),
                                new DialogInterface.OnClickListener() {


                                    public void onClick(DialogInterface arg0, int arg1) {

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
                                            String[] PERMISSIONS = {android.Manifest.permission.CAMERA//Manifest.permission.READ_EXTERNAL_STORAGE
                                            };


                                            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                                                Log.d("TAG","@@@ IN IF hasPermissions");
                                                //ActivityCompat.requestPermissions((getActivity()) , PERMISSIONS, REQUEST );
                                                requestPermissions(PERMISSIONS, REQUEST);
                                            } else {
                                                Log.d("TAG","@@@ IN ELSE hasPermissions");
                                                dispatchTakePictureIntent();
                                            }
                                        }else {
                                            dispatchTakePictureIntent();

                                        }



                                    }
                                });
                        myAlertDialog.show();

                        /*Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent,GALLERY_INTENT);

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, TAKE_PICTURE);
                        }*/




                    }
                });


                final Button enviar = (Button) mView.findViewById(R.id.btn_send);


                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        InputMethodManager imm = (InputMethodManager) Configuracion_Paseo.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        TextView name = (TextView) mView.findViewById(R.id.txt_name_adddog);
                        TextView ruac = (TextView) mView.findViewById(R.id.txt_ruac);
                        String name_insert = name.getText().toString();
                        String ruac_insert = ruac.getText().toString();

                        if (!name_insert.equals("")){
                            mprogress.setTitle(getResources().getString(R.string.Espera_un_momento));
                            mprogress.setMessage(getResources().getString(R.string.Subiendo_foto));
                            mprogress.setCancelable(false);
                            mprogress.show();
                            personsRef1 = FirebaseDatabase.getInstance().getReference().child("Perros");


                            perro_id = personsRef1.child(user.getUid()).push().getKey();

                            final Map<String, Object> vacunas = new HashMap<>();
                            vacunas.put("desparacitado",desparasitacion);
                            vacunas.put("primeraDosis",primeraVac);
                            vacunas.put("segundaDosis",segundaVac);
                            vacunas.put("terceraDosis",terceraVac);

                            final Map<String, Object> perro = new HashMap<>();
                            perro.put("uidUsuario", user.getUid());
                            perro.put("comportamiento", comportamiento);
                            perro.put("edad", edad);
                            perro.put("nombre", name.getText().toString());
                            perro.put("ruac", ruac.getText().toString());
                            perro.put("padecimiento", padecimiento);
                            perro.put("raza", raza);
                            perro.put("idPerro", perro_id);
                            perro.put("vacunas", vacunas);
                            perro.put("antipulgas", antipulgas);


                            personsRef1.child(user.getUid()).child(perro_id).updateChildren(perro).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    if (uri==null && data_pick == null){
                                        personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/caminandog-218818.appspot.com/o/perfil%20perro%403x.png?alt=media&token=ee2198f5-f38b-4a40-8857-784b01b06072");
                                        mprogress.dismiss();
                                        dialog.dismiss();

                                    }else{

                                        final StorageReference filepath = mstorach.child("FotosPerros").child(user.getUid()).child(perro_id+".jpeg");
                                        if (uri==null){

                                            UploadTask uploadTask = filepath.putBytes(data_pick);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    System.out.println(getResources().getString(R.string.No_se_pudo_subir_el_archivo));
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                                    mprogress.dismiss();
                                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                    while (!urlTask.isSuccessful());
                                                    Uri downloadUrl = urlTask.getResult();
                                                    final String sdownload_url = String.valueOf(downloadUrl);
                                                    inserturl = sdownload_url;

                                                    personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue(inserturl);
                                                    uri=null;
                                                    data_pick=null;
                                                    dialog.dismiss();
                                                }
                                            });

                                        }else {

                                            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    //personsRef1 = FirebaseDatabase.getInstance().getReference().child("Perros");
                                                    mprogress.dismiss();
                                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                    while (!urlTask.isSuccessful());
                                                    Uri downloadUrl = urlTask.getResult();
                                                    final String sdownload_url = String.valueOf(downloadUrl);
                                                    inserturl = sdownload_url;

                                                    personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue(inserturl);
                                                    uri=null;
                                                    data_pick=null;
                                                    dialog.dismiss();

                                                }
                                            });

                                        }




                                    }

                                }
                            });





                            /*if (inserturl.equals("")){
                                personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/caminandog-218818.appspot.com/o/perfilVacio.png?alt=media&token=2fe9b6d3-83f1-4295-a669-0692cb26d884");
                            }else {
                                personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue(inserturl);
                            }

                            personsRef1.child(user.getUid()).child(perro_id).child("padecimiento").setValue(padecimiento);
                            personsRef1.child(user.getUid()).child(perro_id).child("comportamiento").setValue(comportamiento);
                            personsRef1.child(user.getUid()).child(perro_id).child("edad").setValue(edad);
                            personsRef1.child(user.getUid()).child(perro_id).child("raza").setValue(raza);
                            personsRef1.child(user.getUid()).child(perro_id).child("nombre").setValue(name.getText().toString());

                            inserturl = "";*/



                        }else{
                            name.setError(getResources().getString(R.string.Este_campo_no_puede_estar_vacio));

                        }






                    }
                });











                //


                //Glide.with(MainActivity.this).load(R.drawable.caren).apply(RequestOptions.circleCropTransform()).into(avatar_walker);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dogs_ref = database.getReference(FirebaseReferences.DOGS_REFERERENCE);

                final Spinner spinnerraza = mView.findViewById(R.id.sp_raza);
                final Spinner spinnerpadecimiento = mView.findViewById(R.id.sp_padec);
                final Spinner spinnercomportamiento = mView.findViewById(R.id.sp_comp);
                final Spinner spinneredad = mView.findViewById(R.id.sp_edad);
                final Spinner spinnerdespara = mView.findViewById(R.id.sp_desparasitado);
                final Spinner spinnerantipulgas = mView.findViewById(R.id.sp_antipulgas);
                final Spinner spinner1ra = mView.findViewById(R.id.sp_1radosis);
                final Spinner spinner2da = mView.findViewById(R.id.sp_2dadosis);
                final Spinner spinner3ra = mView.findViewById(R.id.sp_3radosis);

                final ArrayAdapter<CharSequence> adapteredad = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.edad, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapterraza = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.razas, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapterpadec = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.padecimiento, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adaptercomp = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.comportamiento, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adaptersino = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.sino, android.R.layout.simple_spinner_item);

                adapteredad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adapterraza.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adapterpadec.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adaptercomp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adaptersino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinneredad.setAdapter(adapteredad);
                spinneredad.setOnItemSelectedListener(new Configuracion_Paseo.EdadSpinnerClass());

                spinnerraza.setAdapter(adapterraza);
                spinnerraza.setOnItemSelectedListener(new Configuracion_Paseo.RazaSpinnerClass());

                spinnercomportamiento.setAdapter(adaptercomp);
                spinnercomportamiento.setOnItemSelectedListener(new Configuracion_Paseo.ComportamientoSpinnerClass());

                spinnerpadecimiento.setAdapter(adapterpadec);
                spinnerpadecimiento.setOnItemSelectedListener(new Configuracion_Paseo   .PadecimientoSpinnerClass());

                spinnerdespara.setAdapter(adaptersino);
                spinnerdespara.setOnItemSelectedListener(new Configuracion_Paseo.DesparaSpinnerClass());

                spinnerantipulgas.setAdapter(adaptersino);
                spinnerantipulgas.setOnItemSelectedListener(new Configuracion_Paseo.DesparaSpinnerClass());

                spinner1ra.setAdapter(adaptersino);
                spinner1ra.setOnItemSelectedListener(new Configuracion_Paseo.PrimeraSpinnerClass());

                spinner2da.setAdapter(adaptersino);
                spinner2da.setOnItemSelectedListener(new Configuracion_Paseo.SegundaSpinnerClass());

                spinner3ra.setAdapter(adaptersino);
                spinner3ra.setOnItemSelectedListener(new Configuracion_Paseo.TerceraSpinnerClass());



                //Perro perro = new Perro(user.getUid(),comportamiento.getText().toString(),edad.getText().toString(),name.getText().toString(),padecimiento.getText().toString(),raza.getText().toString(),foto.getText().toString());







            }
        });






        Intent iop = getIntent();
        direccion = iop.getStringExtra("direccion_direccion");
        latitud = iop.getExtras( ).getDouble( "latitud_inicio" );
        longitud = iop.getExtras( ).getDouble( "longitud_inicio" );
        paquete = iop.getExtras( ).getBoolean("paquete");
        dias = iop.getExtras().getInt("dias");
        diasEleccion = iop.getStringExtra("diasEleccion");

        if (paquete){
            pagar_btn.setText(getResources().getString(R.string.Pagar_Paquete));
            int imgResource = R.drawable.img_pagar_paquetexxxhdpi;
            pagar_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
        }else{
            pagar_btn.setText(getResources().getString(R.string.Pagar_Paseo));
        }

        mapView = findViewById(R.id.mapaView_prototipo);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                gMap = googleMap;

                gMap.setMapStyle(mapStyleOptions);


                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(Configuracion_Paseo.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setAllGesturesEnabled(false);
                LatLng ubic = new LatLng(latitud,longitud);
                gMap.addMarker(new MarkerOptions().position(ubic).icon( BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubic, 17));
                //gMap.UiSettings.setZoomControlsEnabled(true);
                //gMap.getUiSettings().setAllGesturesEnabled(true);
                //gMap.getUiSettings().setZoomControlsEnabled(true);
                //gMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);


            }
        });

        xx = (LinearLayout) findViewById(R.id.linear1);
        xy = (LinearLayout) findViewById(R.id.linear2);

        xx.setVisibility(View.VISIBLE);
        xy.setVisibility(View.INVISIBLE);

        Button btn_back = (Button)findViewById(R.id.back_btn_conf);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.show_from_left);
                slideLeft = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right);

                xy.setAnimation(slideLeft);
                xy.setVisibility(View.INVISIBLE);
                xx.setAnimation(slideRight);
                xx.setVisibility(View.VISIBLE);

                cup_txt.setText(getResources().getString(R.string.Cupon_Promocional));





            }
        });


        //
        persons = new ArrayList<>();
        persons.add(new Person("basico", R.drawable.img_categoria_basicoxxxhdpi));
        persons.add(new Person("sport", R.drawable.img_categoria_sportxxxhdpi));
        persons.add(new Person("rukys",  R.drawable.img_categoria_rukysxxxhdpi));
        persons.add(new Person("vip",  R.drawable.img_categoria_vipxxxhdpi));//img_166




        RecyclerView rv = (RecyclerView)findViewById(R.id.rec_conf);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(llm);

        //DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        //rv.addItemDecoration(itemDecor);

        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);



        //
        Button media_hora = (Button)findViewById(R.id.btn_media);
        Button una_hora = (Button)findViewById(R.id.btn_uno);
        Button noventa_min = (Button)findViewById(R.id.btn_noventa);
        Button dos_hora = (Button)findViewById(R.id.btn_dos);

        media_hora.setVisibility(View.INVISIBLE);
        una_hora.setVisibility(View.INVISIBLE);
        dos_hora.setVisibility(View.INVISIBLE);
        noventa_min.setVisibility(View.INVISIBLE);

        media_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
        media_hora.setTextColor(getColor(R.color.gris_texto));

        una_hora.setBackground(getDrawable(R.drawable.rounded_button));
        una_hora.setTextColor(getColor(R.color.white));
        tiempo=1;

        dos_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
        dos_hora.setTextColor(getColor(R.color.gris_texto));

        noventa_min.setBackground(getDrawable(R.drawable.rounded_button_grey));
        noventa_min.setTextColor(getColor(R.color.gris_texto));

        una_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                una_hora.setBackground(getDrawable(R.drawable.rounded_button));
                una_hora.setTextColor(getColor(R.color.white));
                tiempo=1;

                dos_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                dos_hora.setTextColor(getColor(R.color.gris_texto));

                media_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                media_hora.setTextColor(getColor(R.color.gris_texto));

                noventa_min.setBackground(getDrawable(R.drawable.rounded_button_grey));
                noventa_min.setTextColor(getColor(R.color.gris_texto));

            }
        });

        dos_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dos_hora.setBackground(getDrawable(R.drawable.rounded_button));
                dos_hora.setTextColor(getColor(R.color.white));
                tiempo=2;

                una_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                una_hora.setTextColor(getColor(R.color.gris_texto));


                media_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                media_hora.setTextColor(getColor(R.color.gris_texto));

                noventa_min.setBackground(getDrawable(R.drawable.rounded_button_grey));
                noventa_min.setTextColor(getColor(R.color.gris_texto));

            }
        });

        media_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_hora.setBackground(getDrawable(R.drawable.rounded_button));
                media_hora.setTextColor(getColor(R.color.white));
                tiempo=media_val;

                una_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                una_hora.setTextColor(getColor(R.color.gris_texto));

                dos_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                dos_hora.setTextColor(getColor(R.color.gris_texto));

                noventa_min.setBackground(getDrawable(R.drawable.rounded_button_grey));
                noventa_min.setTextColor(getColor(R.color.gris_texto));

            }
        });

        noventa_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noventa_min.setBackground(getDrawable(R.drawable.rounded_button));
                noventa_min.setTextColor(getColor(R.color.white));
                tiempo=noventa_val;

                media_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                media_hora.setTextColor(getColor(R.color.gris_texto));

                una_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                una_hora.setTextColor(getColor(R.color.gris_texto));

                dos_hora.setBackground(getDrawable(R.drawable.rounded_button_grey));
                dos_hora.setTextColor(getColor(R.color.gris_texto));



            }
        });

        //  swswsws
        final DatabaseReference halfhourRef = FirebaseDatabase.getInstance().getReference().child("Configuracion");
        Query query_media = halfhourRef;

        query_media.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                una_hora.setVisibility(View.VISIBLE);
                dos_hora.setVisibility(View.VISIBLE);
                noventa_min.setVisibility(View.VISIBLE);

                Configuracion configuracion = dataSnapshot.getValue(Configuracion.class);

                if (configuracion.mediaHora){
                    media_hora.setVisibility(View.VISIBLE);
                    System.out.println("media true");
                }else{
                    media_hora.setVisibility(View.INVISIBLE);
                    System.out.println("media false");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        progressBar = (ProgressBar) findViewById(R.id.progressBar5);

        selected_dogs.clear();
        mPeopleRV = (RecyclerView) findViewById(R.id.recper3);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Perros").child(user.getUid());

        personsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query personsQuery = personsRef.orderByKey().limitToFirst(9);



        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Perro>().setQuery(personsQuery, Perro.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Perro, Configuracion_Paseo.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Configuracion_Paseo.PetsViewHolder holder, final int position, final Perro model) {
                progressBar.setVisibility(View.GONE);
                holder.setTitle(model.getNombre());
                holder.setImage(getApplicationContext(), model.getFoto());//



                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (selected_dogs.contains(model.getIdPerro())){
                            //Toast.makeText(Seleccion_Perros.this, model.getNombre()+" Deseleccionado", Toast.LENGTH_LONG).show();
                            selected_dogs.remove(model.getIdPerro());
                            names_dogs.remove(model.getNombre());
                            holder.setImagenotchecked(getApplicationContext(),"android.resource://" + getPackageName() +"/"+R.drawable.unselected);



                        }else {
                            DatabaseReference perritoref = database.getReference(FirebaseReferences.DOGS_REFERERENCE).child(user.getUid()).child(model.getIdPerro()).child("vacunas");
                            Query queryperr = perritoref;

                            queryperr.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        System.out.println(getResources().getString(R.string.Si_tiene_vacunas));
                                        selected_dogs.add(model.getIdPerro());
                                        names_dogs.add(model.getNombre());
                                        holder.setImagechecked(getApplicationContext(),"android.resource://" + getPackageName() +"/"+R.drawable.calif);
                                    }else{
                                        System.out.println(getResources().getString(R.string.No_tiene_vacunas));
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Configuracion_Paseo.this);
                                        builder.setTitle("Oh no!");
                                        builder.setMessage((getResources().getString(R.string.El_perfil_de))+model.getNombre()+ getResources().getString(R.string.Aun_no_cuenta_con_información_de_vacunas_en_su_perfil));
                                        builder.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mBuilder = new AlertDialog.Builder(Configuracion_Paseo.this);
                                                final View mVieww = getLayoutInflater().inflate(R.layout.dialog_add_vacunas, null);
                                                mBuilder.setView(mVieww);
                                                mBuilder.setCancelable(false);
                                                dialog = mBuilder.create();
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog.show();

                                                final Button enviar = (Button) mVieww.findViewById(R.id.btn_send);
                                                Button cancelar = (Button) mVieww.findViewById(R.id.btn_cancel_add);


                                                cancelar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                enviar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        DatabaseReference personsRef19;
                                                        personsRef19 = FirebaseDatabase.getInstance().getReference().child("Perros");

                                                        final Map<String, Object> vacunas = new HashMap<>();
                                                        vacunas.put("desparacitado",desparasitacion);
                                                        vacunas.put("primeraDosis",primeraVac);
                                                        vacunas.put("segundaDosis",segundaVac);
                                                        vacunas.put("terceraDosis",terceraVac);

                                                        personsRef19.child(user.getUid()).child(model.getIdPerro()).child("vacunas").updateChildren(vacunas).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                dialog.dismiss();
                                                                
                                                            }
                                                        });

                                                    }
                                                });

                                                final Spinner spinnerdespara = mVieww.findViewById(R.id.sp_desparasitado);
                                                final Spinner spinner1ra = mVieww.findViewById(R.id.sp_1radosis);
                                                final Spinner spinner2da = mVieww.findViewById(R.id.sp_2dadosis);
                                                final Spinner spinner3ra = mVieww.findViewById(R.id.sp_3radosis);

                                                final ArrayAdapter<CharSequence> adaptersino = ArrayAdapter.createFromResource(Configuracion_Paseo.this, R.array.sino, android.R.layout.simple_spinner_item);

                                                adaptersino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                                                spinnerdespara.setAdapter(adaptersino);
                                                spinnerdespara.setOnItemSelectedListener(new Configuracion_Paseo.DesparaSpinnerClass());

                                                spinner1ra.setAdapter(adaptersino);
                                                spinner1ra.setOnItemSelectedListener(new Configuracion_Paseo.PrimeraSpinnerClass());

                                                spinner2da.setAdapter(adaptersino);
                                                spinner2da.setOnItemSelectedListener(new Configuracion_Paseo.SegundaSpinnerClass());

                                                spinner3ra.setAdapter(adaptersino);
                                                spinner3ra.setOnItemSelectedListener(new Configuracion_Paseo.TerceraSpinnerClass());


                                            }
                                        });
                                        builder.show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }



                    }
                });


            }

            @Override
            public Configuracion_Paseo.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_select_perro, parent, false);

                //return new PetsViewHolder(view);
                return  new Configuracion_Paseo.PetsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);

        fill_spinner();


        pagar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagar_btn.setClickable(false);
                TextView txt =(TextView)findViewById(R.id.textView_id_spinner);
                System.out.println(txt.getText());
                if (txt.getText().toString().equals("")){
                    Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.Para_continuar_es_necesario_que_ingreses_una_tarjeta)), Toast.LENGTH_LONG).show();
                    Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.Da_clic_en_el_boton_de_agregar)), Toast.LENGTH_LONG).show();
                    pagar_btn.setClickable(true);
                }else{

                    if (paquete){

                        //Toast.makeText(Configuracion_Paseo.this, txt.getText(), Toast.LENGTH_LONG).show();



                        androidx.appcompat.app.AlertDialog.Builder mBuilder_x = new androidx.appcompat.app.AlertDialog.Builder(Configuracion_Paseo.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);//,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
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


                        txt_info.setText(getResources().getString(R.string.Estamos_tramitando_la_orden_de_pago_del_paquete_agendado_para));
                        txt_perros.setText(namesperros);

                        Glide.with(getApplicationContext()).load(R.raw.gif_agenda).apply(RequestOptions.circleCropTransform()).into(carga_paseo);



                        dialog_x.show();

                        //pagar aqui
                        try {

                            final JSONObject jsonObj = new JSONObject();
                            try {


                                jsonObj.put("uid",user.getUid());
                                jsonObj.put("amount",totalApagar);
                                jsonObj.put("id_card", txt.getText());
                                jsonObj.put("customer_id", customer_id );
                                jsonObj.put("calificacion",5.0);
                                jsonObj.put("categoria",categoria );
                                jsonObj.put("numero_perros",num_perros);
                                jsonObj.put("perros",idsperros  );
                                if (tiempo==1||tiempo==2){
                                    jsonObj.put("tiempo_paseo",tiempo);
                                }else if (tiempo == 1.5) {
                                    jsonObj.put("tiempo_paseo",1.5);
                                }else {
                                    jsonObj.put("tiempo_paseo",0.5);
                                }
                                jsonObj.put("latitud",latitud );
                                jsonObj.put("longitud",longitud  );
                                jsonObj.put("direccion",direccion );
                                jsonObj.put("monto_paseador",subtotalCompra);
                                jsonObj.put("dias",dias);
                                jsonObj.put("diasEleccion",diasEleccion);
                                jsonObj.put("perrosNombre",namesperros);



                                //







                                //




                            } catch (JSONException e) {
                                Toast.makeText(Configuracion_Paseo.this, (getResources().getString(R.string.Error)), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }







                            //System.out.println(""+jsonObj);



                            OkHttpClient httpClient = new OkHttpClient();
                            HttpUrl.Builder httpBuider =
                                    HttpUrl.parse(Conecta_Caminandog.ORDER_AGENDA_FUNC).newBuilder();
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
                                        Toast.makeText(Configuracion_Paseo.this,
                                                (getResources().getString(R.string.No_se_obtuvo_respuesta_del_servidor)),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            resp = responseBody.string();
                                            System.out.println(resp);



                                            if (resp.contains( "error" )){

                                                String[] res = resp.split( "," );
                                                String resx= res[0];
                                                String res_message = res[1];

                                                Configuracion_Paseo.this.runOnUiThread( new Runnable() {
                                                    public void run() {
                                                        System.out.println(getResources().getString(R.string.error_funcion_no_se_proceso_el_pago));
                                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder ( Configuracion_Paseo.this );
                                                        builder.setTitle(getResources().getString(R.string.Pago_no_realizado));
                                                        builder.setMessage ( res_message );
                                                        builder.setPositiveButton ( (getResources().getString(R.string.Aceptar)) ,new DialogInterface.OnClickListener () {
                                                            @Override
                                                            public void onClick(DialogInterface dialog ,int which) {
                                                                progressBar.setVisibility( View.INVISIBLE );
                                                                dialog_x.dismiss();
                                                                pagar_btn.setClickable(true);


                                                            }
                                                        } );
                                                        builder.setNegativeButton( (getResources().getString(R.string.CANCELAR)), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                //startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                                                //finish();
                                                                //fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).addToBackStack(null).commit();
                                                                //getSupportActionBar().setTitle("Mi Cuenta");
                                                                pagar_btn.setClickable(true);


                                                            }
                                                        } );
                                                        builder.show ();
                                                    }
                                                });



                                            }else {


                                                System.out.println(resp);



                                                String[] respuesta = resp.split( "," );
                                                String mensaje= respuesta[0];
                                                if (mensaje.equals( "correcto" )){

                                                    if (FirebaseReferences.NOTIFICATIONS_CONTROL){

                                                        final JSONObject jsonObj_not = new JSONObject();
                                                        try {
                                                            jsonObj_not.put("uid",user.getUid());
                                                        } catch (JSONException e) {
                                                            Toast.makeText(Configuracion_Paseo.this, (getResources().getString(R.string.Error)), Toast.LENGTH_LONG).show();
                                                            e.printStackTrace();
                                                        }
                                                        //
                                                        OkHttpClient httpClient = new OkHttpClient();
                                                        HttpUrl.Builder httpBuider =
                                                                HttpUrl.parse(Conecta_Caminandog.AGEND_FUNC).newBuilder();
                                                        httpBuider.addQueryParameter("text",""+jsonObj_not);
                                                        final Request req = new Request.Builder().
                                                                url(httpBuider.build()).build();
                                                        httpClient.newCall(req).enqueue(new Callback() {
                                                            @Override
                                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                                Log.e(TAG, "error in getting response from firebase cloud function");
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(Configuracion_Paseo.this,
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
                                                        //

                                                    }else {
                                                        System.out.println("notifiactions disabled agenda");

                                                    }

                                                    Configuracion_Paseo.this.runOnUiThread( new Runnable() {
                                                        public void run() {

                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    new AlertDialog.Builder(Configuracion_Paseo.this)
                                                                            .setTitle((getResources().getString(R.string.Compra_exitosa)))
                                                                            .setMessage((getResources().getString(R.string.En_un_periodo_máximo_de_2_horas)))
                                                                            .setCancelable(false)
                                                                            .setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    Configuracion_Paseo.this.finish();
                                                                                }
                                                                            })

                                                                            .show();
                                                                }
                                                            }, 2500);









                                                            //Toast.makeText(Configuracion_Paseo.this, "Pago procesado correctamente", Toast.LENGTH_SHORT).show();
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

                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.e(TAG, "error in getting response from firebase cloud function");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Configuracion_Paseo.this,
                                                    (getResources().getString(R.string.No_se_obtuvo_respuesta_del_servidor)),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }



                                private void runOnUiThread(Runnable runnable) {


                                }

                            });









                            //query1.removeEventListener(  );


                        }catch (Exception e){Toast.makeText(Configuracion_Paseo.this, (getResources().getString(R.string.Ocurrio_un_error_al_procesar_el_pago))+e, Toast.LENGTH_LONG).show();}
                        //end pagar aqui

                    }else {

                        System.out.println(direccion);

                        Intent iopi = new Intent(Configuracion_Paseo.this, Maps_Solic_Pas.class);


                        //iopi.putExtra("order_id_pago", "");
                        iopi.putExtra( "perros", idsperros );
                        iopi.putExtra("direccion_direccion", direccion );
                        iopi.putExtra( "latitud_pago",latitud );
                        iopi.putExtra( "longitud_pago",longitud );
                        iopi.putExtra( "numPerros_pago" , num_perros );
                        iopi.putExtra( "modalidad_pago", categoria );
                        iopi.putExtra( "numero_perros",num_perros );
                        //iopi.putExtra("sessionid", dv);
                        iopi.putExtra("amount",totalApagar);
                        iopi.putExtra("id_card",txt.getText());
                        iopi.putExtra( "customer_id",customer_id );
                        if (tiempo==1||tiempo==2){
                            iopi.putExtra("tiempo_paseo",tiempo);
                        }else if  (tiempo == 1.5) {
                            iopi.putExtra("tiempo_paseo",1.5);
                        }else {
                            iopi.putExtra("tiempo_paseo",0.5);
                        }
                        iopi.putExtra( "monto_paseador",subtotalCompra );
                        iopi.putExtra( "names_dogs",namesperros );
                        iopi.putExtra( "descuento_exist", id_cod_descuento);

                        System.out.println("desdes "+cuponDescuento);



                        pagar_btn.setClickable(true);


                        startActivity( iopi);
                        //finish();

                    }

                }

            }
        });







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

                                    SpinnerAdapter adapter = new SpinnerAdapter(Configuracion_Paseo.this, R.layout.spinner_custom_cards, titleList, idList, imageArray);
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

                            SpinnerAdapter adapter = new SpinnerAdapter(Configuracion_Paseo.this, R.layout.spinner_custom_cards, titleList, idList, imageArray);
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



    public class RVAdapter extends RecyclerView.Adapter<Configuracion_Paseo.RVAdapter.PersonViewHolder>{


        List<Person> persons;

        RVAdapter(List<Person> persons){
            this.persons = persons;
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            //CardView cv;
            TextView personName;

            ImageView personPhoto;

            PersonViewHolder(final View itemView3) {
                super(itemView3);
                //cv = (CardView)itemView3.findViewById(R.id.dd);
                personName = (TextView)itemView3.findViewById(R.id.textView_cate);

                personPhoto = (ImageView)itemView3.findViewById(R.id.imageView_cate);

                itemView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println("d "+personName.getText());

                        categoria = personName.getText().toString();

                        slideRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.show_from_right);
                        slideLeft = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_left);


                        if (0 < selected_dogs.size() && selected_dogs.size() < 5){

                            //stringBuilder.toString()

                            //stringBuilder_name.toString()

                            if (categoria.equals("sport") && selected_dogs.size() >= 3){

                                Toast.makeText(Configuracion_Paseo.this, (getResources().getString(R.string.La_categoria_Sport_no_admite)) , Toast.LENGTH_LONG).show();
                                Toast.makeText(Configuracion_Paseo.this, (getResources().getString(R.string.Modifica_tu_seleccion)) , Toast.LENGTH_LONG).show();

                            }else {

                                num_perros = selected_dogs.size();

                                first_line_txt = findViewById(R.id.txt_payment_1st);
                                glob_cost_txt = findViewById(R.id.txt_glob_cost_payment);
                                second_line_txt = findViewById(R.id.txt_payment_2nd);
                                descuento_value_text = findViewById(R.id.txt_descuento_payment);
                                desc_word = findViewById(R.id.textView_tipopago);
                                subtotal_value_text = findViewById(R.id.txt_subtotal_payment);
                                iva_value_text = findViewById(R.id.txt_iva_payment);
                                total_value_text = findViewById(R.id.txt_total_payment);
                                cup_num_txt = findViewById(R.id.textView_tiempoffpago);
                                cup_txt = findViewById(R.id.textView_tiempopago);

                                StringBuilder stringBuilder = new StringBuilder();
                                for (String s : selected_dogs){
                                    stringBuilder.append(s);
                                    stringBuilder.append(",");
                                }

                                StringBuilder stringBuilder_name = new StringBuilder();
                                for (String ss : names_dogs){
                                    stringBuilder_name.append(ss);
                                    stringBuilder_name.append(",");
                                }


                                idsperros = stringBuilder.toString();
                                namesperros = stringBuilder_name.toString();

                                //Toast.makeText(Configuracion_Paseo.this, "Seleccionaste "+selected_dogs.size()+" Perros"+stringBuilder.toString()+stringBuilder_name.toString(), Toast.LENGTH_LONG).show();

                                if (tiempo==media_val){
                                    first_line_txt.setText(selected_dogs.size()+" perritos, 30 min.");
                                }else if (tiempo==noventa_val){
                                    first_line_txt.setText(selected_dogs.size()+" perritos, 90 min.");
                                }else if (tiempo==1){
                                    first_line_txt.setText(selected_dogs.size()+" perritos, "+1+" h.");
                                }else{
                                    String x = String.valueOf(tiempo).substring(0,1);
                                    first_line_txt.setText(selected_dogs.size()+" perritos, "+x+" h.");
                                }
                                if (dias==1){
                                    second_line_txt.setText(categoria+", "+dias+" dia.");
                                }else {
                                    second_line_txt.setText(categoria+", "+dias+" dias.");
                                }






                                final DatabaseReference iva_ref = database.getReference(FirebaseReferences.IVA_REFERERENCE);
                                final Query queryiva = iva_ref.orderByChild( "iva" );

                                queryiva.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            //String tiempos = Integer.toString(tiempo);
                                             iva = (Double) snapshot.getValue();



                                            final DatabaseReference costos_ref = database.getReference(FirebaseReferences.COSTO_REFERERENCE);
                                            final Query query = costos_ref.orderByChild("tipo").equalTo(categoria);

                                            query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    xx.setAnimation(slideLeft);
                                                    xx.setVisibility(View.INVISIBLE);
                                                    xy.setAnimation(slideRight);
                                                    xy.setVisibility(View.VISIBLE);

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        Double price = (Double) snapshot.child("1").getValue();
                                                        costounitario = price;
                                                    }

                                                    Payment();
                                                    //dialog_add_cod.show();





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
                                } );


                            }









                        }else if (selected_dogs.size()==0){
                            Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.Para_continuar_debes_seleccionar_al_menos_un_perrito)), Toast.LENGTH_LONG).show();
                            Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.Agrega_uno)), Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.La_categoría_elegida_no_admite_más_de_4_perritos_seleccionados)) , Toast.LENGTH_LONG).show();
                            Toast.makeText(Configuracion_Paseo.this,(getResources().getString(R.string.Modifica_tu_seleccion)) , Toast.LENGTH_LONG).show();
                        }








//bgbgbg

                        /*xx.setVisibility(View.INVISIBLE);
                        xy.setAnimation(slideLeft);
                        xy.setVisibility(View.VISIBLE);*/



                       // Toast.makeText( Configuracion_Paseo.this,personName.getText(),Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(Configuracion_Paseo.this, Seleccion_Perros.class);
                        i.putExtra("categoria_tipo",personName.getText());
                        i.putExtra( "tiempo_tipo",1 );//cambiar por variable
                        i.putExtra( "direccion_tipo",direccion );
                        i.putExtra( "latitud_tipo",latitud );
                        i.putExtra( "longitud_tipo",longitud );
                        startActivity( i );*/
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }

        @Override
        public Configuracion_Paseo.RVAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_categoria, viewGroup, false);
            Configuracion_Paseo.RVAdapter.PersonViewHolder pvh = new Configuracion_Paseo.RVAdapter.PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(Configuracion_Paseo.RVAdapter.PersonViewHolder personViewHolder, int i) {
            personViewHolder.personName.setText(persons.get(i).name);
            personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    public class RVAdapter_time extends RecyclerView.Adapter<Configuracion_Paseo.RVAdapter_time.PersonViewHolder>{


        List<Time> times;

        RVAdapter_time(List<Time> times){
            this.times = times;
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView personName;

            ImageView personPhoto;

            PersonViewHolder(final View itemView2) {
                super(itemView2);
                cv = (CardView)itemView2.findViewById(R.id.ddd);
                personName = (TextView)itemView2.findViewById(R.id.textView_time);
                personPhoto = (ImageView)itemView2.findViewById(R.id.imageView_time);

            }

        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        @Override
        public Configuracion_Paseo.RVAdapter_time.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_time, viewGroup, false);
            Configuracion_Paseo.RVAdapter_time.PersonViewHolder pvh = new Configuracion_Paseo.RVAdapter_time.PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(Configuracion_Paseo.RVAdapter_time.PersonViewHolder personViewHolder, int i) {
            int id2 = getResources().getIdentifier(getPackageName()+":drawable/" + "unselected", null, null);
            personViewHolder.personName.setText(times.get(i).name);
            personViewHolder.personPhoto.setImageResource(id2);


            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getResources().getIdentifier(getPackageName()+":drawable/" + "calif", null, null);
                    //personViewHolder.personPhoto.setImageResource(id2);


                    selected_time.clear();
                    selected_time.add(times.get(i).name);
                    if (personViewHolder.getAdapterPosition()==0){
                        notifyItemChanged(1);
                    }else {
                        notifyItemChanged(0);
                    }

                    System.out.println(personViewHolder.getAbsoluteAdapterPosition()+" vs "+personViewHolder.getAdapterPosition());






                    //Toast.makeText(Configuracion_Paseo.this, times.get(i).name, Toast.LENGTH_LONG).show();
                    personViewHolder.personPhoto.setImageResource(id);



                    /*if (selected_time.contains(times.get(i).name)){

                        selected_time.remove(times.get(i).name);
                        personViewHolder.personPhoto.setImageResource(id2);


                    }else {
                        selected_time.add(times.get(i).name);
                        personViewHolder.personPhoto.setImageResource(id);
                        selected = personViewHolder.getAdapterPosition();

                    }*/



                }
            });


        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }



    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
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
    protected void onPause() {
        super.onPause();
        System.out.println("PAUSED");
        mPeopleRVAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPeopleRVAdapter.startListening();
    }

    public static class PetsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PetsViewHolder(View itemViewx){
            super(itemViewx);
            mView = itemViewx;
        }
        public void setTitle(String title2){
            TextView post_title2 = (TextView)mView.findViewById(R.id.nombreselecteddog);
            post_title2.setText(title2);
        }


        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.img_perro);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }

        public void setImagechecked(Context cx, String image2){
            ImageView post_image2 = (ImageView) mView.findViewById(R.id.selected);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(cx).load(image2).apply(RequestOptions.circleCropTransform()).into(post_image2);
        }

        public void setImagenotchecked(Context ct, String image3){
            ImageView post_image3 = (ImageView) mView.findViewById(R.id.selected);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ct).load(image3).into(post_image3);
        }
    }


    //spinners add perro

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
                        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(Configuracion_Paseo.this);
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
                        Conekta.collectDevice(Configuracion_Paseo.this);

                        Card card = new Card(scanResult.cardholderName, scanResult.cardNumber, scanResult.cvv, String.valueOf(scanResult.expiryMonth), String.valueOf(scanResult.expiryYear));
                        Token token = new Token(Configuracion_Paseo.this);

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
                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                Log.e(TAG, "error in getting response from firebase cloud function");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Configuracion_Paseo.this,
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

                                                    Log.e(TAG, "fail response from firebase cloud function");
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(Configuracion_Paseo.this,
                                                                    "Cound’t get response from cloud function",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {

                                                    resp = responseBody.string();
                                                    if (resp.equals( "correcto" )){

                                                        //customer_id = "no dio tiempo";

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(Configuracion_Paseo.this,
                                                                        (getResources().getString(R.string.Tu_tarjeta_fue_ingresada_correctamente_ahora_puedes_utilizarla_para_hacer_feliz_a_tu_perrito)), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        dialog.dismiss();
                                                        fill_spinner();


                                                    }else{

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(Configuracion_Paseo.this,
                                                                        (getResources().getString(R.string.Tu_tarjeta_no_se_pudo_agregar_por_favor_ingresa_otra)), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }

                                                }
                                                runOnUiThread(responseRunnable(resp));





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
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Configuracion_Paseo.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex);
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "mx.com.caminandog.fileprovider",
                        photoFile);
                uri= photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, TAKE_PICTURE);
                TakePictureActivityResultLauncher.launch(takePictureIntent);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file nameF

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Configuracion_Paseo.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    class EdadSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            edad = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    class PadecimientoSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            padecimiento = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    class ComportamientoSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            comportamiento = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    class RazaSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            raza = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class DesparaSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            desparasitacion = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class AntipulgasSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            antipulgas = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class PrimeraSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            primeraVac = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class SegundaSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            segundaVac = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class TerceraSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            terceraVac = parent.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private Runnable responseRunnable(final String responseStr){
        Runnable resRunnable = new Runnable(){
            public void run(){
                Toast.makeText(Configuracion_Paseo.this
                        ,responseStr,
                        Toast.LENGTH_SHORT).show();
            }
        };
        return resRunnable;
    }

    protected  void Add_Cupon(){
        androidx.appcompat.app.AlertDialog.Builder mBuilder_add_code = new androidx.appcompat.app.AlertDialog.Builder(Configuracion_Paseo.this);
        final View mView2 = getLayoutInflater().inflate(R.layout.dialog_agregar_cupon, null);
        mBuilder_add_code.setView(mView2);

        TextView txt_añadir = mView2.findViewById(R.id.añadir_txt_dialog);
        Button btn_añadir = mView2.findViewById(R.id.añadir_btn_dialog);
        Button btn_cancel_cod = mView2.findViewById(R.id.canceld_cod);



        DatabaseReference cupons = database.getReference(FirebaseReferences.CUPONS_REFERENCE);
        DatabaseReference paseos = database.getReference(FirebaseReferences.PASEO_USR_REFERERENCE);
        DatabaseReference usuario = database.getReference(FirebaseReferences.USER_REFERERENCE).child(user.getUid());
        Query exist_paseo = paseos.child(user.getUid());



        dialog_add_cod = mBuilder_add_code.create();
        dialog_add_cod.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cup_txt.setClickable(true);
        cup_txt.setText((getResources().getString(R.string.Agregar_cupon)));


        final DatabaseReference user_cup_ref = database.getReference(FirebaseReferences.USER_REFERERENCE).child(user.getUid()).child("cupones");
        final Query query_cup = user_cup_ref;
        query_cup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    cup_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_azul));
                    cup_num_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_azul));
                }else {
                    cup_txt.setText((getResources().getString(R.string.Codigo_no_aplicable)));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        cup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add_cod.show();
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
                                                                    S_dialog(getResources().getString(R.string.Codigo_no_ingresado), getResources().getString(R.string.Solo_puedes_agregar_un_codigo_hasta_que_utilices_o_borres_el_cupon_activo));
                                                                }else {
                                                                    usr_cupon_query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                S_dialog(getResources().getString(R.string.Codigo_invalido), getResources().getString(R.string.El_codigo_de_promoción_ya_se_ha_aplicado_anteriormente));
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
                                                                                        S_dialog(getResources().getString(R.string.Codigo_valido), getResources().getString(R.string.Codigo_agregado_correctamente));

                                                                                        dialog_add_cod.dismiss();
                                                                                        Payment();
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
                                                        S_dialog(getResources().getString(R.string.Codigo_invalido), getResources().getString(R.string.El_codigo_de_promocion_solo_es_válido_para_nuevos_usuarios));

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });



                                        }else {
                                            S_dialog(getResources().getString(R.string.Codigo_no_aplicable), getResources().getString(R.string.El_codigo_de_promoción_ya_se_ha_aplicado_anteriormente));

                                        }


                                    }else {
                                        S_dialog(getResources().getString(R.string.Codigo_invalido), getResources().getString(R.string.El_codigo_de_promoción_no_es_valido_o_ha_caducado));

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else {
                            S_dialog(getResources().getString(R.string.Ingresa_un_Codigo), getResources().getString(R.string.Debes_ingresar_un_codigo));

                        }
                    }
                });
            }
        });

        btn_cancel_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add_cod.dismiss();

            }
        });



    }

    private void S_dialog (String title, String message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder ( Configuracion_Paseo.this );
        builder.setTitle ( title );
        builder.setMessage ( message );
        builder.setPositiveButton ( (getResources().getString(R.string.Aceptar)) ,new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog ,int which) {


            }
        });

        builder.show ();

    }




    protected void Payment (){




        costoServicio = costounitario * num_perros * tiempo * dias;// Obtiene el costo unitario y lo multiplica los perros y por las horas

        if (tiempo==media_val){

            if(dias == 1){

                if(num_perros == 1){ porcentajeDescuento = 0 ;} else if(num_perros == 2){ porcentajeDescuento = 0; } else if(num_perros == 3){ porcentajeDescuento = 0; } else if(num_perros == 4){ porcentajeDescuento = 0; }


            }else if(dias == 3){

                if(num_perros == 1){ porcentajeDescuento = 0; } else if(num_perros == 2){ porcentajeDescuento = 0; }else if(num_perros == 3){ porcentajeDescuento = 0; }else if(num_perros == 4){  porcentajeDescuento = 0; }


            }else if (dias == 5){

                if(num_perros == 1){ porcentajeDescuento = 0; } else if(num_perros == 2){ porcentajeDescuento = 0; }else if(num_perros == 3){ porcentajeDescuento = 0; }else if(num_perros == 4){  porcentajeDescuento = 0; }

            }



        }else{

            if(dias == 1){

                if(num_perros == 1){ porcentajeDescuento = 0 ;} else if(num_perros == 2){ porcentajeDescuento = 0.1505; } else if(num_perros == 3){ porcentajeDescuento = 0.2233; } else if(num_perros == 4){ porcentajeDescuento = 0.2678; }


            }else if(dias == 3){

                if(num_perros == 1){ porcentajeDescuento = 0.1500; } else if(num_perros == 2){ porcentajeDescuento = 0.1925; }else if(num_perros == 3){ porcentajeDescuento = 0.2621; }else if(num_perros == 4){  porcentajeDescuento = 0.3044; }


            }else if (dias == 5 || dias == 10){

                if(num_perros == 1){ porcentajeDescuento = 0.2000; } else if(num_perros == 2){ porcentajeDescuento = 0.2095; }else if(num_perros == 3){ porcentajeDescuento = 0.2777; }else if(num_perros == 4){  porcentajeDescuento = 0.3191; }

            }



        }






        descuentoInterno = costoServicio * porcentajeDescuento;

        subtotalCompra = costoServicio - descuentoInterno;



        final DatabaseReference user_cup_ref = database.getReference(FirebaseReferences.USER_REFERERENCE).child(user.getUid()).child("cupones");
        final Query query_cup = user_cup_ref.orderByChild("activo").equalTo(true);
        query_cup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    cup_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                    cup_num_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        CuponUser cuponUser = ds.getValue(CuponUser.class);
                        String unidadDescuento = cuponUser.getUnidad();
                        id_cod_descuento = cuponUser.getId();
                        int cup_perros = cuponUser.getPerros();
                        double cup_tiempo = cuponUser.getTiempo();
                        String cup_categoria = cuponUser.getCategoria();

                        //System.out.println("wannaknow "+tiempo+" cuptiempo "+cup_tiempo);

                        if (cup_perros==num_perros&&cup_tiempo==tiempo&&cup_categoria.equals(categoria)&&!paquete){
                            cup_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                            cup_num_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                            if(unidadDescuento.equals("%") ){

                                cuponDescuento = subtotalCompra * cuponUser.getCifra();
                                subtotalCompra = subtotalCompra - cuponDescuento;

                                ivaDeCompra = subtotalCompra * iva;
                                totalApagar = subtotalCompra + ivaDeCompra;


                            }else if(unidadDescuento.equals("$") ){

                                cuponDescuento = cuponUser.getCifra();
                                subtotalCompra = subtotalCompra - cuponDescuento;

                                ivaDeCompra = subtotalCompra * iva;
                                totalApagar = subtotalCompra + ivaDeCompra;

                            }

                        }else {
                            cup_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup));
                            cup_txt.setText(getResources().getString(R.string.Codigo_no_aplicable));
                            cup_num_txt.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup));

                            //  S_dialog();
                            ivaDeCompra = subtotalCompra * iva;
                            totalApagar = subtotalCompra + ivaDeCompra;
                            cuponDescuento = 0.0;
                            id_cod_descuento="";
                            Add_Cupon();
                        }
                    }

                }else {
                    ivaDeCompra = subtotalCompra * iva;
                    totalApagar = subtotalCompra + ivaDeCompra;
                    Add_Cupon();
                }
                costoServicio = new BigDecimal(costoServicio).setScale(2, RoundingMode.HALF_UP).doubleValue();
                descuentoInterno = new BigDecimal(descuentoInterno).setScale(2, RoundingMode.HALF_UP).doubleValue();
                subtotalCompra = new BigDecimal(subtotalCompra).setScale(2, RoundingMode.HALF_UP).doubleValue();
                ivaDeCompra = new BigDecimal(ivaDeCompra).setScale(2, RoundingMode.HALF_UP).doubleValue();
                totalApagar = new BigDecimal(totalApagar).setScale(2, RoundingMode.HALF_UP).doubleValue();


                descuento_value_text.setText("-MXN $"+descuentoInterno);
                if (descuentoInterno!=0.0){
                    descuento_value_text.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                    desc_word.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup_verde));
                }else {
                    descuento_value_text.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup));
                    desc_word.setTextColor(ContextCompat.getColor(Configuracion_Paseo.this, R.color.color_texto_cup));
                }
                subtotal_value_text.setText("MXN $"+subtotalCompra);
                glob_cost_txt.setText("MXN $"+costoServicio);
                iva_value_text.setText("MXN $"+ivaDeCompra);
                total_value_text.setText("MXN $"+totalApagar);
                cup_num_txt.setText("-MXN $"+cuponDescuento);
                progressBar.setVisibility( View.INVISIBLE );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, TAKE_PICTURE);
                    }
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Permisos_Denegados)), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Caminandog_podria_no_funcionar_correctamente)), Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    ActivityResultLauncher<Intent> TakePictureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        Bitmap imageBitmap = null;
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(Configuracion_Paseo.this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }

                        //Bundle extras = data.getExtras();
                        //Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                        data_pick = baos.toByteArray();

                        Glide.with(Configuracion_Paseo.this).load(imageBitmap).apply(RequestOptions.circleCropTransform()).into(mUpload);
                    }
                }
            });

    ActivityResultLauncher<Intent> PickPictureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        uri = data.getData();
                        Glide.with(Configuracion_Paseo.this).load(uri).apply(RequestOptions.circleCropTransform()).into(mUpload);
                    }
                }
            });






    //



}
