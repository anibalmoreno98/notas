package com.mdi.model;

import java.io.Serializable;

/**
 * Representa una carpeta dentro del sistema de notas.
 * Cada carpeta contiene un nombre y puede agrupar múltiples notas.
 * Implementa {@link Serializable} para permitir su almacenamiento o transmisión.
 */
public class Carpeta implements Serializable {

    /** Nombre identificador de la carpeta. */
    private String nombre;

    /**
     * Constructor por defecto necesario para serialización y frameworks.
     */
    public Carpeta() {
    }

    /**
     * Crea una carpeta con un nombre específico.
     *
     * @param nombre nombre de la carpeta
     */
    public Carpeta(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre de la carpeta.
     *
     * @return nombre de la carpeta
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la carpeta.
     *
     * @param nombre nuevo nombre de la carpeta
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre de la carpeta como representación en texto.
     *
     * @return nombre de la carpeta
     */
    @Override
    public String toString() {
        return nombre;
    }
}
