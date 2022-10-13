package mx.com.caminandog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class DatosParaContactoRecuperandog extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_datos_para_contacto_recuperandog, container, false);

        DatabaseReference recuperandog_write = database.getReference(FirebaseReferences.USER_REFERERENCE).child(user.getUid());

        Button btn_actual = (Button) vista.findViewById(R.id.btn_recup_usr_rec);
        TextView txt_name = (TextView) vista.findViewById(R.id.txt_name_recuperdandog_usr_rec);
        TextView txt_numero = (TextView) vista.findViewById(R.id.txt_numero_recuperandog_usr_rec);
        TextView txt_email = (TextView) vista.findViewById(R.id.txt_email_recuperandog_usr_rec);
        TextView txt_msj = (TextView) vista.findViewById(R.id.txt_mensaje_recuperandog_usr_rec);
        ImageView img = vista.findViewById(R.id.imgdat);
        CheckBox checkbox = (CheckBox) vista.findViewById(R.id.checkboxrec);
        Button btn_back = vista.findViewById(R.id.backdatos);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ContenedorRecuperandog_Fragment fragment2 = new ContenedorRecuperandog_Fragment();
                ft.replace(R.id.contenedor, fragment2);
                //ft.addToBackStack(Perrhijos_Recuperandog_Fragment.this);
                ft.commit();
            }
        });

        btn_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkbox.isChecked()){

                    if (TextUtils.isEmpty(txt_name.getText())) {
                        txt_name.setError(getResources().getString(R.string.Ingresa_un_Nombre));
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_name, InputMethodManager.SHOW_IMPLICIT);
                        return;
                    } else if (TextUtils.isEmpty(txt_numero.getText())||txt_numero.getText().length()<10) {
                        txt_numero.setError(getResources().getString(R.string.Ingresa_un_numero_de_telefono_o_ingresa_al_menos_10_digitos));
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_numero, InputMethodManager.SHOW_IMPLICIT);
                        return;
                    } else if (TextUtils.isEmpty(txt_email.getText())) {
                        txt_email.setError(getResources().getString(R.string.Ingresa_una_direccion_de_correo_electronico_valida));
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_email, InputMethodManager.SHOW_IMPLICIT);
                        return;
                    }else if (TextUtils.isEmpty(txt_msj.getText())){
                        txt_email.setError(getResources().getString(R.string.Ingresa_un_Mensaje));
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_msj, InputMethodManager.SHOW_IMPLICIT);
                        return;
                    }else if (txt_msj.getText().toString().length()>280){
                        txt_email.setError(getResources().getString(R.string.El_mensaje_es_muy_largo));
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_msj, InputMethodManager.SHOW_IMPLICIT);
                        return;
                    }else {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_numero, InputMethodManager.RESULT_HIDDEN);

                        recuperandog_write.child("nombreRecuperandog").setValue(txt_name.getText().toString());
                        recuperandog_write.child("telefonoRecuperandog").setValue(txt_numero.getText().toString());
                        recuperandog_write.child("correoRecuperandog").setValue(txt_email.getText().toString());
                        recuperandog_write.child("mensajeRecuperandog").setValue(txt_msj.getText().toString());

                        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_write_confirm, null);
                        mBuilder.setView(mView);
                        final androidx.appcompat.app.AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        Button btn_dismiss = (Button) mView.findViewById(R.id.btn_si_writedialog);
                        btn_dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();


                    }

                }else{
                    checkbox.setError("debe seleccionar para actualizar");
                }




            }
        });

        try {
            //consulta
            Query query = recuperandog_write.orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    txt_name.setText(usuario.getNombreRecuperandog());
                    txt_numero.setText(usuario.getTelefonoRecuperandog());
                    txt_email.setText(usuario.getCorreoRecuperandog());
                    txt_msj.setText(usuario.getMensajeRecuperandog());
                    //Glide.with(getActivity()).load(usuario.).apply(RequestOptions.circleCropTransform()).into(post_image);
                    Glide.with(getActivity()).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(img);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            recuperandog_write.child("nombreRecuperandog").setValue(txt_name.getText().toString());
            recuperandog_write.child("telefonoRecuperandog").setValue(txt_numero.getText().toString());
            recuperandog_write.child("correoRecuperandog").setValue(txt_email.getText().toString());
            recuperandog_write.child("mensajeRecuperandog").setValue(txt_msj.getText().toString());
        }

        return vista;
    }
}