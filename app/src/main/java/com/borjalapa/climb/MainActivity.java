package com.borjalapa.climb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText etUsuario;
    EditText etPassword;
    Button btnInicia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnInicia = findViewById(R.id.btnInicia);

        //codigo para el registro
        final TextView txtSub = (TextView)findViewById(R.id.textViewSubrayado);

        txtSub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Aqui el codigo que queremos que ejecute al ser pulsado
            }
        });


        //codigo de inicio de sesión
        btnInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsuario.getText().toString().equals("admin@mail.com") && etPassword.getText().toString().equalsIgnoreCase("admin")) {
                    Intent ir_mapa = new Intent(MainActivity.this, Main_Page.class);
                    ir_mapa.putExtra("usuario", etUsuario.getText().toString());  // pass your values and retrieve them in the other Activity using keyName
                    startActivity(ir_mapa);
                }
            }
        });



    }




}