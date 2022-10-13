package mx.com.caminandog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class Login_Options extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btnprop;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private GoogleApiClient googleApiClient;

    private SignInButton signInButton;

    public static final int SIGN_IN_CODE = 777;

    private static final int REQUEST= 112;


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressBar progressBar;
    private ImageView options_img;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login__options);
        getSupportActionBar().hide();

        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        window.setStatusBarColor(getColor(R.color.colorPrimary));*/

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




        Button btn_qr = (Button)findViewById(R.id.button_qr);
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
                    String[] PERMISSIONS = {android.Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS
                    };


                    if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                        Log.d("TAG","@@@ IN IF hasPermissions");
                        ActivityCompat.requestPermissions((Login_Options.this) , PERMISSIONS, REQUEST );
                    } else {
                        Log.d("TAG","@@@ IN ELSE hasPermissions");
                        Intent intent = new Intent(Login_Options.this, Lector_Activity.class);
                        overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                        startActivity(intent);
                    }
                } else {
                    Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
                    Intent intent = new Intent(Login_Options.this, Lector_Activity.class);
                    overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                    startActivity(intent);
                }

                /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA)) ;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                } else {


                }*/

                //finish();

            }
        });

        //final ImageView options_img = (ImageView) findViewById(R.id.img_options);
        options_img = (ImageView) findViewById(R.id.img_options);
        Glide.with(getApplicationContext()).load(R.raw.ojitos).into(options_img);

        //facebook
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.btn_fb_signin);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "cancel login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Ocurrio_un_error_al_tratar_de_ingresar)), Toast.LENGTH_SHORT).show();

            }
        });


        //facebook end


        //firebase prop
        //auth = FirebaseAuth.getInstance();

        btnprop = (Button) findViewById(R.id.btn_propio);
        btnprop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Options.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        //end firebase prop

        Button politica = (Button)findViewById( R.id.btn_uso );
        politica.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://caminandog.com.mx/politicas.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData( Uri.parse(url));
                startActivity(i);

            }
        } );


        //goolesignin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.btn_google_signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
                progressBar.setVisibility(View.VISIBLE);
                options_img.setVisibility(View.GONE);
            }
        });
        //end google
        //check if someone is logged in



        auth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String email = auth.getCurrentUser().getEmail();
                    if (email == null){
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseAuth.getInstance().signOut();
                                                            LoginManager.getInstance().logOut();

                                                        }
                                                    }
                                                });
                                    }
                                });


                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder ( Login_Options.this );
                        builder.setCancelable(false);
                        builder.setTitle ( (getResources().getString(R.string.Acceso_denegado)));
                        builder.setMessage ( (getResources().getString(R.string.Tu_cuenta_de_Facebook_no_tiene_email)));
                        builder.setPositiveButton ( (getResources().getString(R.string.Aceptar)),new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog ,int which) {
                                startActivity(new Intent(Login_Options.this, SignupActivity.class));
                                //finish();
                            }
                        } );


                        builder.show ();
                    }else{
                        goMainScreen();
                    }

                }
            }
        };





        /*if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login_Options.this, MainActivity.class));
            finish();
        }*/

        //end check

        auth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };





    }//oncreate closure



    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        options_img.setVisibility(View.GONE);

        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        System.out.println(credential);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Probablemente_la_direccion_de_correo_vinculada_a_tu_cuenta_de_facebook)), Toast.LENGTH_LONG).show();
                    LoginManager.getInstance().logOut();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                options_img.setVisibility(View.VISIBLE);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            auth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }



    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
            //GoogleSignInAccount account = result.getSignInAccount();
            //goMainScreen();
            progressBar.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, (getResources().getString(R.string.No_se_pudo_iniciar_sesion)), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            options_img.setVisibility(View.VISIBLE);

        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
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
                    Intent intent = new Intent(Login_Options.this, Lector_Activity.class);
                    overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                    startActivity(intent);
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Permisos_Denegados)), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),(getResources().getString(R.string.Caminandog_podria_no_funcionar_correctamente)) , Toast.LENGTH_LONG).show();
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

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
