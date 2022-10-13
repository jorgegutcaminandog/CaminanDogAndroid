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
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;



public class MisPerros_Fragment extends Fragment {
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Perro, MisPerros_Fragment.PetsViewHolder> mPeopleRVAdapter;
    String edad;
    String comportamiento;
    String padecimiento;
    String raza;
    String desparasitacion,primeraVac,segundaVac,terceraVac;
    private static final int GALLERY_INTENT = 1;
    private static final int TAKE_PICTURE = 2;
    private StorageReference mstorach;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageButton mUpload;
    private ProgressDialog mprogress;
    String inserturl;
    String oo;
    private ProgressBar progressBar;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    String perro_id = "";
    DatabaseReference personsRef1;
    Uri uri;
    byte[] data_pick;
    String currentPhotoPath;



    Spinner spinnerraza;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Perrosview = inflater.inflate(R.layout.fragment_mis_perros_, container, false);

        progressBar = (ProgressBar) Perrosview.findViewById(R.id.progressBar6);


        ImageButton mShowDialog = (ImageButton) Perrosview.findViewById(R.id.btn_float_add);


        Button show = (Button) Perrosview.findViewById( R.id.show_instruc_perros );
        TextView intruc = (TextView) Perrosview.findViewById( R.id.instruc_txt_perros );
        intruc.setVisibility( View.INVISIBLE );
        show.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intruc.getVisibility() == View.VISIBLE) {
                    intruc.setVisibility( View.INVISIBLE );
                } else {
                    intruc.setVisibility( View.VISIBLE );
                }

            }

        } );

        // start add dog

        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //inserturl="";
                mBuilder = new AlertDialog.Builder(getContext());
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
                mprogress = new ProgressDialog(getContext());
                eliminar.setVisibility(View.GONE);

                mUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                getActivity());
                        myAlertDialog.setTitle((getResources().getString(R.string.Sube_una_foto)));
                        myAlertDialog.setMessage((getResources().getString(R.string.Como_quieres_realizar_esta_accion)));

                        myAlertDialog.setPositiveButton((getResources().getString(R.string.Galeria)),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        PickPicktureActivityResultLauncher.launch(intent);


                                    }
                                });

                        myAlertDialog.setNegativeButton((getResources().getString(R.string.Camara)),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        GetCameraPermission();



                                    }
                                });
                        myAlertDialog.show();
                        //mUpload.setVisibility(View.GONE);
                        //oo = model.getIdPerro();



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


                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        TextView name = (TextView) mView.findViewById(R.id.txt_name_adddog);
                        String name_insert = name.getText().toString();

                        if (!name_insert.equals("")){
                            mprogress.setTitle((getResources().getString(R.string.Espera_un_momento)));
                            mprogress.setMessage((getResources().getString(R.string.Subiendo_foto)));
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
                            perro.put("padecimiento", padecimiento);
                            perro.put("raza", raza);
                            perro.put("idPerro", perro_id);
                            perro.put("foto", "https://firebasestorage.googleapis.com/v0/b/caminandog-218818.appspot.com/o/perfil%20perro%403x.png?alt=media&token=ee2198f5-f38b-4a40-8857-784b01b06072");
                            perro.put("vacunas", vacunas);


                            personsRef1.child(user.getUid()).child(perro_id).updateChildren(perro).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    if (uri==null && data_pick == null){
                                        //personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/caminandog-218818.appspot.com/o/perfil%20perro%403x.png?alt=media&token=ee2198f5-f38b-4a40-8857-784b01b06072");
                                        mprogress.dismiss();
                                        dialog.dismiss();

                                    }else{

                                        final StorageReference filepath = mstorach.child("FotosPerros").child(user.getUid()).child(perro_id+".jpeg");
                                        if (uri==null){

                                            UploadTask uploadTask = filepath.putBytes(data_pick);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    System.out.println((getResources().getString(R.string.No_se_pudo_subir_el_archivo)));
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


                        }else{
                            name.setError((getResources().getString(R.string.Este_campo_no_puede_estar_vacio)));
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
                final Spinner spinner1ra = mView.findViewById(R.id.sp_1radosis);
                final Spinner spinner2da = mView.findViewById(R.id.sp_2dadosis);
                final Spinner spinner3ra = mView.findViewById(R.id.sp_3radosis);

                final ArrayAdapter<CharSequence> adapteredad = ArrayAdapter.createFromResource(getContext(), R.array.edad, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapterraza = ArrayAdapter.createFromResource(getContext(), R.array.razas, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapterpadec = ArrayAdapter.createFromResource(getContext(), R.array.padecimiento, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adaptercomp = ArrayAdapter.createFromResource(getContext(), R.array.comportamiento, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adaptersino = ArrayAdapter.createFromResource(getContext(), R.array.sino, android.R.layout.simple_spinner_item);

                adapteredad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adapterraza.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adapterpadec.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adaptercomp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                adaptersino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinneredad.setAdapter(adapteredad);
                spinneredad.setOnItemSelectedListener(new MisPerros_Fragment.EdadSpinnerClass());

                spinnerraza.setAdapter(adapterraza);
                spinnerraza.setOnItemSelectedListener(new MisPerros_Fragment.RazaSpinnerClass());

                spinnercomportamiento.setAdapter(adaptercomp);
                spinnercomportamiento.setOnItemSelectedListener(new MisPerros_Fragment.ComportamientoSpinnerClass());

                spinnerpadecimiento.setAdapter(adapterpadec);
                spinnerpadecimiento.setOnItemSelectedListener(new MisPerros_Fragment.PadecimientoSpinnerClass());

                spinnerdespara.setAdapter(adaptersino);
                spinnerdespara.setOnItemSelectedListener(new MisPerros_Fragment.DesparaSpinnerClass());

                spinner1ra.setAdapter(adaptersino);
                spinner1ra.setOnItemSelectedListener(new MisPerros_Fragment.PrimeraSpinnerClass());

                spinner2da.setAdapter(adaptersino);
                spinner2da.setOnItemSelectedListener(new MisPerros_Fragment.SegundaSpinnerClass());

                spinner3ra.setAdapter(adaptersino);
                spinner3ra.setOnItemSelectedListener(new MisPerros_Fragment.TerceraSpinnerClass());



                //Perro perro = new Perro(user.getUid(),comportamiento.getText().toString(),edad.getText().toString(),name.getText().toString(),padecimiento.getText().toString(),raza.getText().toString(),foto.getText().toString());






            }
        });


        //finish add dog

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Perros");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) Perrosview.findViewById(R.id.recper1);



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Perros").child(user.getUid());

        Query personsQuery = personsRef.orderByKey();




        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getContext()));



        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Perro>().setQuery(personsQuery, Perro.class).build();

        //System.out.println(" ffvfvf "+personsOptions.getSnapshots());
        if(personsOptions.getSnapshots().isEmpty()){
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
        }

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Perro, MisPerros_Fragment.PetsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(MisPerros_Fragment.PetsViewHolder holder, final int position, final Perro model) {
                progressBar.setVisibility(View.VISIBLE);
                holder.setTitle(model.getNombre());
                holder.setDesc(model.getComportamiento());
                holder.setImage(getActivity().getBaseContext(), model.getFoto());
                holder.setRaza(model.getRaza());
                holder.setEdad(model.getEdad());
                holder.setPadec(model.getPadecimiento());
                progressBar.setVisibility(View.GONE);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_add_perro, null);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        mUpload = (ImageButton) mView.findViewById(R.id.up_foto);

                        Glide.with(getContext()).load(model.getFoto()).apply(RequestOptions.circleCropTransform()).into(mUpload);


                        TextView name = (TextView) mView.findViewById(R.id.txt_name_adddog);
                        TextView edit = (TextView) mView.findViewById(R.id.txt_edit_member);
                        Button editbtn = (Button) mView.findViewById(R.id.btn_send);
                        Button eliminar = (Button) mView.findViewById(R.id.eliminardog_btn);
                        Button cancelar = (Button) mView.findViewById(R.id.btn_cancel_add);
                        name.setText(model.getNombre());

                        edit.setText((getResources().getString(R.string.Editar)));
                        editbtn.setText((getResources().getString(R.string.Editar)));

                        //upload image
                        //mUpload = (ImageButton) mView.findViewById(R.id.up_foto);
                        mstorach = FirebaseStorage.getInstance().getReference();
                        mprogress = new ProgressDialog(getContext());

                        mUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mUpload.setVisibility(View.GONE);
                                oo = model.getIdPerro();

                                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                        getActivity());
                                myAlertDialog.setTitle((getResources().getString(R.string.Sube_una_foto)));
                                myAlertDialog.setMessage((getResources().getString(R.string.Como_quieres_realizar_esta_accion)));

                                myAlertDialog.setPositiveButton((getResources().getString(R.string.Galeria)),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                Intent intent = new Intent(Intent.ACTION_PICK);
                                                intent.setType("image/*");
                                                startActivityForResult(intent,GALLERY_INTENT);


                                            }
                                        });

                                myAlertDialog.setNegativeButton((getResources().getString(R.string.Camara)),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                GetCameraPermission();



                                            }
                                        });
                                myAlertDialog.show();


                            }
                        });

                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //
                                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                                final View mView = getLayoutInflater().inflate(R.layout.dialog_elim_card, null);
                                mBuilder.setView(mView);
                                final TextView resultt = ((TextView) mView.findViewById(R.id.res_txt_elim));
                                final TextView title = ((TextView) mView.findViewById(R.id.title_Txt));
                                final Button btn_res = ((Button) mView.findViewById(R.id.acep_btn_elim));
                                final Button btn_can = ((Button) mView.findViewById(R.id.btn_elim_cancel));
                                final ProgressBar prog = ((ProgressBar) mView.findViewById(R.id.progres_elim));
                                prog.setVisibility( View.INVISIBLE );
                                title.setVisibility( View.INVISIBLE );
                                resultt.setText((getResources().getString(R.string.Estas_seguro_de_eliminar_este_perrhijo)));
                                final AlertDialog dialogg = mBuilder.create();
                                dialogg.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
                                dialogg.show();

                                btn_can.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogg.dismiss();
                                    }
                                } );

                                btn_res.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Perros").child(user.getUid());
                                        final StorageReference filepath = mstorach.child("FotosPerros").child(user.getUid()).child(model.getIdPerro()+".jpeg");

                                        try{
                                            personsRef.child(model.getIdPerro()).removeValue();
                                            filepath.delete();
                                            Toast.makeText(getContext(), (getResources().getString(R.string.Eliminado_exitosamente)), Toast.LENGTH_LONG).show();
                                        } catch (Exception e){Toast.makeText(getContext(), (getResources().getString(R.string.Ocurrio_un_error)), Toast.LENGTH_LONG).show();}


                                        dialog.hide();
                                        dialogg.dismiss();


                                    }
                                } );

                                //
                            }
                        });

                        //

                        dialog.show();

                        final Spinner spinnerraza = mView.findViewById(R.id.sp_raza);
                        final Spinner spinnerpadecimiento = mView.findViewById(R.id.sp_padec);
                        final Spinner spinnercomportamiento = mView.findViewById(R.id.sp_comp);
                        final Spinner spinneredad = mView.findViewById(R.id.sp_edad);
                        final Spinner spinnerdespara = mView.findViewById(R.id.sp_desparasitado);
                        final Spinner spinner1ra = mView.findViewById(R.id.sp_1radosis);
                        final Spinner spinner2da = mView.findViewById(R.id.sp_2dadosis);
                        final Spinner spinner3ra = mView.findViewById(R.id.sp_3radosis);

                        final ArrayAdapter<CharSequence> adapteredad = ArrayAdapter.createFromResource(getContext(), R.array.edad, android.R.layout.simple_spinner_item);
                        final ArrayAdapter<CharSequence> adapterraza = ArrayAdapter.createFromResource(getContext(), R.array.razas, android.R.layout.simple_spinner_item);
                        final ArrayAdapter<CharSequence> adapterpadec = ArrayAdapter.createFromResource(getContext(), R.array.padecimiento, android.R.layout.simple_spinner_item);
                        final ArrayAdapter<CharSequence> adaptercomp = ArrayAdapter.createFromResource(getContext(), R.array.comportamiento, android.R.layout.simple_spinner_item);
                        final ArrayAdapter<CharSequence> adaptersino = ArrayAdapter.createFromResource(getContext(), R.array.sino, android.R.layout.simple_spinner_item);

                        adapteredad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        adapterraza.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        adapterpadec.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        adaptercomp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        adaptersino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                        spinneredad.setAdapter(adapteredad);
                        spinneredad.setOnItemSelectedListener(new MisPerros_Fragment.EdadSpinnerClass());

                        spinnerraza.setAdapter(adapterraza);
                        spinnerraza.setOnItemSelectedListener(new MisPerros_Fragment.RazaSpinnerClass());

                        spinnercomportamiento.setAdapter(adaptercomp);
                        spinnercomportamiento.setOnItemSelectedListener(new MisPerros_Fragment.ComportamientoSpinnerClass());

                        spinnerpadecimiento.setAdapter(adapterpadec);
                        spinnerpadecimiento.setOnItemSelectedListener(new MisPerros_Fragment.PadecimientoSpinnerClass());

                        spinnerdespara.setAdapter(adaptersino);
                        spinnerdespara.setOnItemSelectedListener(new MisPerros_Fragment.DesparaSpinnerClass());

                        spinner1ra.setAdapter(adaptersino);
                        spinner1ra.setOnItemSelectedListener(new MisPerros_Fragment.PrimeraSpinnerClass());

                        spinner2da.setAdapter(adaptersino);
                        spinner2da.setOnItemSelectedListener(new MisPerros_Fragment.SegundaSpinnerClass());

                        spinner3ra.setAdapter(adaptersino);
                        spinner3ra.setOnItemSelectedListener(new MisPerros_Fragment.TerceraSpinnerClass());



                        spinnerraza.setSelection(adapterraza.getPosition(model.getRaza()));
                        spinneredad.setSelection(adapteredad.getPosition(model.getEdad()));
                        spinnercomportamiento.setSelection(adaptercomp.getPosition(model.getComportamiento()));
                        spinnerpadecimiento.setSelection(adapterpadec.getPosition(model.getPadecimiento()));
                        Button editar = (Button) mView.findViewById(R.id.btn_send);

                        editar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                TextView name = (TextView) mView.findViewById(R.id.txt_name_adddog);
                                String name_insert = name.getText().toString();

                                if (!name_insert.equals("")){
                                    mprogress.setTitle((getResources().getString(R.string.Espera_un_momento)));
                                    mprogress.setMessage((getResources().getString(R.string.Subiendo_foto)));
                                    mprogress.setCancelable(false);
                                    mprogress.show();
                                    personsRef1 = FirebaseDatabase.getInstance().getReference().child("Perros");

                                    final Map<String, Object> vacunas = new HashMap<>();
                                    vacunas.put("desparacitado",desparasitacion);
                                    vacunas.put("primeraDosis",primeraVac);
                                    vacunas.put("segundaDosis",segundaVac);
                                    vacunas.put("terceraDosis",terceraVac);


                                    Query query = personsRef.orderByChild("idPerro").equalTo(model.getIdPerro());
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TextView name = (TextView) mView.findViewById(R.id.txt_name_adddog);
                                                personsRef.child(model.getIdPerro()).child("nombre").setValue(name.getText().toString());
                                                personsRef.child(model.getIdPerro()).child("edad").setValue(edad);
                                                personsRef.child(model.getIdPerro()).child("comportamiento").setValue(comportamiento);
                                                personsRef.child(model.getIdPerro()).child("padecimiento").setValue(padecimiento);
                                                personsRef.child(model.getIdPerro()).child("raza").setValue(raza);
                                                personsRef.child(model.getIdPerro()).child("vacunas").setValue(vacunas);


                                                if (uri==null && data_pick == null){
                                                    //personsRef1.child(user.getUid()).child(perro_id).child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/caminandog-218818.appspot.com/o/perfil%20perro%403x.png?alt=media&token=ee2198f5-f38b-4a40-8857-784b01b06072");
                                                    mprogress.dismiss();
                                                    dialog.dismiss();

                                                }else{

                                                    final StorageReference filepath = mstorach.child("FotosPerros").child(user.getUid()).child(model.getIdPerro()+".jpeg");
                                                    if (uri==null){

                                                        UploadTask uploadTask = filepath.putBytes(data_pick);
                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                System.out.println((getResources().getString(R.string.No_se_pudo_subir_el_archivo)));
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

                                                                personsRef1.child(user.getUid()).child(model.getIdPerro()).child("foto").setValue(inserturl);
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

                                                                personsRef1.child(user.getUid()).child(model.getIdPerro()).child("foto").setValue(inserturl);
                                                                uri=null;
                                                                data_pick=null;
                                                                dialog.dismiss();

                                                            }
                                                        });

                                                    }




                                                }
                                            }

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    dialog.hide();

                                }else{
                                    name.setError((getResources().getString(R.string.Este_campo_no_puede_estar_vacio)));
                                }


                            }
                        });
                    }
                });
            }
            @Override
            public MisPerros_Fragment.PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_lista_perros, parent, false);



                //return new PetsViewHolder(view);
                return  new MisPerros_Fragment.PetsViewHolder(view);
                //return new Mis_Perros.PetsViewHolder(view);
            }
        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);
        return Perrosview;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "mx.com.caminandog.fileprovider",
                        photoFile);
                System.out.println(photoFile);
                uri= photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                TakePicktureActivityResultLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }
    public static class PetsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PetsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.txt_nombre_perro);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.txt_comportamiento_perro);
            post_desc.setText(desc);
        }
        public void setRaza(String desc){
            TextView post_raza = (TextView)mView.findViewById(R.id.txt_raza_perro);
            post_raza.setText(desc);
        }
        public void setEdad(String desc){
            TextView post_edad = (TextView)mView.findViewById(R.id.txt_edad_perro);
            post_edad.setText(desc);
        }
        public void setPadec(String desc){
            TextView post_padec = (TextView)mView.findViewById(R.id.txt_padecimiento_perro);
            post_padec.setText(desc);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.avatar_perro_img);
            //Picasso.with(ctx).load(image).into(post_image);
            Glide.with(ctx).load(image).apply(RequestOptions.circleCropTransform()).into(post_image);
        }
    }


    public void GetCameraPermission(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
             dispatchTakePictureIntent();
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
            subtitlemsj.setText((getResources().getString(R.string.Recuerda_que_para_subir_una_foto_de_tu_perrito)));
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
                    Manifest.permission.CAMERA);
        }
    }

    /*public void getReadPermission(){
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
            subtitlemsj.setText("Recuerda que para subir una foto de tu perrito deberás aceptar el permiso de la camara y acceso al almacenamiento interno.");
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
            requestPermissionLauncherReadexternal.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncherReadexternal =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetCameraPermission();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });*/

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    GetCameraPermission();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    ActivityResultLauncher<Intent> TakePicktureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Bitmap imageBitmap = null;
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }

                        //Bundle extras = data.getExtras();
                        //Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                        data_pick = baos.toByteArray();

                        Glide.with(getActivity()).load(imageBitmap).apply(RequestOptions.circleCropTransform()).into(mUpload);
                    }
                }
            });

    ActivityResultLauncher<Intent> PickPicktureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data.getData();
                        Glide.with(getContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(mUpload);
                    }
                }
            });


}
