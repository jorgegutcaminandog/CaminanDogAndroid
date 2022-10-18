package mx.com.caminandog;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputPassword2;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        getSupportActionBar().hide();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPassword2 = (EditText) findViewById(R.id.password2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String password2 = inputPassword2.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {

                    inputEmail.setError(getResources().getString(R.string.Ingresa_un_email));
                    return;
                }

                if (TextUtils.isEmpty(password)) {

                    inputPassword.setError(getResources().getString(R.string.Ingresa_una_contraseña));
                    return;
                }

                if (TextUtils.isEmpty(password2)) {

                    inputPassword2.setError(getResources().getString(R.string.Repite_tu_contraseña));
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError(getString(R.string.minimum_password));
                    return;
                }

                if (!password.equals(password2)){
                    inputPassword2.setError(getResources().getString(R.string.Las_contraseñas_no_coinciden));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                try {

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, (getResources().getString(R.string.Usuario_creado_exitosamente)) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, (getResources().getString(R.string.Algo_fallo)) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Registro_creado_exitosamente)), Toast.LENGTH_SHORT).show();
                                    auth.getCurrentUser().sendEmailVerification();
                                    //Toast.makeText(getApplicationContext(), "Registro creado exitosamente", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Hemos_enviado_un_email_para_que_actives_tu_cuenta)), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });

                    /*auth.createUserWithEmailAndPassword(email,password);
                    auth.getCurrentUser().sendEmailVerification();
                    Toast.makeText(getApplicationContext(), "Registro creado exitosamente", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Hemos enviado un email para que actives tu cuenta", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();*/



                }catch ( Exception e){
                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Hubo_un_problema_al_crear_el_nuevo_registro)), Toast.LENGTH_SHORT).show();
                }

                /*auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "registro creado exitosament", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });*/

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
