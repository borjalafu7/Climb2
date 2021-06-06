package com.borjalapa.climb.recyclerviewrutas;

import com.borjalapa.climb.recyclerviewinventario.Item;

import java.util.ArrayList;

public class Ruta {

    String id;
    String nombre;
    String descripcion;
    String pueblo;
    String ciudad;
    String como_llegar;
    String items;
    String url_imagen;
    String latitud;
    String longitud;

    public Ruta(String id, String nombre, String descripcion, String pueblo, String ciudad, String como_llegar, String items, String url_imagen, String latitud,String longitud) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pueblo = pueblo;
        this.ciudad = ciudad;
        this.como_llegar = como_llegar;
        this.items = items;
        this.url_imagen = url_imagen;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ruta(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPueblo() {
        return pueblo;
    }

    public void setPueblo(String pueblo) {
        this.pueblo = pueblo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComo_llegar() {
        return como_llegar;
    }

    public void setComo_llegar(String como_llegar) {
        this.como_llegar = como_llegar;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", pueblo='" + pueblo + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", como_llegar='" + como_llegar + '\'' +
                ", items='" + items + '\'' +
                ", url_imagen='" + url_imagen + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                '}';
    }
}
