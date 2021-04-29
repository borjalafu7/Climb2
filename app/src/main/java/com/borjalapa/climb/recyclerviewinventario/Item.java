package com.borjalapa.climb.recyclerviewinventario;

public class Item {

    String id;
    String nombre;
    String descripcion;
    Integer cantidad;
    String url_imagen;

    public Item(String id,String nombre, String descripcion, Integer cantidad, String url_imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.url_imagen = url_imagen;
        this.id = id;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    @Override
    public String toString() {
        return "Item{" + "nombre='" + nombre + '\'' + ", descripcion='" + descripcion + '\'' + ", cantidad=" + cantidad + '}';
    }
}
