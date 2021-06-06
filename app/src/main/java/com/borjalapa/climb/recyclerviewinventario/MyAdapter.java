package com.borjalapa.climb.recyclerviewinventario;

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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.app.ActivityCompat.startActivityForResult;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> items = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public MyAdapter(Context context, ArrayList<Item> items ) {
        this.items = items;
        this.context = context;
    }

    public void setData( ArrayList<Item> items){
        this.items = items;

        //para actualizar la lista
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //metes en una variable inflada el xml para vincular variables
        View view = LayoutInflater.from(context).inflate(R.layout.items_list, parent,false);

        return new MyViewHolder(view);
    }

    //metodo que aplica la funcionalidad
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        //inicializacion del firebaseAuth y el firestore

        holder.tvNombre.setText(items.get(i).getNombre());
        holder.tvDescripcion.setText(items.get(i).getDescripcion());
        holder.tvCantidad.setText(""+items.get(i).getCantidad()+" unidades");
        String url = items.get(i).getUrl_imagen();
        Glide.with(context)
                .load(url.toString())
                //.fitCenter()
                //.override(800, 400)
                //.centerCrop()
                .into(holder.ivImagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalle_item = new Intent(context, detalles_inventario.class);
                detalle_item.putExtra("nombre", holder.tvNombre.getText().toString().trim());
                detalle_item.putExtra("descripcion",holder.tvDescripcion.getText().toString().trim());
                detalle_item.putExtra("cantidad", items.get(i).getCantidad());
                detalle_item.putExtra("imagen",url);
                context.startActivity(detalle_item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNombre, tvDescripcion,tvCantidad;
        public ImageView ivImagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = (TextView)itemView.findViewById(R.id.txtnombre);
            tvDescripcion = (TextView)itemView.findViewById(R.id.txtdescripcion);
            tvCantidad = (TextView)itemView.findViewById(R.id.txtcantidad);
            ivImagen = (ImageView)itemView.findViewById(R.id.imagen);
        }
    }

}