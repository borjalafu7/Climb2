package com.borjalapa.climb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.ui.inventario.InventarioFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class add_inventario extends AppCompatActivity {

    Button btnAdd;
    TextView txtNombre;
    TextView txtDescripcion;
    TextView txtCantidad;
    ImageView imagen_add;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ProgressBar progressBar;
    StorageReference storageRef;

    private static final int GALLERY_INTENT = 1;

    Uri uri;
    Item item1 = new Item();

    boolean comprobar_foto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventario);
        setTitle("Añadir Inventario");

        btnAdd = findViewById(R.id.btnAdd);
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtDescripcion= (TextView)findViewById(R.id.txtDescripcion);
        txtCantidad = (TextView)findViewById(R.id.txtCantidad);
        imagen_add = (ImageView) findViewById(R.id.imagen_add);
        progressBar = (ProgressBar)findViewById(R.id.barra);

        storageRef = FirebaseStorage.getInstance().getReference();

        imagen_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id= mAuth.getUid();

                if (txtNombre.getText().toString().trim().equalsIgnoreCase("") || txtDescripcion.getText().toString().trim().equalsIgnoreCase("") || txtCantidad.getText().toString().trim().equalsIgnoreCase("") || comprobar_foto == false){

                    Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_LONG).show();

                }else{
                    Integer cantidad_sacada =  Integer.parseInt(txtCantidad.getText().toString());
                    item1.setId(user_id);
                    item1.setNombre(txtNombre.getText().toString().trim());
                    item1.setDescripcion(txtDescripcion.getText().toString().trim());
                    item1.setCantidad(cantidad_sacada);

                    Log.i("INFO",item1.getUrl_imagen());

                    UploadTask uptsk;
                    StorageReference filePath = storageRef.child("inventario").child(uri.getLastPathSegment());

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
                                    db.collection("usuarios").document(user_id).collection("inventario").document(txtNombre.getText().toString()).update("url_imagen", uri.toString());

                                    progressBar.setVisibility(View.GONE);

                                    Intent volver = new Intent(add_inventario.this, Main_Page.class);
                                    startActivity(volver);

                                }
                            });

                        }
                    });

                    // Añadir la subcoleccion de inventario
                    db.collection("usuarios").document(user_id).collection("inventario").document(txtNombre.getText().toString()).set(item1);
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
            item1.setUrl_imagen(imageUrl);
            imagen_add.setImageURI(Uri.parse(imageUrl));
            comprobar_foto = true;
        }
    }
}