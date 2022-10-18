package mx.com.caminandog;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;


public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, Contenedor_Fragment.OnFragmentInteractionListener,
        No_edit_Account_Frgmnt.OnFragmentInteractionListener,Cards_Fragment.OnFragmentInteractionListener,
        Historic_Paseo_Fragmnt.OnFragmentInteractionListener,Inicio_Fragment.OnFragmentInteractionListener,
        Tacking_map.OnFragmentInteractionListener, Paseos_Activos_Fragment.OnFragmentInteractionListener,
        ContenedorRecuperandog_Fragment.OnFragmentInteractionListener,
        Perrhijos_Recuperandog_Fragment.OnFragmentInteractionListener,Recuperandog_Informacion_Fragment.OnFragmentInteractionListener,
        Historic_Map.OnFragmentInteractionListener, ContenedorActivos.OnFragmentInteractionListener,
        Codigos_Fragment.OnFragmentInteractionListener{
    Button  btn_Logout;
    private GoogleApiClient googleApiClient;

    RecyclerView rv;
    private RecyclerView mPeopleRV;
    //Tacking_map.adaptador adapter;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<Chat, MainActivity.PetsViewHolder> mPeopleRVAdapter;



    FragmentManager fragmentManager=getSupportFragmentManager();

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    public final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //AppEventsLogger.activateApp(this);
        setAutoLogAppEventsEnabled(true);

        FirebaseCrashlytics.getInstance().setUserId(user.getUid());
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);






        /*Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });


        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/


        Perros_Adapter cls2 = new Perros_Adapter();
        cls2.ReqPerros();

        System.out.println( ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.guau);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));





        final DatabaseReference usersult_ref = database.getReference( FirebaseReferences.USER_REFERERENCE ).child( user.getUid() );









        //googlesigin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //endgooglesign




        auth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    /*if (user.getEmail().equals("")){
                        System.out.println("no hay correo: "+user.getEmail());
                    }else {
                        System.out.println("si hay correo: "+user.getEmail()+" verified? "+user.isEmailVerified()+" <<<>>> "+user.getProviders());

                    }*/
                    //Toast.makeText(getApplicationContext(), "Usr Loggeed In", Toast.LENGTH_SHORT).show();
                    //if (user.isEmailVerified()){Toast.makeText(getApplicationContext(), "verificado", Toast.LENGTH_SHORT).show();}
                    //else {Toast.makeText(getApplicationContext(), "No verificado", Toast.LENGTH_SHORT).show();
                    //user.sendEmailVerification();
                       // }

                }else {startActivity(new Intent(MainActivity.this, Login_Options.class));}
            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //space to configure the nav drawer

        //

        //space to configure and send the elements to the nav drawer ex. photo email etc
        if (user != null){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener( new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()){
                                System.out.println("fallo instance");
                                return;
                            }

                            try {
                                usersult_ref.child( "ultima_vez" ).setValue( ServerValue.TIMESTAMP );
                                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                String version = pInfo.versionName;
                                usersult_ref.child( "through" ).setValue("Android "+version);

                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    //Log.w(TAG, "getInstanceId failed", task.getException());
                                                    return;
                                                }

                                                // Get new Instance ID token
                                                String token = task.getResult().getToken();

                                                usersult_ref.child( "token" ).child("token").setValue(token);


                                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                usersult_ref.child( "through" ).setValue("Android // no se pudo leer version");
                            }

                            //String token = task.getResult().getToken();
                            //System.out.println("tokenmsj "+token);
                            //usersult_ref.child( "token" ).child("token").setValue( token );

                        }
                    } );
            View headerView = navigationView.getHeaderView(0);
            TextView navEmail = (TextView) headerView.findViewById(R.id.subnav);
            navEmail.setText(user.getEmail());

            TextView navUsername = (TextView) headerView.findViewById(R.id.titnav);
            navUsername.setText(user.getDisplayName());

            ImageButton navimage= (ImageButton) headerView.findViewById(R.id.userphoto);
            //Glide.with(this).load(user.getPhotoUrl()).into(navimage);
            Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(navimage);

            navimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    fragmentManager.beginTransaction().replace(R.id.contenedor,new Contenedor_Fragment()).addToBackStack(null).commit();
                    getSupportActionBar().setTitle((getResources().getString(R.string.Mi_Cuenta)));

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    //.clearAnimation();

                    /*int size = navigationView.getMenu().size();
                    for (int i = 0; i < size; i++) {
                        navigationView.getMenu().getItem(i).setChecked(false);
                    }*/

                   // startActivity(new Intent(MainActivity.this, Contenedor_Fragment.class));vjh

                }
            });

        }
        else {

        }
        //

        /*btn_Logout = (Button) findViewById(R.id.signout_btn);
        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                            goLogInScreen();
                        }else {
                            Toast.makeText(getApplicationContext(),"No se pudo revocar el acceso",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                //startActivity(new Intent(MainActivity.this, Login_Options.class));
            }
        });*/
        if( getIntent().getExtras() != null) {
            System.out.println("intent no null");
            switch (getIntent().getStringExtra("EXTRA")){
                case "openFragment":
                    String ord_id_func = getIntent().getStringExtra("order_id");
                    String fotopas = getIntent().getStringExtra("fotopaseador");
                    String nompas = getIntent().getStringExtra("nombrepaseador");

                    FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
                    ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    Tacking_map fragment2 = new Tacking_map();

                    Bundle bundle = new Bundle();

                    bundle.putString( "order_id",ord_id_func);
                    bundle.putString( "fotopaseador",fotopas);
                    bundle.putString( "nombrepaseador",nompas );
                    //bundle.putString( "order_id",model.g );
                    fragment2.setArguments(bundle);

                    ft.replace(R.id.contenedor, fragment2);
                    ft.addToBackStack(null);
                    ft.commit();

                    //getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,new Paseos_Activos_Fragment()).commit();
                    //fragmentManager.beginTransaction().replace(R.id.contenedor,new Paseos_Activos_Fragment()).commit();
                    System.out.println("case");

                    break;
                case "NotifChat":

                    /*FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor,new Inicio_Fragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_inicio);
                    getSupportFragmentManager().executePendingTransactions();
                    Inicio_Fragment fragment = (Inicio_Fragment) getSupportFragmentManager().getPrimaryNavigationFragment();
                    fragment.ChatList();*/

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.contenedor, new Inicio_Fragment(), "iniciofrag");
                    fragmentTransaction.commit();
                    getSupportFragmentManager().executePendingTransactions();

                    Inicio_Fragment fr = (Inicio_Fragment) getSupportFragmentManager().findFragmentByTag("iniciofrag");
                    fr.ChatList();



                    //ChatFragment();
                    break;
            }

        }else {
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Inicio_Fragment()).commit();
            navigationView.setCheckedItem(R.id.nav_inicio);
        }

        //x();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.Estas_seguro_de_querer_salir))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.Si), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //FragmentManager fragmentManager=getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Inicio_Fragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Principal));
        } else if (id == R.id.nav_perros) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new MisPerros_Fragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Perrhijos));
        } else if (id == R.id.nav_paseadores) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Cards_Fragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Tarjetas));
        } else if (id == R.id.nav_codigos) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Codigos_Fragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Codigos));
        } else if (id == R.id.nav_tarifas) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Tarifas_Fragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Tarifas));
        } else if (id == R.id.nav_hotel) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new AliadosFragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Aliados));
        }else if (id == R.id.nav_pasact){
            fragmentManager.beginTransaction().replace(R.id.contenedor,new ContenedorActivos()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.Paseos));
        }else if (id == R.id.nav_recuperandog){
            fragmentManager.beginTransaction().replace(R.id.contenedor,new ContenedorRecuperandog_Fragment()).commit();
            getSupportActionBar().setTitle("RecuperanDog");
        }
        /*else if (id == R.id.nav_mobica) {
            String url = "https://www.mobica.com.mx/pets";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }*/
        else if (id == R.id.nav_acercade) {
            /*String url = "https://caminandog.com.mx/contacto.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);*/
            ChatFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;




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
        View customView = inflater.inflate(R.layout.dialog_chat_soporte, null);
// Build the dialog
        customDialog = new Dialog(this, R.style.CustomDialog);
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
                Glide.with(MainActivity.this).load(configChat.getFoto()).apply(RequestOptions.circleCropTransform()).into(fotopas_chat);
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


        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        mPeopleRV.setLayoutManager(mLayoutManager);
        mPeopleRV.setNestedScrollingEnabled(false);
        mPeopleRV.getRecycledViewPool().setMaxRecycledViews(0, 0);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(personsQuery, Chat.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Chat, MainActivity.PetsViewHolder>(personsOptions) {
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
                    String date = DateFormat.format("hh:mm a", cal).toString();
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
                    String date = DateFormat.format("hh:mm a", cal).toString();
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
                        Toast.makeText(MainActivity.this, (getResources().getString(R.string.Error)), Toast.LENGTH_LONG).show();
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
                            MainActivity.this.runOnUiThread(new Runnable() {
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
                                    Toast.makeText(MainActivity.this,
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {

        super.onStart();

        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null){
            auth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();



            //Toast.makeText(getApplicationContext(),account.getPhotoUrl().toString(),Toast.LENGTH_SHORT).show();
            //Log.d("foto",account.getPhotoUrl().toString());

        }else {
            goLogInScreen();
        }
    }

    public void logOut(View view   ){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    goLogInScreen();
                }else {
                    Toast.makeText(getApplicationContext(),(getResources().getString(R.string.No_se_pudo_cerrar_sesion)),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void revoke(View view){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    goLogInScreen();
                }else {
                    Toast.makeText(getApplicationContext(),(getResources().getString(R.string.No_se_pudo_revocar_el_acceso)),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Options.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    private void x(){
        DatabaseReference ref = database.getReference( "sessions" ).child( "actual" );

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Long hr_act = (Long) snapshot.getValue();
                if (1594835756000L<hr_act){
                    finish();
                }
                System.out.println("x "+hr_act);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.setValue(ServerValue.TIMESTAMP);
    }



}
