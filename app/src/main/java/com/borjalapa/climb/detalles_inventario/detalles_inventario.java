package com.borjalapa.climb.detalles_inventario;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.borjalapa.climb.Main_Page;
import com.borjalapa.climb.add_inventario;
import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.recyclerviewinventario.MyAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.borjalapa.climb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class detalles_inventario extends AppCompatActivity {

    EditText etNombre,etDescripcion,etCantidad;
    ImageView editar_imagen;
    Button btnEditar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final int GALLERY_INTENT = 1;

    Item item_entrada = new Item();

    ProgressBar progressBar;
    StorageReference storageRef;

    Uri uri_global;
    boolean comprobar_foto = false;

    String nombre = "";
    String descripcion = "";
    int cantidad = 0;
    String imagen="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_inventario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalle Inventario");

        etNombre = findViewById(R.id.edit_nombre);
        etDescripcion = findViewById(R.id.edit_descripcion);
        etCantidad = findViewById(R.id.edit_cantidad);
        editar_imagen = findViewById(R.id.edit_imagen);
        btnEditar = findViewById(R.id.btnEdit);
        progressBar = (ProgressBar)findViewById(R.id.barra_progeso);

        db = FirebaseFirestore.getInstance();
        String user_id= mAuth.getUid();

        storageRef = FirebaseStorage.getInstance().getReference();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar
                finish();
            }
        });

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Objeto eliminado con éxito", Toast.LENGTH_SHORT).show();

                db.collection("usuarios").document(user_id).collection("inventario").document(nombre).delete();
                Intent eliminar = new Intent(detalles_inventario.this, Main_Page.class);
                startActivity(eliminar);

            }
        });

        //recogida ded datos del intent y seteo de datos en el activity
        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            nombre = extras.getString("nombre");
            descripcion = extras.getString("descripcion");
            cantidad = extras.getInt("cantidad");
            imagen =  extras.getString("imagen");
        }

        etNombre.setText(nombre);
        etDescripcion.setText(descripcion);
        etCantidad.setText(String.valueOf(cantidad));
        editar_imagen.setImageURI(Uri.parse(imagen));
        Glide.with(getApplicationContext())
                .load(imagen.toString())
                //.fitCenter()
                //.override(800, 400)
                //.centerCrop()
                .into(editar_imagen);


        editar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id= mAuth.getUid();

                if (etNombre.getText().toString().trim().equalsIgnoreCase("") || etDescripcion.getText().toString().trim().equalsIgnoreCase("") || etCantidad.getText().toString().trim().equalsIgnoreCase("") || comprobar_foto == false ){

                    Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_LONG).show();

                }else{
                    Integer cantidad_sacada =  Integer.parseInt(etCantidad.getText().toString());
                    item_entrada.setId(user_id);
                    item_entrada.setNombre(etNombre.getText().toString().trim());
                    item_entrada.setDescripcion(etDescripcion.getText().toString().trim());
                    item_entrada.setCantidad(cantidad_sacada);

                    Log.i("INFO",item_entrada.getUrl_imagen());

                    UploadTask uptsk;
                    StorageReference filePath = storageRef.child("inventario").child(uri_global.getLastPathSegment());

                    uptsk = filePath.putFile(uri_global);

                    Log.i("RUTA",user_id);

                    progressBar.setVisibility(View.VISIBLE);

                    uptsk.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> firebaseuri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseuri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(), "Foto cambiada " + uri.toString(), Toast.LENGTH_LONG).show();

                                    uri_global = uri;
                                    Log.i("IMAGEN",uri_global.toString());

                                    // Añadir la subcoleccion de inventario
                                    if (etNombre.equals(nombre)){
                                        db.collection("usuarios").document(user_id).collection("inventario").document(nombre).update("descripcion", etDescripcion.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("inventario").document(nombre).update("cantidad", etCantidad.getText().toString().trim());

                                    }else{
                                        db.collection("usuarios").document(user_id).collection("inventario").document(nombre).delete();

                                        db.collection("usuarios").document(user_id).collection("inventario").document(etNombre.getText().toString().trim()).set(item_entrada);
                                        //guardo ruta foto en firestore
                                        db.collection("usuarios").document(user_id).collection("inventario").document(etNombre.getText().toString().trim()).update("url_imagen", uri_global.toString());
                                    }

                                    progressBar.setVisibility(View.GONE);

                                    Intent volver = new Intent(detalles_inventario.this, Main_Page.class);
                                    startActivity(volver);

                                }
                            });

                        }
                    });



                }

            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            uri_global = data.getData();
            String imageUrl = String.valueOf(data.getData());
            item_entrada.setUrl_imagen(imageUrl);
            editar_imagen.setImageURI(Uri.parse(imageUrl));
            comprobar_foto = true;
        }
    }
}