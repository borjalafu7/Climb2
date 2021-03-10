package com.borjalapa.climb.ui.rutas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.borjalapa.climb.R;

public class RutasFragment extends Fragment {

    private RutasViewModel rutasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rutasViewModel =
                new ViewModelProvider(this).get(RutasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rutas, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        rutasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}