package com.mdi.notas.model;

public class Nota {

    private String titulo;
    private String contenido;
    private String carpeta;

    public Nota() {
    }

    public Nota(String titulo, String contenido, String carpeta) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.carpeta = carpeta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }
}
