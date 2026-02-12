package com.mdi.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar las carpetas del sistema a nivel de archivo.
 * Utiliza el sistema de ficheros para crear, listar, renombrar y eliminar carpetas,
 * así como para obtener los archivos de notas almacenados dentro de ellas.
 *
 * <p>La carpeta base utilizada es {@code carpetas/} en el directorio raíz del proyecto.</p>
 */
@Repository
public class RepositoryCarpeta {

    /** Ruta base donde se almacenan todas las carpetas del sistema. */
    private final Path base = Paths.get("carpetas");

    /**
     * Constructor que garantiza la existencia del directorio base.
     *
     * @throws IOException si ocurre un error al crear el directorio
     */
    public RepositoryCarpeta() throws IOException {
        if (!Files.exists(base)) {
            Files.createDirectories(base);
        }
    }

    /**
     * Lista todas las carpetas existentes dentro del directorio base.
     *
     * @return lista de nombres de carpetas
     * @throws IOException si ocurre un error al acceder al sistema de archivos
     */
    public List<String> listar() throws IOException {
        List<String> lista = new ArrayList<>();

        if (!Files.exists(base)) return lista;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
            for (Path p : stream) {
                if (Files.isDirectory(p)) {
                    lista.add(p.getFileName().toString());
                }
            }
        }

        return lista;
    }

    /**
     * Crea una nueva carpeta dentro del directorio base.
     * Si ya existe, no realiza ninguna acción.
     *
     * @param nombre nombre de la carpeta a crear
     * @throws IOException si ocurre un error al crear la carpeta
     */
    public void crear(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
    }

    /**
     * Obtiene la lista de archivos de notas (.txt) dentro de una carpeta específica.
     *
     * @param nombre nombre de la carpeta a abrir
     * @return lista de nombres de archivos .txt
     * @throws IOException si ocurre un error al acceder al sistema de archivos
     */
    public List<String> abrir(String nombre) throws IOException {
        List<String> archivos = new ArrayList<>();
        Path carpeta = base.resolve(nombre);

        if (!Files.exists(carpeta)) return archivos;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta, "*.txt")) {
            for (Path p : stream) {
                archivos.add(p.getFileName().toString());
            }
        }

        return archivos;
    }

    /**
     * Renombra una carpeta existente.
     *
     * @param actual nombre actual de la carpeta
     * @param nuevo  nuevo nombre deseado
     * @throws IOException si ocurre un error al renombrar la carpeta
     */
    public void renombrar(String actual, String nuevo) throws IOException {
        Path origen = base.resolve(actual);
        Path destino = base.resolve(nuevo);

        if (Files.exists(origen)) {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Elimina una carpeta y todo su contenido.
     *
     * @param nombre nombre de la carpeta a eliminar
     * @throws IOException si ocurre un error al borrar archivos o directorios
     */
    public void borrar(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);

        if (!Files.exists(carpeta)) return;

        // Borrar archivos dentro de la carpeta
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
            for (Path p : stream) {
                Files.deleteIfExists(p);
            }
        }

        // Borrar la carpeta
        Files.deleteIfExists(carpeta);
    }
}
