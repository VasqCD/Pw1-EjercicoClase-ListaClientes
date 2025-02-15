package com.example.pm01app1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm01app1.Models.Cliente;
import com.example.pm01app1.configuracion.SQLiteConexion;
import com.example.pm01app1.configuracion.Transacciones;

import java.util.ArrayList;

public class ActivityLists extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listaClientes;
    ArrayList<Cliente> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lists);

        conexion = new SQLiteConexion(this, Transacciones.NameDB, null, 1);
        listaClientes = findViewById(R.id.listclient);

        lista = new ArrayList<>();

        ObtenrInfo();

        CustomAdapter adapter = new CustomAdapter(this, lista);
        listaClientes.setAdapter(adapter);

        listaClientes.setOnItemClickListener((parent, view, position, id) -> {
            Cliente clienteSeleccionado = lista.get(position);

            Intent intent = new Intent(ActivityLists.this, ClientDetailActivity.class);
            intent.putExtra("nombres", clienteSeleccionado.getNombres());
            intent.putExtra("apellidos", clienteSeleccionado.getApellidos());
            intent.putExtra("correo", clienteSeleccionado.getCorreo());
            intent.putExtra("foto", clienteSeleccionado.getFoto());

            startActivity(intent);
        });
    }

    private void ObtenrInfo() {
        try {
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cliente cliente;

            Cursor cursor = db.rawQuery(Transacciones.SelectTableClient, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    cliente = new Cliente();
                    cliente.setId(cursor.getInt(0));
                    cliente.setNombres(cursor.getString(1));
                    cliente.setApellidos(cursor.getString(2));
                    cliente.setCorreo(cursor.getString(3));
                    cliente.setFoto(cursor.getString(4));
                    lista.add(cliente);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar la lista: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}