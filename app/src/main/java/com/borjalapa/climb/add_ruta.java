package com.borjalapa.climb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.borjalapa.climb.recyclerviewrutas.Ruta;
import com.borjalapa.climb.ui.rutas.RutasFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add_ruta extends AppCompatActivity {

    Button btnAddRuta;
    TextView txtNombreRuta;
    TextView txtDescripcionRuta;
    TextView txtPuebloRuta,txtCiudadRuta,txtComoLlegarRuta,txtUtensilios,txtLatitud,txtLongitud;
    ImageView imagen_ruta;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ProgressBar progressBar;
    StorageReference storageRef;

    Uri uri;
    private static final int GALLERY_INTENT = 1;
    Ruta ruta1 = new Ruta();

    boolean comprobar_foto = false;

    double longitud_click = 0.0;
    double latitud_click = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ruta);
        setTitle("Añadir Ruta");

        btnAddRuta = findViewById(R.id.btnAddRuta);
        txtNombreRuta = findViewById(R.id.nombreruta);
        txtDescripcionRuta = findViewById(R.id.descripcionruta);
        txtPuebloRuta = findViewById(R.id.puebloruta);
        txtCiudadRuta = findViewById(R.id.ciudadruta);
        txtComoLlegarRuta = findViewById(R.id.como_llegar_ruta);
        txtUtensilios = findViewById(R.id.itemsruta);
        txtLatitud = findViewById(R.id.latitudruta);
        txtLongitud = findViewById(R.id.longitudruta);
        imagen_ruta = findViewById(R.id.imagen_addruta);
        progressBar = (ProgressBar)findViewById(R.id.barra_carga);

        storageRef = FirebaseStorage.getInstance().getReference();

        imagen_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        latitud_click = getIntent().getExtras().getDouble("latitud");
        longitud_click = getIntent().getExtras().getDouble("longitud");

        txtLatitud.setText(String.valueOf(latitud_click));
        txtLongitud.setText(String.valueOf(longitud_click));
        txtNombreRuta.setHint("Nueva Ruta");

        btnAddRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id= mAuth.getUid();

                if (txtNombreRuta.getText().toString().trim().equalsIgnoreCase("") || txtDescripcionRuta.getText().toString().trim().equalsIgnoreCase("") || txtCiudadRuta.getText().toString().trim().equalsIgnoreCase("") || comprobar_foto == false){

                    Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_LONG).show();

                }else{

                    if (ruta1.getUrl_imagen().equalsIgnoreCase("")){

                        Toast.makeText(getApplicationContext(), "Falta una imagen por añadir", Toast.LENGTH_LONG).show();
                    }else{
                        ruta1.setId(user_id);
                        ruta1.setNombre(txtNombreRuta.getText().toString().trim());
                        ruta1.setDescripcion(txtDescripcionRuta.getText().toString().trim());
                        ruta1.setPueblo(txtPuebloRuta.getText().toString().trim());
                        ruta1.setCiudad(txtCiudadRuta.getText().toString().trim());
                        ruta1.setComo_llegar(txtComoLlegarRuta.getText().toString().trim());
                        ruta1.setItems(txtUtensilios.getText().toString().trim());
                        ruta1.setLatitud(txtLatitud.getText().toString().trim());
                        ruta1.setLongitud(txtLongitud.getText().toString().trim());

                        UploadTask uptsk;
                        StorageReference filePath = storageRef.child("rutas").child(uri.getLastPathSegment());

                        uptsk = filePath.putFile(uri);

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

                                        //guardo ruta foto en firestore
                                        db.collection("usuarios").document(user_id).collection("rutas").document(txtNombreRuta.getText().toString()).update("url_imagen", uri.toString());

                                        progressBar.setVisibility(View.GONE);

                                        Intent volver = new Intent(add_ruta.this, Main_Page.class);
                                        startActivity(volver);

                                    }
                                });

                            }
                        });

                        // Añadir la subcoleccion de inventario
                        db.collection("usuarios").document(user_id).collection("rutas").document(txtNombreRuta.getText().toString()).set(ruta1);
                    }

                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            String imageUrl = String.valueOf(data.getData());
            ruta1.setUrl_imagen(imageUrl);
            imagen_ruta.setImageURI(Uri.parse(imageUrl));
            comprobar_foto = true;
        }
    }
}