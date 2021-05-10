package com.borjalapa.climb.ui.rutas;

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

import com.borjalapa.climb.R;
import com.borjalapa.climb.add_ruta;
import com.borjalapa.climb.recyclerviewinventario.Item;
import com.borjalapa.climb.recyclerviewinventario.MyAdapter;
import com.borjalapa.climb.recyclerviewrutas.MyAdapter2;
import com.borjalapa.climb.recyclerviewrutas.Ruta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RutasFragment extends Fragment {

    Button btnAddItem;

    MyAdapter2 myAdapter2;
    RecyclerView rvLista;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<Ruta> rutas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutas, container, false);

        rvLista = (RecyclerView) root.findViewById(R.id.lista_rutas);
        btnAddItem = (Button) root.findViewById(R.id.btnAñadir);

        db = FirebaseFirestore.getInstance();
        String user_id= mAuth.getUid();

        //LECTURA DE DATOS DE FIREBASE POR COLECCION
        db.collection("usuarios").document(user_id).collection("rutas").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("DATOS", "onSuccess: LIST EMPTY");
                            return;
                        } else {

                            List<Ruta> rutas_lista = documentSnapshots.toObjects(Ruta.class);

                            rutas.addAll(rutas_lista);

                            rvLista.setLayoutManager(new GridLayoutManager(getContext(), 1));

                            myAdapter2 = new MyAdapter2(getContext(), rutas);

                            rvLista.setAdapter(myAdapter2);

                            myAdapter2.setData(rutas);

                            Log.d("DATOS", "onSuccess: " + rutas);
                        }
                    }
                });


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(getContext(), add_ruta.class);
                startActivity(volver);
            }
        });


        return root;
    }
}