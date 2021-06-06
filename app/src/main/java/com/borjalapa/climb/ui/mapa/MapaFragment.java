package com.borjalapa.climb.ui.mapa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.borjalapa.climb.Main_Page;
import com.borjalapa.climb.R;
import com.borjalapa.climb.add_ruta;
import com.borjalapa.climb.recyclerviewrutas.MyAdapter2;
import com.borjalapa.climb.recyclerviewrutas.Ruta;
import com.borjalapa.climb.ui.rutas.RutasFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private double mLatitude = 0.0, mLongitude = 0.0;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Ruta> rutas = new ArrayList<>();

    LatLng posicion_exacta;
    double latitud_exacta = 0.0;
    double longitud_exacta = 0.0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_actividad_mapa, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;

    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LatLng sitio_click = latLng;
                mMap.addMarker(new MarkerOptions()
                        .position(sitio_click)
                        .title("Nueva ruta")).setTag("marker");
                mMap.setOnInfoWindowClickListener(MapaFragment.this::onInfoWindowClick);
            }
        });

        //habilitar la localizacion en la que te encuentres
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

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

                            for (int i=0;i<rutas.size();i++){
                                double latitud_ruta = Double.parseDouble(rutas.get(i).getLatitud());
                                double longitud_ruta = Double.parseDouble(rutas.get(i).getLongitud());
                                LatLng varianble_posicion = new LatLng(latitud_ruta,longitud_ruta);

                                //PONER ICONO DE MAP PARA LAS RUTAS
                                mMap.addMarker(new MarkerOptions().position(varianble_posicion).title(rutas.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ruta2))).setTag("Marcador");
                                mMap.setOnInfoWindowClickListener(MapaFragment.this::onInfoWindowClick);
                            }

                            Log.d("DATOS", "onSuccess: " + rutas);
                        }
                    }
                });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        posicion_exacta = marker.getPosition();
        latitud_exacta = posicion_exacta.latitude;
        longitud_exacta = posicion_exacta.longitude;

        if (marker.getTag().toString().equals("marker")) {
            mostrardialogo(MapaFragment.this);
        }else{
        }
    }


    public void mostrardialogo(MapaFragment view) {

        MiDialogo md = new MiDialogo(getContext(), mLatitude, mLongitude, new MiDialogo.RespuestaDialogo() {
            @Override
            public void OnAccept(String cadena) {
                Intent ir_ruta = new Intent(getActivity(), add_ruta.class);
                ir_ruta.putExtra("latitud",latitud_exacta);
                ir_ruta.putExtra("longitud",longitud_exacta);
                startActivity(ir_ruta);
            }

            @Override
            public void OnCancel(String cadena) throws Throwable {
                finalize();
            }

        });

        md.MostrarDialogoBotones().show();


    }



}