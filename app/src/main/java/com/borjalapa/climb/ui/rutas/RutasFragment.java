package com.borjalapa.climb.ui.rutas;

import android.content.Intent;
import android.os.Bundle;
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
import com.borjalapa.climb.recyclerviewrutas.MyAdapter2;
import com.borjalapa.climb.recyclerviewrutas.Ruta;

import java.util.ArrayList;

public class RutasFragment extends Fragment {

    Button btnAddItem;

    MyAdapter2 myAdapter2;
    RecyclerView rvLista;

    ArrayList<Ruta> rutas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutas, container, false);

        rvLista = (RecyclerView) root.findViewById(R.id.lista_rutas);
        btnAddItem = (Button) root.findViewById(R.id.btnAñadir);

        rvLista.setLayoutManager(new GridLayoutManager(getContext(), 1));

        myAdapter2 = new MyAdapter2(getContext(), rutas);

        rvLista.setAdapter(myAdapter2);

        myAdapter2.setData(rutas);

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