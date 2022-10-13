package mx.com.caminandog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.util.ArrayList;
import java.util.List;


public class Historic_Map extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap gMap;
    MapView mView;
    LocationRequest mLocationRequest;
    Location mLocation;
    Marker mCurrentLocationMarker;
    GoogleApiClient mGoogleApiClient;
    boolean isFirstTime = true;

    Chronometer crono;

    RecyclerView rv;
    adaptador adapter;
    List<ModelDatosPipiPopo> modelDatosPipiPopos;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Historic_Map() {
        // Required empty public constructor
    }

    public static Historic_Map newInstance(String param1, String param2) {
        Historic_Map fragment = new Historic_Map();
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
        checkLocationPermission();

        View v = inflater.inflate(R.layout.fragment_tacking_map, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        modelDatosPipiPopos = new ArrayList<>();

        ImageButton fotopas = (ImageButton) v.findViewById( R.id.img_pas );
        TextView nombpas = (TextView) v.findViewById( R.id.nom_txt );







        Bundle bundle = this.getArguments();
        final String order_id = bundle.getString( "order_id" );
        final String pas_id = bundle.getString( "pas_id" );

        DatabaseReference pas_ref = FirebaseDatabase.getInstance().getReference("Paseadores").child(pas_id);
        Query query_pas = pas_ref;
        query_pas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Paseador paseador = dataSnapshot.getValue(Paseador.class);
                Glide.with(getActivity()).load(paseador.getDirecfoto()).apply(RequestOptions.circleCropTransform()).into(fotopas);
                final String nombrepaseador = paseador.getNombre();
                nombpas.setText( (getResources().getString(R.string.Paseadorr))+nombrepaseador );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        crono = v.findViewById( R.id.crono_crono );
        crono.setVisibility( View.INVISIBLE );




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
                new CountDownTimer(30000, 5000) {

                    public void onTick(long millisUntilFinished) {
                        System.out.println("tick");
                        //Log.e("seconds remaining: ","" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        final DatabaseReference estatus_paseo =  FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("estatusPaseo");
                        Query oo = estatus_paseo;
                        oo.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot issue4 : dataSnapshot.getChildren()) {
                                    Estatus estatus = dataSnapshot.getValue( Estatus.class );
                                    System.out.println( estatus.getEstatus());
                                    String estatusString = estatus.getEstatus();
                                    if (estatusString.equals( (getResources().getString(R.string.Progreso)) )){
                                        onMapReady(gMap);
                                    }
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

                final DatabaseReference actualRef = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id).child("ubicacionActual");
                Query queryActual = actualRef.orderByChild("ubicacion");
                queryActual.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        actualRef.removeEventListener( this );
                        Double lati = 0.0;
                        Double longi = 0.0;

                        try {
                            mCurrentLocationMarker.remove();
                        }catch (Exception e){}



                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ModeloTracking ubicacionActual = snapshot.getValue(ModeloTracking.class);
                            lati = ubicacionActual.getLatitud();
                            longi = ubicacionActual.getLongitud();

                            mCurrentLocationMarker = gMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lati, longi)).icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_CYAN)).title( (getResources().getString(R.string.Paseador)) ));


                        }



                        final DatabaseReference refUsuario = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id);
                        Query queryUbicUsuario = refUsuario;

                        queryUbicUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double latit = 0.0;
                                Double longi = 0.0;

                                try {

                                } catch (Exception e){
                                }

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                    ModeloTracking ubicacionUsuario = dataSnapshot.getValue(ModeloTracking.class);
                                    latit = ubicacionUsuario.getLatitud();
                                    longi = ubicacionUsuario.getLongitud();
                                    gMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(latit, longi)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title( (getResources().getString(R.string.Punto_de_recogida))));
                                    gMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng( latit,longi ), 16.0f));
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
                        gMap.setMyLocationEnabled(true);


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
                                gMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lattrack, lontrack)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).title((getResources().getString(R.string.Paseador)) ));

                                final DatabaseReference refUsuario = FirebaseDatabase.getInstance().getReference("Paseos_usuarios").child(user.getUid()).child(order_id);
                                Query queryUbicUsuario = refUsuario;

                                queryUbicUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Double latit = 0.0;
                                        Double longi = 0.0;

                                        try {

                                        } catch (Exception e){
                                        }

                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                            ModeloTracking ubicacionUsuario = dataSnapshot.getValue(ModeloTracking.class);
                                            latit = ubicacionUsuario.getLatitud();
                                            longi = ubicacionUsuario.getLongitud();
                                            gMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(latit, longi)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title( (getResources().getString(R.string.Punto_de_recogida))));
                                            //gMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng( latit,longi ), 16.0f));
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                PolylineOptions pl = new PolylineOptions();
                                pl.addAll(point);
                                pl.width(15).color(getResources().getColor(R.color.azul_caminandog));
                                gMap.addPolyline(pl);

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

    public static boolean isLocationEnabled(Context context){
        int LocationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                LocationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e){
                e.printStackTrace();
            }
            return LocationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        }
        return !TextUtils.isEmpty(locationProviders);
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

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker.remove();
        }

        if (isFirstTime) {
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            isFirstTime = false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi( LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext (),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity (),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(getContext ())
                        .setTitle((getResources().getString(R.string.Permiso_de_ubicacion)))
                        .setMessage((getResources().getString(R.string.Desea_activar_su_ubicacion)))
                        .setPositiveButton((getResources().getString(R.string.Aceptar)), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton((getResources().getString(R.string.Cancelar)), new DialogInterface.OnClickListener() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        gMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), (getResources().getString(R.string.Permiso_negado)), Toast.LENGTH_LONG).show();
                }
                return;
            }
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
}
