package mx.com.caminandog;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.GraphRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


public class ComprarRecuperandog extends Fragment implements OnMapReadyCallback {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int NumerodePlacas = 1;
    double total;
    double envio = 40.0;
    double subtotal;
    double costoPPlaquita = 0 ;
    private Spinner spinner;
    String customer_id;
    private static final int MY_SCAN_REQUEST_CODE = 3;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    AsyncTask[] asyncTask_reverse_geo;
    ImageView marker_img;
    private Geocoder geocoder;
    MapStyleOptions mapStyleOptions;
    Marker mCurrentLocationMarker;
    List<Place.Field> fields;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    Marker marker;
    TextView txt_places;
    TextView txt_places2;
    List<Address> addresses;
    ImageView carga_paseo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_comprar_recuperandog, container, false);

        ImageButton mas = vista.findViewById(R.id.btnMas);
        ImageButton menos = vista.findViewById(R.id.btnMenos);
        TextView numPlaquitastxt = vista.findViewById(R.id.txtPlacas);
        TextView costoPorPlaquita = vista.findViewById(R.id.costoplaquitatxt);
        TextView costoPorEnvio = vista.findViewById(R.id.txt_envio_payment);
        TextView subtotaltxt = vista.findViewById(R.id.txt_subtotal_recup);
        TextView Totaltxt = vista.findViewById(R.id.txt_total_recup);
        ProgressBar progressBar = vista.findViewById(R.id.progress_inicioRec);
        spinner = vista.findViewById(R.id.spinner_servicesrecup);
        final ImageButton scancard = vista.findViewById(R.id.scan_btn_confrecup);
        txt_places = (TextView) vista.findViewById(R.id.txt_direc1_princRec);
        txt_places2 = (TextView) vista.findViewById(R.id.txt_direc2_princRec);
        TextView nombre = (TextView) vista.findViewById(R.id.nombre_txt_inicioRec);
        geocoder = new Geocoder(getContext());
        asyncTask_reverse_geo = new AsyncTask[1];
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json); //chechar crash al inicio
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        geocoder = new Geocoder(getContext());
        asyncTask_reverse_geo = new AsyncTask[1];
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        marker_img = (ImageView) vista.findViewById(R.id.imageView2_markeyRec);
        marker_img.setVisibility(View.INVISIBLE);


        final DatabaseReference costos = database.getReference(FirebaseReferences.COSTO_REFERERENCE).child("Recuperandog");
        final Query query = costos;

        fill_spinner();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);



                Costos costos = dataSnapshot.getValue(Costos.class);
                costoPPlaquita = costos.getCostoAndroid();
                envio = costos.getEnvio();


                subtotal = costoPPlaquita*NumerodePlacas;
                total = subtotal+envio;
                subtotal = new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP).doubleValue();
                total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
                costoPorPlaquita.setText("$"+costoPPlaquita);
                costoPorEnvio.setText("MXN $"+envio);
                numPlaquitastxt.setText(NumerodePlacas+"");
                subtotaltxt.setText("MXN $"+subtotal);
                Totaltxt.setText("MXN $"+total);
                mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NumerodePlacas++;
                        subtotal = costoPPlaquita*NumerodePlacas;
                        total = subtotal+envio;
                        subtotal = new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        numPlaquitastxt.setText(NumerodePlacas+"");
                        subtotaltxt.setText("MXN $"+subtotal);
                        Totaltxt.setText("MXN $"+total);

                    }
                });
                menos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NumerodePlacas!=1){
                            NumerodePlacas--;
                            subtotal = costoPPlaquita*NumerodePlacas;
                            total = subtotal+envio;
                            subtotal = new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            numPlaquitastxt.setText(NumerodePlacas+"");
                            subtotaltxt.setText("MXN $"+subtotal);
                            Totaltxt.setText("MXN $"+total);
                        }

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        scancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);



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


        DatabaseReference comote_ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid());

        Query comoteQuery = comote_ref.orderByChild("menteraste");

        comoteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                if (dataSnapshot.exists()){
                    nombre.setText((getResources().getString(R.string.Hola)) + usuario.getNombre());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Button compra = vista.findViewById(R.id.btn_pagarRec);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compra.setClickable(false);
                TextView txt =(TextView)vista.findViewById(R.id.textView_id_spinner);
                System.out.println(txt.getText());

                if (txt.getText().toString().equals("")){
                    Toast.makeText(getActivity(),(getResources().getString(R.string.Para_continuar_es_necesario_que_ingreses_una_tarjeta)), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),(getResources().getString(R.string.Da_clic_en_el_boton_de_agregar)), Toast.LENGTH_LONG).show();
                    compra.setClickable(true);
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

                    txt_info.setText (getResources().getString(R.string.Estamos_tramitando_la_orden_de_pago_de_tus_plaquitas));


                    Glide.with(getApplicationContext()).load(R.raw.gif_agenda).apply(RequestOptions.circleCropTransform()).into(carga_paseo);



                    dialog_x.show();

                    //pagar aqui
                    final JSONObject jsonObj2 = new JSONObject();
                    try {


                        jsonObj2.put("uid", user.getUid());
                        jsonObj2.put("id_card",txt.getText() );
                        jsonObj2.put("amount", costoPPlaquita);
                        jsonObj2.put("customer_id", customer_id);
                        jsonObj2.put("cantidad",NumerodePlacas);
                        jsonObj2.put("envio",envio);
                        jsonObj2.put("direccion",txt_places.getText()+";"+txt_places2.getText());
                        jsonObj2.put("latitud",mMap.getCameraPosition().target.latitude);
                        jsonObj2.put("longitud",mMap.getCameraPosition().target.longitude);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    OkHttpClient httpClient = new OkHttpClient();
                    HttpUrl.Builder httpBuider =
                            HttpUrl.parse(Conecta_Caminandog.ORDER_COMPRA_PLAQUITA).newBuilder();
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
                                                System.out.println (getResources().getString(R.string.Error_funcion_no_se_proceso_el_pago));
                                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder ( getActivity() );
                                                builder.setTitle (getResources().getString(R.string.Pago_no_realizado));
                                                builder.setMessage ( res_message );
                                                builder.setPositiveButton ( (getResources().getString(R.string.Aceptar)),new DialogInterface.OnClickListener () {
                                                    @Override
                                                    public void onClick(DialogInterface dialog ,int which) {
                                                        //progressBar.setVisibility( View.INVISIBLE );
                                                        dialog_x.dismiss();
                                                        compra.setClickable(true);

                                                    }
                                                } );
                                                builder.setNegativeButton( (getResources().getString(R.string.CANCELAR)), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        //startActivity(new Intent(Maps_Solic_Pas.this, MainActivity.class));
                                                        //finish();
                                                        //fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).addToBackStack(null).commit();
                                                        //getSupportActionBar().setTitle("Mi Cuenta");
                                                        compra.setClickable(true);


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

                                            getActivity().runOnUiThread( new Runnable() {
                                                public void run() {

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            new AlertDialog.Builder(getActivity())
                                                                    .setTitle((getResources().getString(R.string.Compra_exitosa)))
                                                                    .setMessage((getResources().getString(R.string.En_un_periodo_maximo_de_2_horas_te_contactaremos_para_informarte_sobre_el_envio_de_tus_plaquitas)))
                                                                    .setCancelable(false)
                                                                    .setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
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

                                    ComprarRecuperandog.SpinnerAdapter adapter = new ComprarRecuperandog.SpinnerAdapter(getContext(), R.layout.spinner_custom_cards, titleList, idList, imageArray);
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

                            ComprarRecuperandog.SpinnerAdapter adapter = new ComprarRecuperandog.SpinnerAdapter(getContext(), R.layout.spinner_custom_cards, titleList, idList, imageArray);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);




                if (scanResult.isExpiryValid()) {
                    if (scanResult.cvv != null) {
                        //
                        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                        Conekta.collectDevice(getActivity());

                        Card card = new Card(scanResult.cardholderName, scanResult.cardNumber, scanResult.cvv, String.valueOf(scanResult.expiryMonth), String.valueOf(scanResult.expiryYear));
                        Token token = new Token(getActivity());

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

                                                    Log.e(TAG, "fail response from firebase cloud function");
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(),
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
                                                                Toast.makeText(getActivity(),
                                                                        (getResources().getString(R.string.Tu_tarjeta_fue_ingresada_correctamente_ahora_puedes_utilizarla_para_hacer_feliz_a_tu_perrito)),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        dialog.dismiss();
                                                        fill_spinner();


                                                    }else{

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getActivity(),
                                                                        (getResources().getString(R.string.Tu_tarjeta_no_se_pudo_agregar_por_favor_ingresa_otra)),Toast.LENGTH_SHORT).show();
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

        title.setText(getResources().getString(R.string.GPS_Desactivado));
        subtitle.setText(getResources().getString(R.string.Por_favor_activa_el_GPS_de_tu_dispositivo));

        Button si = (Button)viewww.findViewById(R.id.btn_yes);
        Button no = (Button)viewww.findViewById(R.id.btn_nel);

        no.setVisibility( View.GONE );
        si.setText (getResources().getString(R.string.Activar));

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
                mMap.clear();

                mCurrentLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(mMap.getCameraPosition().target).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));



            }
        });


        //



        //gMap.setPadding(0,0,0,mapView.getHeight());


        mMap.setMapStyle(mapStyleOptions);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //lin_direc.animate().translationY(getView().getHeight());


        ///mMap.setPadding(0, cardView_sup.getHeight() - 15, 0, linearLayout_inf.getHeight() + 15);


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







        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);

                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);

                //gMap.clear();





                asyncTask_reverse_geo[0] = new ComprarRecuperandog.ReverseGeocoding().execute(mMap.getCameraPosition().target);



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




                //gMap.setPadding(0,cardView_sup.getHeight()-15,0,linearLayout_inf.getHeight()+15);
                mMap.clear();



            }
        });



        GetLocation();

    }

    public void GetLocation(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
            System.out.println(getResources().getString(R.string.Obtener_ubicacion));
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
            subtitlemsj.setText(getResources().getString(R.string.Recuerda_que_para_tener_una_precision_del_lugar_donde_requieres_tus_paseos));
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

    public void centreMapOnLocation(Location location){
        if (location != null){
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.clear();
            //marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,20f));
            if( locationCallback != null){
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
            //locationManager.removeUpdates(locationListener);
        }else{
            //createLocationRequest();
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
                    subtitlemsj.setText(getResources().getString(R.string.Recuerda_que_para_tener_una_precision_del_lugar_donde_requieres_tus_paseos));
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
}