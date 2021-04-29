package com.borjalapa.climb.recyclerviewinventario;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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