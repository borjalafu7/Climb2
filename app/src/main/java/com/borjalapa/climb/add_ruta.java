package com.borjalapa.climb;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.borjalapa.climb.recyclerviewrutas.Ruta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_ruta extends AppCompatActivity {

    Button btnAddRuta;
    TextView txtNombreRuta;
    TextView txtDescripcionRuta;
    TextView txtPuebloRuta,txtCiudadRuta,txtComoLlegarRuta,txtUtensilios;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ruta);

        btnAddRuta = findViewById(R.id.btnAddRuta);
        txtNombreRuta = findViewById(R.id.nombreruta);
        txtDescripcionRuta = findViewById(R.id.descripcionruta);
        txtPuebloRuta = findViewById(R.id.puebloruta);
        txtCiudadRuta = findViewById(R.id.ciudadruta);
        txtComoLlegarRuta = findViewById(R.id.como_llegar_ruta);
        txtUtensilios = findViewById(R.id.itemsruta);

        btnAddRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id= mAuth.getUid();

                Ruta ruta1 = new Ruta(user_id,"Ruta1","Descripción de la ruta1","Alaquás","Valencia","Ejemplo de como llegar", "casco, saco de dormir, tienda de campaña","");

                // Añadir la subcoleccion de inventario de rutas
                db.collection("usuarios").document(user_id).collection("rutas").add(ruta1);

                Intent volver = new Intent(add_ruta.this, Main_Page.class);
                startActivity(volver);
            }
        });
    }
}