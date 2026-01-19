package com.mdi.notas.model;

/**
 * Modelo que representa una nota dentro de la aplicación.
 * Contiene un identificador único, un título y un contenido textual.
 * Esta clase actúa como entidad simple utilizada por los servicios y controladores.
 */
public class Nota {

    /** Identificador único de la nota. */
    private int id;

    /** Título descriptivo de la nota. */
    private String titulo;

    /** Contenido completo de la nota. */
    private String contenido;

    /**
     * Obtiene el identificador único de la nota.
     *
     * @return id de la nota
     */
    public int getId() { 
        return id; 
    }

    /**
     * Establece el identificador único de la nota.
     *
     * @param id nuevo identificador
     */
    public void setId(int id) { 
        this.id = id; 
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
     * Obtiene el contenido textual de la nota.
     *
     * @return contenido de la nota
     */
    public String getContenido() { 
        return contenido; 
    }

    /**
     * Establece el contenido textual de la nota.
     *
     * @param contenido nuevo contenido
     */
    public void setContenido(String contenido) { 
        this.contenido = contenido; 
    }
}
