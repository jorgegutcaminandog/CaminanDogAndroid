package mx.com.caminandog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;


public class Account extends AppCompatActivity  {
    String dia;
    String mes;
    String anio;

    private ImageButton mUpload;
    private ProgressDialog mprogress;
    String inserturl;
    private static final int GALLERY_INTENT = 1;
    private StorageReference mstorach;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView name,appat,telefono;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.Completa_el_registro));

        final TextView name = (TextView) findViewById(R.id.txt_name);
        final TextView appat = (TextView) findViewById(R.id.txt_app_pat);
        final TextView apmat = (TextView) findViewById(R.id.txt_app_mat);
        TextView email = (TextView) findViewById(R.id.txt_email);
        final Spinner dia_sp = (Spinner) findViewById(R.id.sp_dia);
        final Spinner mes_sp = (Spinner) findViewById(R.id.sp_mes);
        final Spinner anio_sp = (Spinner) findViewById(R.id.sp_ano);
        final TextView telefono = (TextView) findViewById(R.id.telefono);
        final TextView telefono2 = (TextView) findViewById(R.id.txt_telefono2);


        Spinner spinnerdia = findViewById(R.id.sp_dia);
        Spinner spinnermes = findViewById(R.id.sp_mes);
        Spinner spinneranio = findViewById(R.id.sp_ano);

        final ArrayAdapter<CharSequence> adapterdia = ArrayAdapter.createFromResource(this, R.array.dias, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adaptermes = ArrayAdapter.createFromResource(this, R.array.meses, android.R.layout.simple_spinner_item);
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1925; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        final ArrayAdapter<String> adapteranio = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        adapterdia.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        adaptermes.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerdia.setAdapter(adapterdia);
        spinnerdia.setOnItemSelectedListener(new DiasSpinnerClass());

        spinnermes.setAdapter(adaptermes);
        spinnermes.setOnItemSelectedListener(new MesesSpinnerClass());

        spinneranio.setAdapter(adapteranio);
        spinneranio.setOnItemSelectedListener(new AniosSpinnerClass());

        //upload image
        mUpload = (ImageButton) findViewById(R.id.profile_img);
        mstorach = FirebaseStorage.getInstance().getReference();
        mprogress = new ProgressDialog(this);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        //

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference(FirebaseReferences.USER_REFERERENCE);
        //final DatabaseReference cards_ref = database.getReference(FirebaseReferences.CARDS_REFERERENCE).child( user.getUid() );



        ImageView profile_picture = (ImageView) findViewById(R.id.profile_img);

        Button enviar = (Button) findViewById(R.id.btn_enviar_reg);

        Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profile_picture);

        //name.setText(user.getDisplayName());
        email.setText(user.getEmail());


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        System.out.println(user.getDisplayName()+" "+user.getPhoneNumber());


        final Query query = users_ref.child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    name.setText(usuario.getNombre());

                    UserProfileChangeRequest.Builder nameUpdate = new UserProfileChangeRequest.Builder();
                    nameUpdate.setDisplayName(usuario.getNombre());

                    appat.setText(usuario.getApellido_Paterno());
                    apmat.setText(usuario.getApellido_Materno());
                    dia_sp.setSelection(adapterdia.getPosition(usuario.getDia()));
                    mes_sp.setSelection(adaptermes.getPosition(usuario.getMes()));
                    anio_sp.setSelection(adapteranio.getPosition(usuario.getAnio()));
                    //dia.setText(usuario.getDia());
                    //mes.setText(usuario.getMes());
                    //anio.setText(usuario.getAnio());
                    telefono.setText(usuario.getTelefono1());
                    telefono2.setText(usuario.getTelefono2());

                    for (DataSnapshot issue2: dataSnapshot.getChildren()){

                    }
                }else {

                    name.setText(user.getDisplayName());
                    telefono.setText(user.getPhoneNumber());



                    //Usuario usuario = new Usuario(user.getUid(),user.getEmail(),name.getText().toString(),appat.getText().toString(),apmat.getText().toString(),dia,mes,anio,calle.getText().toString(),cp.getText().toString(),noext.getText().toString(),noint.getText().toString(),delegacion.getText().toString(),estado.getText().toString(),telefono.getText().toString(),telefono2.getText().toString());
                    //users_ref.push().setValue(usuario);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Usuario usuario = new Usuario(user.getUid(),user.getEmail(),name.getText().toString(),appat.getText().toString(),apmat.getText().toString(),dia,mes,anio,telefono.getText().toString(),telefono2.getText().toString());

                final Query query = users_ref.orderByChild("uid").equalTo(user.getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            if(name.getText().toString().equals( "" )||appat.getText().toString().equals( "" )||email.getText().toString().equals( "" )||telefono.getText().toString().equals( "" )||telefono.getText().toString().length()<10){
                                if (name.getText().toString().equals( "" )){
                                    name.setError(getResources().getString(R.string.Por_favor_llena_este_campo));
                                    Toast.makeText(getApplicationContext(),(getResources().getString(R.string.Nombre_Requerido)) , Toast.LENGTH_LONG).show();

                                }else if (appat.getText().toString().equals( "" )){
                                    appat.setError(getResources().getString(R.string.Por_favor_llena_este_campo));
                                    Toast.makeText(getApplicationContext(),(getResources().getString(R.string.Apellido_Paterno_Requerido)), Toast.LENGTH_LONG).show();

                               // }else if (appat.getText().toString().equals( "" )){
                                    //appat.setError((getResources().getString(R.string.Por_favor_llena_este_campo)));
                                    //Toast.makeText(getApplicationContext(),(getResources().getString(R.string.Apellido_Materno_Requerido)), Toast.LENGTH_LONG).show();//

                                }else if (email.getText().toString().equals( "" )){
                                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Tu_cuenta_pobablemente_no_tiene_un_email_registrado)), Toast.LENGTH_LONG).show();

                                }else if (telefono.getText().toString().equals( "" )){
                                    telefono.setError (getResources().getString(R.string.Por_favor_llena_este_campo));
                                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Telefono_requerido)), Toast.LENGTH_LONG).show();
                                }else if (telefono.getText().toString().length()<10){
                                    telefono.setError (getResources().getString(R.string.Por_favor_utiliza_la_clave_de_area));
                                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Telefono_incorrecto)) , Toast.LENGTH_LONG).show();
                                }

                                return;
                            }else{
                                for (DataSnapshot issue: dataSnapshot.getChildren()){
                                    String key = issue.getKey();
                                    //users_ref.child(key).setValue(usuario);
                                    users_ref.child(key).child("nombre").setValue(name.getText().toString());
                                    users_ref.child(key).child("apellido_Paterno").setValue(appat.getText().toString());
                                    users_ref.child(key).child("apellido_Materno").setValue(apmat.getText().toString());
                                    users_ref.child(key).child("telefono1").setValue(telefono.getText().toString());
                                    users_ref.child(key).child("telefono2").setValue(telefono2.getText().toString());
                                    users_ref.child(key).child("dia").setValue(dia);
                                    users_ref.child(key).child("mes").setValue(mes);
                                    users_ref.child(key).child("anio").setValue(anio);

                                    finish();



                                }
                            }






                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //startActivity(new Intent(Account.this, Account_No_Editable.class));

            }
        });


    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //if(name.getText().toString().equals( "" )||appat.getText().toString().equals( "" )||telefono.getText().toString().equals( "" )) {
               // Toast.makeText(getApplicationContext(), "Imagen Actualizada Exitosamente", Toast.LENGTH_LONG).show();

            //}
            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setMessage("¿Estas seguro de querer salir? En paseos activos podras ver los paseos de tus peludhijos que estan activos.")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }



    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK);{
            mprogress.setTitle(getResources().getString(R.string.Espera_un_momento));
            mprogress.setMessage(getResources().getString(R.string.Subiendo_foto));
            mprogress.setCancelable(false);
            mprogress.show();
            Uri uri = data.getData();
            final StorageReference filepath = mstorach.child("profile_images").child(user.getUid()+".jpeg");
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mprogress.dismiss();
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    Glide.with(getApplicationContext()).load(downloadUrl).apply(RequestOptions.circleCropTransform()).into(mUpload);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUrl)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Imagen_Actualizada_Exitosamente)) , Toast.LENGTH_LONG).show();

                                    }
                                }
                            });


                    Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Imagen_Subida_Exitosamente)), Toast.LENGTH_LONG).show();
                }
            });
        }
    }




    class DiasSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            dia = parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }
    }

    class MesesSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mes = parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class AniosSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            anio = parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private Runnable responseRunnable(final String responseStr){
        Runnable resRunnable = new Runnable(){
            public void run(){
                Toast.makeText(getApplication()
                        ,responseStr,
                        Toast.LENGTH_SHORT).show();
            }
        };
        return resRunnable;
    }

    @Override
    public void onBackPressed() {


            Toast.makeText(getApplicationContext(), (getResources().getString(R.string.Para_brindarte_un_mejor_servicio_es_necesario_llenar_algunos_datos)), Toast.LENGTH_LONG).show();










        //super.onBackPressed();
    }
}
