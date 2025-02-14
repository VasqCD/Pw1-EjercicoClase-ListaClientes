package com.example.pm01app1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pm01app1.configuracion.SQLiteConexion;
import com.example.pm01app1.configuracion.Transacciones;

public class ActivityPrincipal extends AppCompatActivity {

    EditText nombres, apellidos, correo;
    Button btnProcesar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        nombres = (EditText) findViewById(R.id.nombres);
        apellidos = (EditText) findViewById(R.id.apellidos);
        correo = (EditText) findViewById(R.id.correo);
        btnProcesar = (Button) findViewById(R.id.btnProcesar);
        
        btnProcesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClient();
            }
        });



    }

    private void AddClient() {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDB, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombres, nombres.getText().toString());
            valores.put(Transacciones.apellidos, apellidos.getText().toString());
            valores.put(Transacciones.correo, correo.getText().toString());

            Long resultado = db.insert(Transacciones.tabla_clientes, Transacciones.id, valores);

            Toast.makeText(this, getString(R.string.mensaje_exito), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.mensaje_error), Toast.LENGTH_LONG).show();
        }



    }
}