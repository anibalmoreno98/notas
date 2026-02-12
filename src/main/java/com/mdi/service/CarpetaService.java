package com.mdi.service;

import com.mdi.repository.RepositoryCarpeta;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con carpetas.
 * Actúa como capa intermedia entre los controladores y el repositorio,
 * delegando las operaciones de acceso al sistema de archivos.
 */
@Service
public class CarpetaService {

    /** Repositorio encargado de la gestión física de carpetas. */
    private final RepositoryCarpeta repo;

    /**
     * Crea una instancia del servicio inyectando el repositorio correspondiente.
     *
     * @param repo repositorio de carpetas
     */
    public CarpetaService(RepositoryCarpeta repo) {
        this.repo = repo;
    }

    /**
     * Obtiene la lista de carpetas existentes.
     *
     * @return lista de nombres de carpetas
     * @throws IOException si ocurre un error al acceder al sistema de archivos
     */
    public List<String> listar() throws IOException {
        return repo.listar();
    }

    /**
     * Crea una nueva carpeta con el nombre indicado.
     *
     * @param nombre nombre de la carpeta a crear
     * @throws IOException si ocurre un error al crear la carpeta
     */
    public void crear(String nombre) throws IOException {
        repo.crear(nombre);
    }

    /**
     * Abre una carpeta y devuelve la lista de archivos de notas que contiene.
     *
     * @param nombre nombre de la carpeta a abrir
     * @return lista de archivos .txt dentro de la carpeta
     * @throws IOException si ocurre un error al acceder al sistema de archivos
     */
    public List<String> abrir(String nombre) throws IOException {
        return repo.abrir(nombre);
    }

    /**
     * Renombra una carpeta existente.
     *
     * @param actual nombre actual de la carpeta
     * @param nuevo  nuevo nombre deseado
     * @throws IOException si ocurre un error al renombrar la carpeta
     */
    public void renombrar(String actual, String nuevo) throws IOException {
        repo.renombrar(actual, nuevo);
    }

    /**
     * Elimina una carpeta y todo su contenido.
     *
     * @param nombre nombre de la carpeta a eliminar
     * @throws IOException si ocurre un error al borrar archivos o directorios
     */
    public void borrar(String nombre) throws IOException {
        repo.borrar(nombre);
    }
}
