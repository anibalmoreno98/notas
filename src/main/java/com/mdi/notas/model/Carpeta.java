package com.mdi.notas.model;

import java.io.Serializable;

public class Carpeta implements Serializable {

    private String nombre;

    public Carpeta() {
    }

    public Carpeta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
