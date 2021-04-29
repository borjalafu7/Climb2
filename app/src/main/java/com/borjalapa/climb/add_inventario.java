package com.borjalapa.climb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.ui.inventario.InventarioFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class add_inventario extends AppCompatActivity {

    Button btnAdd;
    TextView txtNombre;
    TextView txtDescripcion;
    TextView txtCantidad;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventario);

        btnAdd = findViewById(R.id.btnAdd);
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtDescripcion= (TextView)findViewById(R.id.txtDescripcion);
        txtCantidad = (TextView)findViewById(R.id.txtCantidad);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id= mAuth.getUid();
                Integer cantidad_sacada =  Integer.parseInt(txtCantidad.getText().toString());
                Item item1 = new Item(user_id, txtNombre.getText().toString().trim(), txtDescripcion.getText().toString().trim(),cantidad_sacada,"");


                // Añadir la subcoleccion de inventario
                db.collection("usuarios").document(user_id).collection("inventario").add(item1);


                Intent volver = new Intent(add_inventario.this, Main_Page.class);
                startActivity(volver);
            }
        });
    }
}