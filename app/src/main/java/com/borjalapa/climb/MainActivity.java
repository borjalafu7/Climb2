package com.borjalapa.climb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borjalapa.climb.registro.registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends AppCompatActivity {

    EditText etCorreo;
    EditText etPassword;
    Button btnInicia,btnSOS,btnFacebook;
    TextView textViewSubrayado;
    private FirebaseAuth mAuth;

    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnInicia = findViewById(R.id.btnInicia);
        btnSOS = findViewById(R.id.btnSOS);

        mAuth = FirebaseAuth.getInstance();
        setTitle("Climb");

        //codigo para el registro
        final TextView txtSub = (TextView)findViewById(R.id.textViewSubrayado);



        txtSub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Aqui el codigo que queremos que ejecute al ser pulsado
                Intent ir_registro = new Intent(MainActivity.this, registro.class);
                startActivity(ir_registro);
            }
        });


        //codigo del inicio de sesión
        btnInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (correo.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                } else {
                    iniciar_sesion(correo,password);
                }
            }
        });

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if

                    (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            android.Manifest.permission.CALL_PHONE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        Toast.makeText(MainActivity.this,"Its Necessary To Call",Toast.LENGTH_LONG).show();

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]     {Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    } else {

                        // No explanation needed, we can request the     permission.

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]   {Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }else{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + 112));
                    startActivity(intent);

                }
                Intent i = new Intent(android.content.Intent.ACTION_CALL,
                Uri.parse("tel:0000000"));
                startActivity(i);
            }
        });

    }

    public void iniciar_sesion(String correo, String password){
        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Has iniciado sesión con éxito", Toast.LENGTH_SHORT).show();
                            Intent ir_mapa = new Intent(MainActivity.this, Main_Page.class);
                            startActivity(ir_mapa);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}