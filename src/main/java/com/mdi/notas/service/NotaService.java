package com.mdi.notas.service;

import com.mdi.notas.model.Nota;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

/**
 * Servicio encargado de gestionar las operaciones de almacenamiento de notas.
 * Las notas se guardan como archivos de texto dentro de la carpeta "data/notas",
 * utilizando un ID incremental como nombre de archivo.
 *
 * Este servicio proporciona métodos para:
 * <ul>
 *     <li>Listar todas las notas</li>
 *     <li>Guardar una nueva nota</li>
 *     <li>Buscar una nota por título</li>
 *     <li>Generar un nuevo ID incremental</li>
 * </ul>
 */
@Service
public class NotaService {

    /** Carpeta donde se almacenan los archivos de notas. */
    private final Path carpeta = Paths.get("data/notas");

    /**
     * Constructor del servicio.
     * Crea la carpeta de almacenamiento si no existe.
     *
     * @throws Exception si ocurre un error al crear la carpeta
     */
    public NotaService() throws Exception {
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
    }

    /**
     * Lista todas las notas almacenadas en la carpeta.
     * Cada archivo .txt representa una nota con el siguiente formato:
     * <pre>
     *     Título
     *     ---
     *     Contenido...
     * </pre>
     *
     * @return lista de notas cargadas desde los archivos
     * @throws Exception si ocurre un error al leer los archivos
     */
    public List<Nota> listar() throws Exception {
        List<Nota> notas = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta, "*.txt")) {
            for (Path p : stream) {
                List<String> lineas = Files.readAllLines(p);

                Nota n = new Nota();
                n.setId(Integer.parseInt(p.getFileName().toString().replace(".txt", "")));
                n.setTitulo(lineas.get(0));
                n.setContenido(String.join("\n", lineas.subList(2, lineas.size())));

                notas.add(n);
            }
        }

        return notas;
    }

    /**
     * Guarda una nueva nota en un archivo .txt.
     * El archivo se nombra con un ID incremental.
     *
     * @param nota nota a guardar
     * @throws Exception si ocurre un error al escribir el archivo
     */
    public void guardar(Nota nota) throws Exception {
        int id = generarNuevoId();
        Path archivo = carpeta.resolve(id + ".txt");

        List<String> contenido = List.of(
                nota.getTitulo(),
                "---",
                nota.getContenido()
        );

        Files.write(archivo, contenido);
    }

    /**
     * Genera un nuevo ID incremental basado en los archivos existentes.
     * Busca el ID más alto y devuelve ese valor + 1.
     *
     * @return nuevo ID disponible
     * @throws Exception si ocurre un error al leer los archivos
     */
    private int generarNuevoId() throws Exception {
        int max = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta, "*.txt")) {
            for (Path p : stream) {
                int id = Integer.parseInt(p.getFileName().toString().replace(".txt", ""));
                if (id > max) max = id;
            }
        }

        return max + 1;
    }

    /**
     * Busca una nota por su título.
     * Recorre todas las notas y devuelve la primera coincidencia exacta.
     *
     * @param titulo título de la nota a buscar
     * @return nota encontrada o null si no existe
     * @throws Exception si ocurre un error al leer los archivos
     */
    public Nota buscarPorTitulo(String titulo) throws Exception {
        for (Nota n : listar()) {
            if (n.getTitulo().equals(titulo)) {
                return n;
            }
        }
        return null;
    }
}
