package com.example.pm01app1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    ArrayList<String> arreglo;
    ArrayList<Cliente> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lists);

        conexion = new SQLiteConexion(this, Transacciones.NameDB, null, 1);
        listaClientes = findViewById(R.id.listclient);

        lista = new ArrayList<>();
        arreglo = new ArrayList<>();

        ObtenrInfo();

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arreglo);
        listaClientes.setAdapter(adp);


    }

    private void ObtenrInfo() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cliente cliente;

        Cursor cursor = db.rawQuery(Transacciones.SelectTableClient, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                cliente = new Cliente();
                cliente.setId(cursor.getInt(0));
                cliente.setNombres(cursor.getString(1));
                cliente.setApellidos(cursor.getString(2));
                cliente.setCorreo(cursor.getString(3));
                lista.add(cliente);
            }
            cursor.close();
        }

        FillData();
    }

    private void FillData() {
        for (Cliente cliente : lista) {
            arreglo.add(cliente.getId() + " | " + cliente.getNombres() + " | " + cliente.getApellidos() + " | " + cliente.getCorreo());
        }
    }
}
// Funcion