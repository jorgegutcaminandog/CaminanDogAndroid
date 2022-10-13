package mx.com.caminandog;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Tarifas_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View TarifasView = inflater.inflate(R.layout.fragment_tarifas_, container, false);



        DatabaseReference vig_ref = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.CONFIGURACION_REFERENCE);
        Query query_vig = vig_ref;
        query_vig.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                Configuracion config = dataSnapshot3.getValue(Configuracion.class);

                ImageButton tarifas = TarifasView.findViewById(R.id.img_banner_tarif);
                //Glide.with(getContext()).load(getImage("bannerroma")).into(roma);
                Glide.with(getContext()).load(config.getFoto_costos()).into(tarifas);

                tarifas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://caminandog.com.mx/paseos.html";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return TarifasView;
    }



}
