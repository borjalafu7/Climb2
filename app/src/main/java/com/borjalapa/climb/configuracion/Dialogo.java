package com.borjalapa.climb.configuracion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.borjalapa.climb.R;


public class Dialogo {

    Context context;
    RespuestaDialogo rd;

    public Dialogo(Context context, RespuestaDialogo rd){
        this.context = context;
        this.rd = rd;
    }

    public Dialog customDialog(){
        //Creamos el constructor de dialogos
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialogo_correo,null));

        //devuelves el builder creandolo
        return builder.create();
    }

    public interface RespuestaDialogo{
        void OnAccept(String cadena);
        void OnCancel(String cadena);
    }
}
