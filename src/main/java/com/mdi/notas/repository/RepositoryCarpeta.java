package com.mdi.notas.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryCarpeta {

    private final Path base = Paths.get("carpetas");

    public RepositoryCarpeta() throws IOException {
        if (!Files.exists(base)) {
            Files.createDirectories(base);
        }
    }

    // ------------------------------
    // LISTAR CARPETAS
    // ------------------------------
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

    // ------------------------------
    // CREAR CARPETA
    // ------------------------------
    public void crear(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
    }

    // ------------------------------
    // ABRIR CARPETA (listar archivos .txt)
    // ------------------------------
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

    // ------------------------------
    // RENOMBRAR CARPETA
    // ------------------------------
    public void renombrar(String actual, String nuevo) throws IOException {
        Path origen = base.resolve(actual);
        Path destino = base.resolve(nuevo);

        if (Files.exists(origen)) {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ------------------------------
    // BORRAR CARPETA (y su contenido)
    // ------------------------------
    public void borrar(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);

        if (!Files.exists(carpeta)) return;

        // Borrar archivos dentro
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
            for (Path p : stream) {
                Files.deleteIfExists(p);
            }
        }

        // Borrar carpeta
        Files.deleteIfExists(carpeta);
    }
}
