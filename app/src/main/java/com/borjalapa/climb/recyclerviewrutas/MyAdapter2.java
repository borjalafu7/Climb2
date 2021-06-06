package com.borjalapa.climb.recyclerviewrutas;

import android.content.Context;
import android.content.Intent;
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
import com.borjalapa.climb.detalles_inventario.detalles_inventario;
import com.borjalapa.climb.detalles_ruta.detalles_ruta;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter2 extends RecyclerView.Adapter<com.borjalapa.climb.recyclerviewrutas.MyAdapter2.MyViewHolder> {

    Context context;
    ArrayList<Ruta> rutas = new ArrayList<>();
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
        holder.tvPueblo.setText(rutas.get(i).getPueblo());
        holder.tvCiudad.setText(rutas.get(i).getCiudad());
        holder.tvComollegar.setText(rutas.get(i).getComo_llegar());
        holder.tvUtensilios.setText(rutas.get(i).getItems());
        holder.tvLatitud.setText(rutas.get(i).getLatitud());
        holder.tvLongitud.setText(rutas.get(i).getLongitud());

        String url = rutas.get(i).getUrl_imagen();
        Glide.with(context)
                .load(url.toString())
                //.fitCenter()
                //.override(800, 400)
                //.centerCrop()
                .into(holder.ivImagen);

        Log.i("url_imagen", url);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalle_ruta = new Intent(context, detalles_ruta.class);
                detalle_ruta.putExtra("nombre", holder.tvNombre.getText().toString().trim());
                detalle_ruta.putExtra("descripcion", holder.tvDescripcion.getText().toString().trim());
                detalle_ruta.putExtra("ciudad", holder.tvCiudad.getText().toString().trim());
                detalle_ruta.putExtra("pueblo", holder.tvPueblo.getText().toString().trim());
                detalle_ruta.putExtra("como_llegar", holder.tvComollegar.getText().toString().trim());
                detalle_ruta.putExtra("items", holder.tvUtensilios.getText().toString().trim());
                detalle_ruta.putExtra("latitud", holder.tvLatitud.getText().toString().trim());
                detalle_ruta.putExtra("longitud", holder.tvLongitud.getText().toString().trim());
                detalle_ruta.putExtra("imagen", url);
                context.startActivity(detalle_ruta);
            }
        });



    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNombre, tvDescripcion,tvPueblo,tvCiudad,tvComollegar,tvUtensilios,tvLatitud,tvLongitud;
        public ImageView ivImagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = (TextView)itemView.findViewById(R.id.nombre);
            tvDescripcion = (TextView)itemView.findViewById(R.id.descripcion);
            tvPueblo = (TextView)itemView.findViewById(R.id.pueblo);
            tvCiudad = (TextView)itemView.findViewById(R.id.ciudad);
            tvComollegar = (TextView)itemView.findViewById(R.id.como_llegar);
            tvUtensilios = (TextView)itemView.findViewById(R.id.items);
            ivImagen = (ImageView)itemView.findViewById(R.id.imagen);
            tvLatitud = (TextView) itemView.findViewById(R.id.latitud);
            tvLongitud = (TextView) itemView.findViewById(R.id.longitud);
        }
    }
}