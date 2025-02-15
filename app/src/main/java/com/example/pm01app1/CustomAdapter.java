
package com.example.pm01app1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pm01app1.Models.Cliente;

import java.io.File;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Cliente> {

    private Context context;
    private List<Cliente> clientes;

    public CustomAdapter(Context context, List<Cliente> clientes) {
        super(context, R.layout.list_item, clientes);
        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Cliente cliente = clientes.get(position);

        ImageView imageViewFoto = convertView.findViewById(R.id.imageViewFoto);
        TextView textViewNombre = convertView.findViewById(R.id.textViewNombre);
        TextView textViewApellido = convertView.findViewById(R.id.textViewApellido);
        TextView textViewCorreo = convertView.findViewById(R.id.textViewCorreo);

        textViewNombre.setText(cliente.getNombres());
        textViewApellido.setText(cliente.getApellidos());
        textViewCorreo.setText(cliente.getCorreo());

        // Manejo más seguro de las imágenes
        try {
            if (cliente.getFoto() != null && !cliente.getFoto().isEmpty()) {
                File imgFile = new File(cliente.getFoto());
                if (imgFile.exists()) {
                    imageViewFoto.setImageURI(Uri.fromFile(imgFile));
                } else {
                    // Usar un icono por defecto del sistema
                    imageViewFoto.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                // Usar un icono por defecto del sistema
                imageViewFoto.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        } catch (Exception e) {
            // En caso de cualquier error, usar un icono por defecto del sistema
            imageViewFoto.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        return convertView;
    }
}