package com.borjalapa.climb.configuracion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.borjalapa.climb.MainActivity;
import com.borjalapa.climb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class configuracion extends AppCompatActivity {

    Button reset_password,update_mail,update_password,delete_user;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String correo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        db = FirebaseFirestore.getInstance();
        reset_password = findViewById(R.id.reset_password);
        update_mail = findViewById(R.id.update_mail);
        update_password = findViewById(R.id.update_password);
        delete_user = findViewById(R.id.eliminar_usuario);

        correo = mAuth.getCurrentUser().getEmail();

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword(correo);
            }
        });

        update_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar_correo(v);
            }
        });

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar_contraseña();
            }
        });

        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delUser();
            }
        });

    }

    public void forgotPassword(String correo) {
        mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Se ha enviado un correo a "+correo+" para rehabilitar la contraseña", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Ha sucedido un problema a la hora de enviar el correo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void reauthenticateUser(String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), password);
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Ha sucedido un problema", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void update_Email(String newEmail) {
        String user_id= mAuth.getUid();
        mAuth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.collection("usuarios").document(user_id).update("correo", newEmail);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void update_password(String newPassword, String oldPassword) {
        /*
        mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Contraseña cambiada con éxito", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    Intent ir_inicio = new Intent(configuracion.this, MainActivity.class);
                    startActivity(ir_inicio);
                } else {
                    Toast.makeText(getApplicationContext(), "Ha sucedido un error", Toast.LENGTH_LONG).show();
                }
            }
        });
         */

        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Ha sucedido un error", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Contraseña cambiada con éxito", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                Intent ir_inicio = new Intent(configuracion.this, MainActivity.class);
                                startActivity(ir_inicio);
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Ha sucedido un error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void delUser() {
        String user_id= mAuth.getUid();
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuth.signOut();
                    Intent ir_inicio = new Intent(configuracion.this, MainActivity.class);
                    startActivity(ir_inicio);
                    db.collection("usuarios").document(user_id).delete();

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cambiar_correo(View view) {
        final View alertDialog = getLayoutInflater().inflate(R.layout.dialogo_correo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertDialog);
        builder.setTitle("CAMBIAR EMAIL");
        builder.setMessage("Introduzca su contraseña y su correo nuevo");
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.show();
        Button nbutton = alertDialogCreate.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = alertDialogCreate.getButton(DialogInterface.BUTTON_POSITIVE);

        alertDialogCreate.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nuevo_correo = alertDialog.findViewById(R.id.correo1);
                EditText password_actual = alertDialog.findViewById(R.id.password2);
                if(!password_actual.getText().toString().trim().equals("") && !nuevo_correo.getText().toString().trim().equals("")){
                    reauthenticateUser(password_actual.getText().toString().trim());
                    update_Email(nuevo_correo.getText().toString().trim());
                    alertDialogCreate.dismiss();
                }
                if(password_actual.getText().toString().trim().equals("")){
                    password_actual.setError("Introduzca la contraseña");
                }
                if(nuevo_correo.getText().toString().trim().equals("")){
                    nuevo_correo.setError("Introduzca el correo");
                }
            }
        });
    }

    public void cambiar_contraseña() {
        final View alertDialog2 = getLayoutInflater().inflate(R.layout.dialogo_password, null);

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setView(alertDialog2);
        builder2.setTitle("CAMBIAR CONTRASEÑA");
        builder2.setMessage("Introduzca su antigua contraseña y la nueva");
        builder2.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
            }
        });
        builder2.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
                dialog2.dismiss();
            }
        });

        AlertDialog alertDialogCreate2 = builder2.create();
        alertDialogCreate2.show();
        Button nbutton2 = alertDialogCreate2.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton2 = alertDialogCreate2.getButton(DialogInterface.BUTTON_POSITIVE);

        alertDialogCreate2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText password_antigua = alertDialog2.findViewById(R.id.password_antigua);
                EditText password_nueva = alertDialog2.findViewById(R.id.password_nueva);
                if(!password_nueva.getText().toString().trim().equals("") && !password_antigua.getText().toString().trim().equals("")){
                    reauthenticateUser(password_antigua.getText().toString().trim());
                    update_password(password_nueva.getText().toString().trim(),password_antigua.getText().toString().trim());
                    alertDialogCreate2.dismiss();
                }
                if(password_nueva.getText().toString().trim().equals("")){
                    password_nueva.setError("Introduzca la contraseña nueva");
                }
                if(password_antigua.getText().toString().trim().equals("")){
                    password_antigua.setError("Introduzca la contraseña antigua");
                }
            }
        });

    }

}