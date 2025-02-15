package com.example.pm01app1;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ClientDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

        ImageView imageView = findViewById(R.id.detailImageView);
        TextView nombresTextView = findViewById(R.id.detailNombres);
        TextView apellidosTextView = findViewById(R.id.detailApellidos);
        TextView correoTextView = findViewById(R.id.detailCorreo);

        // Obtener los datos enviados
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombres = extras.getString("nombres", "");
            String apellidos = extras.getString("apellidos", "");
            String correo = extras.getString("correo", "");
            String fotoPath = extras.getString("foto", "");

            nombresTextView.setText(nombres);
            apellidosTextView.setText(apellidos);
            correoTextView.setText(correo);

            // Cargar la imagen si existe
            if (fotoPath != null && !fotoPath.isEmpty()) {
                File imgFile = new File(fotoPath);
                if (imgFile.exists()) {
                    imageView.setImageURI(Uri.fromFile(imgFile));
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }
    }
}