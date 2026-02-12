package com.mdi.repository;

import com.mdi.model.Nota;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar el almacenamiento de notas en el sistema de archivos.
 * Cada carpeta del directorio base representa una categoría de notas, y cada nota se guarda
 * como un archivo <code>.txt</code> acompañado opcionalmente de un archivo <code>.fav</code>
 * que indica si está marcada como favorita.
 *
 * <p>Este repositorio no usa base de datos; trabaja directamente con archivos locales.</p>
 */
@Repository
public class RepositoryNota {

    /** Carpeta raíz donde se almacenan todas las carpetas de notas. */
    private final Path base = Paths.get("carpetas");

    /**
     * Lista todas las notas pertenecientes a una carpeta específica.
     * Cada nota se carga desde su archivo <code>.txt</code> y se verifica si existe
     * un archivo <code>.fav</code> asociado para marcarla como favorita.
     *
     * @param carpeta nombre de la carpeta a listar
     * @return lista de notas encontradas
     * @throws IOException si ocurre un error leyendo los archivos
     */
    public List<Nota> listarPorCarpeta(String carpeta) throws IOException {
        List<Nota> notas = new ArrayList<>();
        Path dir = base.resolve(carpeta);

        if (!Files.exists(dir)) return notas;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : stream) {
                String contenido = Files.readString(p);
                String titulo = p.getFileName().toString().replace(".txt", "");

                Nota n = new Nota(titulo, contenido, carpeta);

                Path fav = dir.resolve(titulo + ".fav");
                n.setFavorita(Files.exists(fav));

                notas.add(n);
            }
        }
        return notas;
    }

    /**
     * Guarda una nota en el sistema de archivos.
     * Crea o sobrescribe el archivo <code>.txt</code> correspondiente
     * y gestiona el archivo <code>.fav</code> según el estado de favorito.
     *
     * @param nota nota a guardar
     * @throws IOException si ocurre un error al escribir los archivos
     */
    public void guardar(Nota nota) throws IOException {
        Path dir = base.resolve(nota.getCarpeta());
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path archivo = dir.resolve(nota.getTitulo() + ".txt");
        Files.writeString(archivo, nota.getContenido(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        Path fav = dir.resolve(nota.getTitulo() + ".fav");
        if (nota.isFavorita()) {
            if (!Files.exists(fav)) Files.createFile(fav);
        } else {
            if (Files.exists(fav)) Files.delete(fav);
        }
    }

    /**
     * Elimina de forma definitiva una nota y su archivo de favorito.
     *
     * @param carpeta carpeta donde se encuentra la nota
     * @param titulo título de la nota a eliminar
     * @throws IOException si ocurre un error al borrar los archivos
     */
    public void borrar(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");
        Path fav = base.resolve(carpeta).resolve(titulo + ".fav");

        if (Files.exists(archivo)) Files.delete(archivo);
        if (Files.exists(fav)) Files.delete(fav);
    }

    /**
     * Busca una nota por su título dentro de una carpeta específica.
     *
     * @param carpeta carpeta donde buscar
     * @param titulo título de la nota
     * @return la nota encontrada o <code>null</code> si no existe
     * @throws IOException si ocurre un error leyendo el archivo
     */
    public Nota buscarPorTitulo(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");

        if (!Files.exists(archivo)) return null;

        String contenido = Files.readString(archivo);
        Nota n = new Nota(titulo, contenido, carpeta);

        Path fav = base.resolve(carpeta).resolve(titulo + ".fav");
        n.setFavorita(Files.exists(fav));

        return n;
    }

    /**
     * Lista todas las notas existentes en todas las carpetas.
     * Recorre recursivamente el directorio base y carga cada archivo <code>.txt</code>.
     *
     * @return lista completa de notas
     */
    public List<Nota> findAll() {
        List<Nota> lista = new ArrayList<>();

        File baseDir = base.toFile();
        if (!baseDir.exists()) return lista;

        for (File carpeta : baseDir.listFiles()) {
            if (carpeta.isDirectory()) {
                for (File archivo : carpeta.listFiles()) {
                    if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
                        Nota n = cargarNotaDesdeArchivo(archivo, carpeta.getName());
                        if (n != null) lista.add(n);
                    }
                }
            }
        }

        return lista;
    }

    /**
     * Carga una nota desde un archivo físico.
     * Se utiliza internamente para reconstruir objetos Nota desde el sistema de archivos.
     *
     * @param archivo archivo <code>.txt</code> que contiene la nota
     * @param carpeta carpeta donde se encuentra
     * @return la nota cargada o <code>null</code> si ocurre un error
     */
    private Nota cargarNotaDesdeArchivo(File archivo, String carpeta) {
        try {
            String titulo = archivo.getName().replace(".txt", "");
            String contenido = Files.readString(archivo.toPath());

            Nota n = new Nota();
            n.setTitulo(titulo);
            n.setContenido(contenido);
            n.setCarpeta(carpeta);

            File favFile = new File(archivo.getParent(), titulo + ".fav");
            n.setFavorita(favFile.exists());

            return n;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
