package com.borjalapa.climb.detalles_ruta;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.borjalapa.climb.Main_Page;
import com.borjalapa.climb.detalles_inventario.detalles_inventario;
import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.recyclerviewrutas.Ruta;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class detalles_ruta extends AppCompatActivity {

    EditText etRutaNombre,etRutaDescripcion,etRutaCiudad,etRutaPueblo,etRutaComollegar,etRutaItems,etLatitud,etLongitud;
    ImageView imagen_ruta;
    Button btnEditarRuta;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final int GALLERY_INTENT = 1;

    Ruta ruta_entrada = new Ruta();

    ProgressBar progressBar;
    StorageReference storageRef;

    Uri uri_global;
    boolean comprobar_foto = false;

    String nombre_ruta = "";
    String descripcion_ruta = "";
    String pueblo_ruta = "";
    String ciudad_ruta = "";
    String como_llegar_ruta = "";
    String items_ruta = "";
    String editar_imagen_ruta = "";
    String latitd_ruta = "";
    String longitud_ruta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ruta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalle Ruta");


        etRutaNombre = findViewById(R.id.edit_ruta_nombre);
        etRutaDescripcion = findViewById(R.id.edit_ruta_descripcion);
        etRutaCiudad = findViewById(R.id.edit_ruta_ciudad);
        etRutaPueblo = findViewById(R.id.edit_ruta_pueblo);
        etRutaComollegar = findViewById(R.id.edit_ruta_como_llegar);
        etRutaItems = findViewById(R.id.edit_ruta_items);
        etLatitud = findViewById(R.id.edit_latitud);
        etLongitud = findViewById(R.id.edit_longitud);
        imagen_ruta = findViewById(R.id.edit_imagen_ruta);
        btnEditarRuta = findViewById(R.id.btnEditRuta);
        progressBar = (ProgressBar)findViewById(R.id.barra_progeso_ruta);

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

                db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).delete();
                Intent eliminar = new Intent(detalles_ruta.this, Main_Page.class);
                startActivity(eliminar);

            }
        });

        //recogida ded datos del intent y seteo de datos en el activity
        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            nombre_ruta = extras.getString("nombre");
            descripcion_ruta = extras.getString("descripcion");
            pueblo_ruta = extras.getString("pueblo");
            como_llegar_ruta = extras.getString("como_llegar");
            items_ruta = extras.getString("items");
            ciudad_ruta = extras.getString("ciudad");
            editar_imagen_ruta = extras.getString("imagen");
            latitd_ruta = extras.getString("latitud");
            longitud_ruta = extras.getString("longitud");
        }

        etRutaNombre.setText(nombre_ruta);
        etRutaDescripcion.setText(descripcion_ruta);
        etRutaCiudad.setText(ciudad_ruta);
        etRutaPueblo.setText(pueblo_ruta);
        etRutaComollegar.setText(como_llegar_ruta);
        etRutaItems.setText(items_ruta);
        etLatitud.setText(latitd_ruta);
        etLongitud.setText(longitud_ruta);
        imagen_ruta.setImageURI(Uri.parse(editar_imagen_ruta));
        Glide.with(getApplicationContext())
                .load(editar_imagen_ruta.toString())
                //.fitCenter()
                //.override(800, 400)
                //.centerCrop()
                .into(imagen_ruta);

        Log.i("IMAGEN",editar_imagen_ruta.toString());


        imagen_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        btnEditarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id= mAuth.getUid();

                if (etRutaNombre.getText().toString().trim().equalsIgnoreCase("") || etRutaDescripcion.getText().toString().trim().equalsIgnoreCase("") || etRutaCiudad.getText().toString().trim().equalsIgnoreCase("") || etRutaPueblo.getText().toString().equalsIgnoreCase("")  || etRutaComollegar.getText().toString().equalsIgnoreCase("") || etRutaItems.getText().toString().equalsIgnoreCase("") || comprobar_foto == false ){

                    Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_LONG).show();

                }else{
                    ruta_entrada.setId(user_id);
                    ruta_entrada.setNombre(etRutaNombre.getText().toString().trim());
                    ruta_entrada.setDescripcion(etRutaDescripcion.getText().toString().trim());
                    ruta_entrada.setCiudad(etRutaCiudad.getText().toString().trim());
                    ruta_entrada.setPueblo(etRutaPueblo.getText().toString().trim());
                    ruta_entrada.setComo_llegar(etRutaComollegar.getText().toString().trim());
                    ruta_entrada.setItems(etRutaItems.getText().toString().trim());
                    ruta_entrada.setLatitud(etLatitud.getText().toString().trim());
                    ruta_entrada.setLongitud(etLongitud.getText().toString().trim());

                    Log.i("INFO",ruta_entrada.getUrl_imagen());

                    UploadTask uptsk;
                    StorageReference filePath = storageRef.child("rutas").child(uri_global.getLastPathSegment());

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
                                    if (etRutaNombre.equals(nombre_ruta)){
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("descripcion", etRutaDescripcion.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("ciudad", etRutaCiudad.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("pueblo", etRutaPueblo.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("como_llegar", etRutaComollegar.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("items", etRutaItems.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("latitud", etLatitud.getText().toString().trim());
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).update("longitud", etLongitud.getText().toString().trim());
                                    }else{
                                        db.collection("usuarios").document(user_id).collection("rutas").document(nombre_ruta).delete();

                                        db.collection("usuarios").document(user_id).collection("rutas").document(etRutaNombre.getText().toString().trim()).set(ruta_entrada);
                                        //guardo ruta foto en firestore
                                        db.collection("usuarios").document(user_id).collection("rutas").document(etRutaNombre.getText().toString().trim()).update("url_imagen", uri_global.toString());
                                    }

                                    progressBar.setVisibility(View.GONE);

                                    Intent volver = new Intent(detalles_ruta.this, Main_Page.class);
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
            ruta_entrada.setUrl_imagen(imageUrl);
            imagen_ruta.setImageURI(Uri.parse(imageUrl));
            comprobar_foto = true;
        }
    }
}