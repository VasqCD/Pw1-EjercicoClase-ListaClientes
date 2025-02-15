package com.example.pm01app1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pm01app1.configuracion.SQLiteConexion;
import com.example.pm01app1.configuracion.Transacciones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityPrincipal extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView vistaFoto;
    private String currentPhotoPath;
    EditText nombres, apellidos, correo;
    Button btnProcesar;
    ImageButton btnCapturarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        correo = findViewById(R.id.correo);
        btnProcesar = findViewById(R.id.btnProcesar);
        btnCapturarFoto = findViewById(R.id.capturarFoto);
        vistaFoto = findViewById(R.id.vistaFoto);

        btnProcesar.setOnClickListener(v -> AddClient());
        btnCapturarFoto.setOnClickListener(v -> showPictureDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Seleccione una opción");
        String[] pictureDialogItems = {
                "Seleccionar foto de la galería",
                "Tomar usando la cámara" };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            fotoGaleria();
                            break;
                        case 1:
                            fotoCamara();
                            break;
                    }
                });
        pictureDialog.show();
    }

    public void fotoGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    private void fotoCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm01app1.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File file = new File(currentPhotoPath);
                if (file.exists()) {
                    vistaFoto.setImageURI(Uri.fromFile(file));
                }
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        // Crear una copia del archivo seleccionado en el almacenamiento de la app
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "_";
                        File storageDir = getExternalFilesDir(null);
                        File imageFile = File.createTempFile(
                                imageFileName,
                                ".jpg",
                                storageDir
                        );

                        // Copiar el contenido de la imagen seleccionada
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.close();
                        inputStream.close();

                        // Actualizar la ruta de la imagen
                        currentPhotoPath = imageFile.getAbsolutePath();
                        vistaFoto.setImageURI(Uri.fromFile(imageFile));

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddClient() {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDB, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombres, nombres.getText().toString());
            valores.put(Transacciones.apellidos, apellidos.getText().toString());
            valores.put(Transacciones.correo, correo.getText().toString());
            valores.put(Transacciones.foto, currentPhotoPath); // Guardar la ruta de la foto

            Long resultado = db.insert(Transacciones.tabla_clientes, Transacciones.id, valores);

            Toast.makeText(this, getString(R.string.mensaje_exito), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.mensaje_error), Toast.LENGTH_LONG).show();
        }
    }
}