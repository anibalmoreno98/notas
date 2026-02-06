package com.mdi.notas.repository;

import com.mdi.notas.model.Nota;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryNota {

    private final Path base = Paths.get("carpetas");

    public List<Nota> listarPorCarpeta(String carpeta) throws IOException {
        List<Nota> notas = new ArrayList<>();
        Path dir = base.resolve(carpeta);

        if (!Files.exists(dir)) return notas;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : stream) {
                String contenido = Files.readString(p);
                String titulo = p.getFileName().toString().replace(".txt", "");
                notas.add(new Nota(titulo, contenido, carpeta));
            }
        }
        return notas;
    }

    public void guardar(Nota nota) throws IOException {
        Path dir = base.resolve(nota.getCarpeta());
        if (!Files.exists(dir)) Files.createDirectory(dir);

        Path archivo = dir.resolve(nota.getTitulo() + ".txt");
        Files.writeString(archivo, nota.getContenido(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void borrar(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");
        if (Files.exists(archivo)) Files.delete(archivo);
    }
}
