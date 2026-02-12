package com.mdi.model;

import java.io.Serializable;

/**
 * Representa una nota dentro del sistema.
 * Contiene un título, contenido, la carpeta a la que pertenece
 * y un indicador de si está marcada como favorita.
 *
 * <p>Implementa {@link Serializable} para permitir su almacenamiento
 * en archivos o bases de datos simples.</p>
 */
public class Nota implements Serializable {

    /** Título de la nota. */
    private String titulo;

    /** Contenido textual de la nota. */
    private String contenido;

    /** Carpeta a la que pertenece la nota. */
    private String carpeta;

    /** Indica si la nota está marcada como favorita. */
    private boolean favorita;

    /**
     * Constructor por defecto.
     * Inicializa la nota como no favorita.
     */
    public Nota() {
        this.favorita = false;
    }

    /**
     * Crea una nota con título, contenido y carpeta.
     * La nota no se marca como favorita por defecto.
     *
     * @param titulo título de la nota
     * @param contenido contenido de la nota
     * @param carpeta carpeta donde se almacena
     */
    public Nota(String titulo, String contenido, String carpeta) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.carpeta = carpeta;
        this.favorita = false;
    }

    /**
     * Crea una nota con todos sus atributos.
     *
     * @param titulo título de la nota
     * @param contenido contenido de la nota
     * @param carpeta carpeta donde se almacena
     * @param favorita indica si está marcada como favorita
     */
    public Nota(String titulo, String contenido, String carpeta, boolean favorita) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.carpeta = carpeta;
        this.favorita = favorita;
    }

    /**
     * Obtiene el título de la nota.
     *
     * @return título de la nota
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la nota.
     *
     * @param titulo nuevo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el contenido de la nota.
     *
     * @return contenido de la nota
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido de la nota.
     *
     * @param contenido nuevo contenido
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la carpeta donde se encuentra la nota.
     *
     * @return nombre de la carpeta
     */
    public String getCarpeta() {
        return carpeta;
    }

    /**
     * Establece la carpeta donde se almacena la nota.
     *
     * @param carpeta nombre de la carpeta
     */
    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    /**
     * Indica si la nota está marcada como favorita.
     *
     * @return {@code true} si es favorita, {@code false} en caso contrario
     */
    public boolean isFavorita() {
        return favorita;
    }

    /**
     * Marca o desmarca la nota como favorita.
     *
     * @param favorita nuevo estado de favorito
     */
    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }

    /**
     * Representación textual de la nota, útil para depuración.
     *
     * @return cadena con información básica de la nota
     */
    @Override
    public String toString() {
        return "Nota{" +
                "titulo='" + titulo + '\'' +
                ", carpeta='" + carpeta + '\'' +
                ", favorita=" + favorita +
                '}';
    }
}
