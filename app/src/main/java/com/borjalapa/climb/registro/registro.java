package com.borjalapa.climb.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borjalapa.climb.MainActivity;
import com.borjalapa.climb.Main_Page;
import com.borjalapa.climb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {

    TextView link_to_login,register_error;
    EditText txtEmail,txtUserName,txtPass;
    Button btnRegister;
    //definicion instancia de firebase/autentificacion
    private FirebaseAuth mAuth;
    public FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        link_to_login = findViewById(R.id.link_to_login);
        txtEmail = findViewById(R.id.txtEmail);
        btnRegister = findViewById(R.id.btnRegister);
        txtUserName = findViewById(R.id.txtUserName);
        txtPass = findViewById(R.id.txtPass);
        register_error = findViewById(R.id.register_error);

        //inicializacion del firebaseAuth y el firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //link a la pantalla de REGISTRO
        link_to_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Aqui el codigo que queremos que ejecute al ser pulsado
                Intent ir_login = new Intent(registro.this, MainActivity.class);
                startActivity(ir_login);
            }
        });

        //btn de registrar usuario
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                String correo = txtEmail.getText().toString().trim();
                String password = txtPass.getText().toString().trim();
                String usuario = txtUserName.getText().toString().trim();

                 //verificacion de que no este vacio el campo de correo
                if(TextUtils.isEmpty(correo)){
                      Toast.makeText(getApplicationContext(),"Se debe añadir un correo",Toast.LENGTH_SHORT).show();
                      return;
                }

                //verificacion de que no este vacio el campo de contraseña
                if(TextUtils.isEmpty(password)){
                      Toast.makeText(getApplicationContext(),"Se debe añadir una contraseña",Toast.LENGTH_SHORT).show();
                      return;
                }

                //registrar el usuario en el autentificate y registrarlo en la bd.
                crearUsuario(correo,password,usuario);

            }
        });


    }

    public void crearUsuario(String correo,String password,String usuario){
        Log.i("FALLO","AXSDASDX");

        //creando un nuevo usuario
        mAuth.createUserWithEmailAndPassword(correo,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user_fb = mAuth.getCurrentUser();
                    String id_user = user_fb.getUid();
                    Log.i("FALLO", id_user);

                    Map<String, Object> user = new HashMap<>();
                    user.put("usuario", usuario);
                    user.put("correo", correo);
                    user.put("id", id_user);


                    Log.i("FALLO","AXSDASDX");
                    String usuarios = "usuarios";
                    // Add a new document with a generated ID
                    db.collection(usuarios).document(id_user).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(registro.this,"Se ha registrado el usuario con éxito", Toast.LENGTH_SHORT).show();
                            Intent ir_login = new Intent(registro.this, MainActivity.class);
                            startActivity(ir_login);

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registro.this,"NO se ha registrado el usuario con éxito", Toast.LENGTH_SHORT).show();
                        }
                    });



                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(registro.this, "No se pudo registrar el usuario.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }





}