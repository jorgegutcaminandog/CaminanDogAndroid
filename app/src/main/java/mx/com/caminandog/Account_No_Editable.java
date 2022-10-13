package mx.com.caminandog;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Account_No_Editable extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    Button  btn_Logout;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__no__editable);
        getSupportActionBar().hide();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference(FirebaseReferences.USER_REFERERENCE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




        ImageView profile_picture = (ImageView) findViewById(R.id.up_foto);
        final TextView name = (TextView) findViewById(R.id.txt_name);
        final TextView appat = (TextView) findViewById(R.id.txt_app_pat);
        final TextView apmat = (TextView) findViewById(R.id.txt_app_mat);
        TextView email = (TextView) findViewById(R.id.txt_email);
        final TextView dia = (TextView) findViewById(R.id.dia);
        final TextView mes = (TextView) findViewById(R.id.mes);
        final TextView anio = (TextView) findViewById(R.id.txt_ano);
        final TextView telefono = (TextView) findViewById(R.id.telefono);
        final TextView telefono2 = (TextView) findViewById(R.id.txt_telefono2);
        Button editar = (Button) findViewById(R.id.edit_float_btn);



        Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profile_picture);
        //name.setText(user.getDisplayName());
        email.setText(user.getEmail());


       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final Query query = users_ref.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot issue2: dataSnapshot.getChildren()){
                        Usuario usuario = issue2.getValue(Usuario.class);
                        name.setText(usuario.getNombre());
                        appat.setText(usuario.getApellido_Paterno());
                        apmat.setText(usuario.getApellido_Materno());
                        dia.setText(usuario.getDia());
                        mes.setText(usuario.getMes());
                        anio.setText(usuario.getAnio());
                        telefono.setText(usuario.getTelefono1());
                        telefono2.setText(usuario.getTelefono2());
                    }
                }else {
                    Usuario usuario = new Usuario(user.getUid(),user.getEmail(),name.getText().toString(),appat.getText().toString(),apmat.getText().toString(),dia.getText().toString(),mes.getText().toString(),anio.getText().toString(),telefono.getText().toString(),telefono2.getText().toString());
                    users_ref.push().setValue(usuario);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account_No_Editable.this, Account.class));
                //finish();

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btn_Logout = (Button) findViewById(R.id.float_logout);
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
                            Toast.makeText(getApplicationContext(), (getResources().getString(R.string.No_se_pudo_revocar_el_acceso)),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                //startActivity(new Intent(MainActivity.this, Login_Options.class));
            }
        });

    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Options.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}





