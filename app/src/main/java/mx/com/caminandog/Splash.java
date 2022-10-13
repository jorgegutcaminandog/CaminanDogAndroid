package mx.com.caminandog;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.CircularArray;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class Splash extends AppCompatActivity {

    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 2500; // 3 segundos
    boolean intnt;

    private VideoView mVideoView;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //quitar barra de notificacion
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //fin
        getSupportActionBar().hide();


        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_splash);




        /*if( getIntent().getExtras() != null && getIntent().getExtras().getString("EXTRA").equals("NotifChat")) {
            intnt=true;
        }*/


        //video

        VideoView videoView = (VideoView) findViewById(R.id.videoinit);

        Uri path = Uri.parse("android.resource://mx.com.caminandog/" + R.raw.splashvid);


        videoView.setVideoURI(path);
        videoView.start();



        //end video

        new Handler().postDelayed(new Runnable(){
            public void run(){


// Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                if (user != null){

                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                    finish();







                    /*SharedPreferences paymnt_json = getSharedPreferences("PREFS",0);
                    SharedPreferences.Editor editor = paymnt_json.edit();
                    String json = paymnt_json.getString("jsonPago", "");

                    if (!json.isEmpty()) {
                        Intent intentpago = new Intent(Splash.this, Tracking.class);
                        startActivity(intentpago);
                        finish();
                    }*/



                }else {
                    Intent intent = new Intent(Splash.this, Login_Options.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                    finish();
                }

                /*Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/


            };
        }, DURACION_SPLASH);


    }
}
