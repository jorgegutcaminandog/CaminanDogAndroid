package mx.com.caminandog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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

public class Reportado_Fragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_reportado_, container, false);
        Bundle bundle3 = getArguments();
        TextView txtmsj = vista.findViewById(R.id.msjrepor);
        TextView txtNoPlaca = vista.findViewById(R.id.placaperrrep);
        Button btnRep = vista.findViewById(R.id.btnRepRep);
        ImageView foto = (ImageView)vista.findViewById(R.id.fotoRepo);
        Glide.with(getActivity()).load(bundle3.getString("foto")).apply( RequestOptions.circleCropTransform()).into(foto);
        TextView txt_nombreper = vista.findViewById(R.id.nombreperrrep);
        txt_nombreper.setText(bundle3.getString("nombre"));

        Button back = (Button) vista.findViewById(R.id.backreporte);
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


        DatabaseReference recuperandog_lectura_ref = database.getReference(FirebaseReferences.RECUPERANDOG_REFERENCE).child( bundle3.getString("idPerro") );
        Query query = recuperandog_lectura_ref.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Paseo_recuperandog paseo_recuperandog = dataSnapshot.getValue(Paseo_recuperandog.class);

                txtNoPlaca.setText(paseo_recuperandog.getQR());


                if (paseo_recuperandog.getReportado().equals("true")){
                    txtmsj.setText(getResources().getString(R.string.El_extravio_de_tu_mascota));
                    btnRep.setText(getResources().getString(R.string.Finalizar_reporte_de_extravio));
                    btnRep.setBackground(getResources().getDrawable(R.drawable.rounded_button_azul_recup));
                    btnRep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recuperandog_lectura_ref.child("reportado").setValue("false");
                            txtmsj.setText(getResources().getString(R.string.Si_tu_mascota_vinculada));
                            btnRep.setText(getResources().getString(R.string.Reportar_mascota_extraviada));
                            btnRep.setBackground(getResources().getDrawable(R.drawable.rounded_button_grey_reporte));

                        }
                    });
                }else{
                    btnRep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recuperandog_lectura_ref.child("reportado").setValue("true");
                            btnRep.setBackground(getResources().getDrawable(R.drawable.rounded_button_azul_recup));
                            txtmsj.setText(getResources().getString(R.string.El_extravio_de_tu_mascota));
                            btnRep.setText(getResources().getString(R.string.Finalizar_reporte_de_extravio));

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vista;
    }
}