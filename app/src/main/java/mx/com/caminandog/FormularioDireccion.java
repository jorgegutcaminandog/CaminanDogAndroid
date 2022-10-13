package mx.com.caminandog;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FormularioDireccion extends AppCompatActivity {

    EditText editTextNumInterior, editTextNumExterior, editTextCalle, editTextMunicipio, editTextEstado, editTextCP;

    String ObtDireccion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_direccion);

        getSupportActionBar().setTitle(getResources().getString(R.string.Confirmar_Direccion));

        editTextNumInterior = findViewById(R.id.txtNumInterior);
        editTextNumExterior = findViewById(R.id.txtNumExterior);
        editTextCalle = findViewById(R.id.txtCalle);
        editTextMunicipio = findViewById(R.id.txtMunicipio);
        editTextEstado = findViewById(R.id.txtEstado);
        editTextCP = findViewById(R.id.txtCP);

        editTextNumExterior.setText(getIntent().getStringExtra("NUMERO_EXTERIOR"));
        editTextCalle.setText(getIntent().getStringExtra("CALLE"));
        editTextMunicipio.setText(getIntent().getStringExtra("MUNICIPIO"));
        editTextEstado.setText(getIntent().getStringExtra("ESTADO"));
        editTextCP.setText(getIntent().getStringExtra("CP"));

        ObtDireccion = editTextNumInterior.getText().toString()+" "+ editTextNumExterior.getText().toString()+" "+
                editTextCalle.getText().toString() +" "+ editTextMunicipio.getText().toString() +" "+ editTextEstado.getText().toString()
                +" "+editTextCP.getText().toString();

        Intent intent = getIntent();
        Double latitud = intent.getExtras( ).getDouble( "latitud_inicio" );
        Double longitud = intent.getExtras( ).getDouble( "longitud_inicio" );
        System.out.println("longitud inicio "+longitud);






        Button sig =(Button) findViewById(R.id.btnEnviarDatos);
        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iop = new Intent( FormularioDireccion.this, Configuracion_Paseo.class );
                iop.putExtra("direccion_direccion", ObtDireccion);
                iop.putExtra( "latitud_direccion",latitud );
                iop.putExtra( "longitud_direccion",longitud );
                startActivity( iop );
                finish();

            }
        });
    }
}
