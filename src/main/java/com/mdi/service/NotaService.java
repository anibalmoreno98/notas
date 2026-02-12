package com.mdi.service;

import com.mdi.model.Nota;
import com.mdi.repository.RepositoryNota;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con las notas.
 * Actúa como intermediario entre los controladores y el repositorio de almacenamiento.
 *
 * <p>Proporciona operaciones como listar, guardar, mover a eliminadas,
 * eliminar definitivamente y buscar notas por distintos criterios.</p>
 */
@Service
public class NotaService {

    /** Repositorio encargado del acceso a los archivos de notas. */
    private final RepositoryNota repo;

    /**
     * Crea una instancia del servicio inyectando el repositorio correspondiente.
     *
     * @param repo repositorio de notas
     */
    public NotaService(RepositoryNota repo) {
        this.repo = repo;
    }

    /**
     * Obtiene todas las notas pertenecientes a una carpeta específica.
     *
     * @param carpeta nombre de la carpeta
     * @return lista de notas encontradas
     * @throws IOException si ocurre un error leyendo los archivos
     */
    public List<Nota> listarPorCarpeta(String carpeta) throws IOException {
        return repo.listarPorCarpeta(carpeta);
    }

    /**
     * Guarda una nota en el sistema de archivos.
     * Si ya existe, se sobrescribe.
     *
     * @param nota nota a guardar
     * @throws IOException si ocurre un error al escribir los archivos
     */
    public void guardar(Nota nota) throws IOException {
        repo.guardar(nota);
    }

    /**
     * Mueve una nota a la carpeta "Eliminadas".
     * No la borra, solo cambia su ubicación.
     *
     * @param nota nota a mover
     * @throws IOException si ocurre un error al guardar la nota
     */
    public void moverAEliminadas(Nota nota) throws IOException {
        nota.setCarpeta("Eliminadas");
        repo.guardar(nota);
    }

    /**
     * Elimina una nota de forma definitiva del sistema de archivos.
     *
     * @param carpeta carpeta donde se encuentra la nota
     * @param titulo título de la nota
     * @throws IOException si ocurre un error al borrar los archivos
     */
    public void borrarDefinitivamente(String carpeta, String titulo) throws IOException {
        repo.borrar(carpeta, titulo);
    }

    /**
     * Busca una nota por su título dentro de una carpeta específica.
     *
     * @param carpeta carpeta donde buscar
     * @param titulo título de la nota
     * @return la nota encontrada o {@code null} si no existe
     * @throws IOException si ocurre un error leyendo el archivo
     */
    public Nota buscarPorTitulo(String carpeta, String titulo) throws IOException {
        return repo.buscarPorTitulo(carpeta, titulo);
    }

    /**
     * Lista todas las notas existentes en todas las carpetas.
     *
     * @return lista completa de notas
     */
    public List<Nota> listarTodas() {
        return repo.findAll();
    }

    /**
     * Busca una nota por título recorriendo todas las carpetas.
     *
     * @param titulo título de la nota
     * @return la nota encontrada o {@code null} si no existe
     */
    public Nota buscarEnTodas(String titulo) {
        List<Nota> todas = repo.findAll();
        for (Nota n : todas) {
            if (n.getTitulo().equals(titulo)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Obtiene todas las notas marcadas como favoritas.
     *
     * @return lista de notas favoritas
     */
    public List<Nota> listarFavoritas() {
        return repo.findAll().stream()
                .filter(Nota::isFavorita)
                .toList();
    }
}
