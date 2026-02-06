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
        if (!Files.exists(base)) Files.createDirectory(base);
    }

    public List<String> listar() throws IOException {
        List<String> lista = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
            for (Path p : stream) {
                if (Files.isDirectory(p)) lista.add(p.getFileName().toString());
            }
        }
        return lista;
    }

    public void crear(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);
        if (!Files.exists(carpeta)) Files.createDirectory(carpeta);
    }

    public List<String> abrir(String nombre) throws IOException {
        List<String> archivos = new ArrayList<>();
        Path carpeta = base.resolve(nombre);

        if (!Files.exists(carpeta)) return archivos;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta, "*.txt")) {
            for (Path p : stream) archivos.add(p.getFileName().toString());
        }
        return archivos;
    }

    public void renombrar(String actual, String nuevo) throws IOException {
        Path origen = base.resolve(actual);
        Path destino = base.resolve(nuevo);
        if (Files.exists(origen)) Files.move(origen, destino);
    }

    public void borrar(String nombre) throws IOException {
        Path carpeta = base.resolve(nombre);
        if (!Files.exists(carpeta)) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
            for (Path p : stream) Files.delete(p);
        }

        Files.delete(carpeta);
    }
}
