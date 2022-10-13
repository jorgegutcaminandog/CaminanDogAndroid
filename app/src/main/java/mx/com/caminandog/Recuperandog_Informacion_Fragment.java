package mx.com.caminandog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recuperandog_Informacion_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recuperandog_Informacion_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recuperandog_Informacion_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    MapView mapView;
    public GoogleMap gMap;
    Marker marker;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    LatLng ubic;
    String nombrePerr;

    Bundle bundle = new Bundle();
    String idPerro;




    public Recuperandog_Informacion_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recuperandog_Informacion_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Recuperandog_Informacion_Fragment newInstance(String param1, String param2) {
        Recuperandog_Informacion_Fragment fragment = new Recuperandog_Informacion_Fragment();
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
        View vista = inflater.inflate(R.layout.fragment_recuperandog__informacion_, container, false);

        Bundle bundle2 = getArguments();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        ImageView foto = (ImageView)vista.findViewById(R.id.img_recup_info);
        Glide.with(getActivity()).load(bundle2.getString("foto")).apply( RequestOptions.circleCropTransform()).into(foto);
        Button reportar = vista.findViewById(R.id.btnReportar);



        TextView txt_nombreper = (TextView)vista.findViewById(R.id.nombreperrorecup);
        txt_nombreper.setText(bundle2.getString("nombre"));
        nombrePerr=bundle2.getString("nombre");

        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Reportado_Fragment fragment2 = new Reportado_Fragment();
                bundle.putString("idPerro", bundle2.getString("idPerro"));
                bundle.putString("nombre", bundle2.getString("nombre"));
                bundle.putString("foto", bundle2.getString("foto"));
                fragment2.setArguments(bundle);
                ft.replace(R.id.contenedor, fragment2);
                ft.commit();

            }
        });

        Button back = (Button) vista.findViewById(R.id.btnrecupback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ContenedorRecuperandog_Fragment fragment2 = new ContenedorRecuperandog_Fragment();


                ft.replace(R.id.contenedor, fragment2);
                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                ft.commit();
            }
        });


        DatabaseReference recuperandog_lectura_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child( bundle2.getString("idPerro") );
        Query query = recuperandog_lectura_ref.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Paseo_recuperandog paseo_recuperandog = dataSnapshot.getValue(Paseo_recuperandog.class);
                Date date = new Date(paseo_recuperandog.getTimestamp());
                Date date2 = new Date(paseo_recuperandog.getFechaVencimiento());


                //"E MMMM d, yyyy"

                DateFormat displayFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
                DateFormat displayFormat2 = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                System.out.println(displayFormat.format(date));

                TextView txt_fecha = (TextView)vista.findViewById(R.id.txt_fecha_lect);
                TextView txt_direc = (TextView)vista.findViewById(R.id.txt_direc_lect);
                TextView txt_nombre = (TextView)vista.findViewById(R.id.txt_nombre_lect);
                Button telefono = (Button) vista.findViewById(R.id.tel_lect);
                Button mensaje = (Button) vista.findViewById(R.id.msj_lect);
                TextView txt_titulo = (TextView)vista.findViewById(R.id.txt_no_recup_info);
                TextView txt_msjContacto = (TextView)vista.findViewById(R.id.msjcontact);
                TextView txt_fechaVig = (TextView)vista.findViewById(R.id.fechavigrecup);


                txt_titulo.setText((getResources().getString(R.string.Placa))+paseo_recuperandog.getQR());
                txt_fechaVig.setText(displayFormat2.format(date2));
                txt_fecha.setText(displayFormat.format(date));
                txt_direc.setText(paseo_recuperandog.getDireccion());
                txt_nombre.setText(paseo_recuperandog.getNombreContacto());
                txt_msjContacto.setText(paseo_recuperandog.getMensajeContacto());

                if (paseo_recuperandog.getReportado().equals("true")){
                    reportar.setText((getResources().getString(R.string.Reportado)));
                }



                telefono.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (GetPermisionCall()){
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse((getResources().getString(R.string.Tel))+paseo_recuperandog.getTelefonoContacto()));
                            startActivity(i);
                        }else{

                        }

                    }
                });

                mensaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("smsto:"+paseo_recuperandog.getTelefonoContacto());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "The SMS text");
                        startActivity(intent);

                    }
                });


                mapView = vista.findViewById(R.id.mapaView_recup);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        gMap = googleMap;
                        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        try {
                            ubic = new LatLng(paseo_recuperandog.getLatitud(), paseo_recuperandog.getLongitud());
                            gMap.addMarker(new MarkerOptions()
                                    .position(ubic)
                                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pinperro))
                                    .title((getResources().getString(R.string.Ubicacion_de)+nombrePerr)));
                            //gMap.addMarker(new MarkerOptions().position(ubic).title("Ubicacion de "+bundle2.getString("nombre"))).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinperro));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubic, 17));
                            //gMap.UiSettings.setZoomControlsEnabled(true);
                            //gMap.getUiSettings().setAllGesturesEnabled(true);
                            gMap.getUiSettings().setZoomControlsEnabled(true);
                            gMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
                            GetLocation();

                        }catch (Exception e){}










                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






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

    public void GetLocation(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
            System.out.println("obtener ubicacion");
            //mMap.setMyLocationEnabled(true);

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }else{

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

    public void centreMapOnLocation(Location location){
        if (location != null){

            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            gMap.clear();
            gMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pinusuario))
                    .title("Tu ubicacion"));
            gMap.addMarker(new MarkerOptions()
                    .position(ubic)
                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_pinperro))
                    .title("Ubicacion de "+nombrePerr));


            //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,20f));
            //locationManager.removeUpdates(locationListener);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng( ubic.latitude, ubic.longitude));
            builder.include(new LatLng(location.getLatitude(),location.getLongitude()));
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
            gMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-.6f);
                    gMap.animateCamera(zout);

                }

                @Override
                public void onCancel() {

                }
            });
        }else{
            //createLocationRequest();
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
