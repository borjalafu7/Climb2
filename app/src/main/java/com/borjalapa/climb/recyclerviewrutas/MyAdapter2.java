package com.borjalapa.climb.recyclerviewrutas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borjalapa.climb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter2 extends RecyclerView.Adapter<com.borjalapa.climb.recyclerviewrutas.MyAdapter2.MyViewHolder> {

    Context context;
    ArrayList<Ruta> rutas;
    int cont;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public MyAdapter2(Context context, ArrayList<Ruta> rutas) {
        this.rutas = rutas;
        this.context = context;
    }

    public void setData(ArrayList rutas){
        this.rutas = rutas;

        //para actualizar la lista
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public com.borjalapa.climb.recyclerviewrutas.MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //metes en una variable inflada el xml para vincular variables
        View view = LayoutInflater.from(context).inflate(R.layout.items_list_rutas, parent,false);

        return new com.borjalapa.climb.recyclerviewrutas.MyAdapter2.MyViewHolder(view);
    }



    //metodo que aplica la funcionalidad
    @Override
    public void onBindViewHolder(@NonNull com.borjalapa.climb.recyclerviewrutas.MyAdapter2.MyViewHolder holder, int i) {
        //inicializacion del firebaseAuth y el firestore
        db = FirebaseFirestore.getInstance();

        holder.tvNombre.setText(rutas.get(i).getNombre());
        holder.tvDescripcion.setText(rutas.get(i).getDescripcion());
        //holder.tvPueblo.setText(rutas.get(i).getPueblo());
        holder.tvCiudad.setText(rutas.get(i).getCiudad());
        //holder.tvComollegar.setText(rutas.get(i).getComo_llegar());
        //holder.tvUtensilios.setText(rutas.get(i).getItems());
        //String url = items.get(i).getUrl_imagen();
        //Picasso.get().load(url).into(holder.ivImagen);


        holder.tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView t = (TextView)view;
                Toast.makeText(context,"Has pulsado nombre : " + t.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNombre, tvDescripcion,tvPueblo,tvCiudad,tvComollegar,tvUtensilios;
        public ImageView ivImagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = (TextView)itemView.findViewById(R.id.nombre);
            tvDescripcion = (TextView)itemView.findViewById(R.id.descripcion);
            //tvPueblo = (TextView)itemView.findViewById(R.id.pueblo);
            tvCiudad = (TextView)itemView.findViewById(R.id.ciudad);
            //tvComollegar = (TextView)itemView.findViewById(R.id.como_llegar);
            //tvUtensilios = (TextView)itemView.findViewById(R.id.utensilios);
            ivImagen = (ImageView)itemView.findViewById(R.id.imagen);
        }
    }
}