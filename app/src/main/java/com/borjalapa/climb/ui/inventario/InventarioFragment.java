package com.borjalapa.climb.ui.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjalapa.climb.MainActivity;
import com.borjalapa.climb.R;
import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.recyclerviewinventario.MyAdapter;
import com.borjalapa.climb.add_inventario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventarioFragment extends Fragment {

    Button btnAddItem;

    MyAdapter myAdapter;
    RecyclerView rvLista;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<Item> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventario, container, false);

        rvLista = (RecyclerView) root.findViewById(R.id.lista);
        btnAddItem = (Button) root.findViewById(R.id.btnAñadir);

        db = FirebaseFirestore.getInstance();
        String user_id= mAuth.getUid();

        //db.collection("usuarios").document(user_id).collection("inventario").get().;

        rvLista.setLayoutManager(new GridLayoutManager(getContext(), 1));

        myAdapter = new MyAdapter(getContext(), items);

        rvLista.setAdapter(myAdapter);

        myAdapter.setData(items);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_inventario = new Intent(getContext(), add_inventario.class);
                startActivity(add_inventario);
            }
        });

        return root;

    }


}