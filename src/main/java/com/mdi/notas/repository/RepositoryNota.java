package com.mdi.notas.repository;

import com.mdi.notas.model.Nota;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryNota {

    // Carpeta raíz donde guardas todo
    private final Path base = Paths.get("carpetas");

    // ------------------------------
    // LISTAR NOTAS POR CARPETA
    // ------------------------------
    public List<Nota> listarPorCarpeta(String carpeta) throws IOException {
        List<Nota> notas = new ArrayList<>();
        Path dir = base.resolve(carpeta);

        if (!Files.exists(dir)) return notas;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : stream) {
                String contenido = Files.readString(p);
                String titulo = p.getFileName().toString().replace(".txt", "");

                Nota n = new Nota(titulo, contenido, carpeta);

                // cargar favorito
                Path fav = dir.resolve(titulo + ".fav");
                n.setFavorita(Files.exists(fav));

                notas.add(n);
            }
        }
        return notas;
    }

    // ------------------------------
    // GUARDAR NOTA
    // ------------------------------
    public void guardar(Nota nota) throws IOException {
        Path dir = base.resolve(nota.getCarpeta());
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path archivo = dir.resolve(nota.getTitulo() + ".txt");
        Files.writeString(archivo, nota.getContenido(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Guardar favorito
        Path fav = dir.resolve(nota.getTitulo() + ".fav");
        if (nota.isFavorita()) {
            if (!Files.exists(fav)) Files.createFile(fav);
        } else {
            if (Files.exists(fav)) Files.delete(fav);
        }
    }

    // ------------------------------
    // BORRAR NOTA
    // ------------------------------
    public void borrar(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");
        Path fav = base.resolve(carpeta).resolve(titulo + ".fav");

        if (Files.exists(archivo)) Files.delete(archivo);
        if (Files.exists(fav)) Files.delete(fav);
    }

    // ------------------------------
    // BUSCAR NOTA POR TÍTULO
    // ------------------------------
    public Nota buscarPorTitulo(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");

        if (!Files.exists(archivo)) return null;

        String contenido = Files.readString(archivo);
        Nota n = new Nota(titulo, contenido, carpeta);

        Path fav = base.resolve(carpeta).resolve(titulo + ".fav");
        n.setFavorita(Files.exists(fav));

        return n;
    }

    // ------------------------------
    // LISTAR TODAS LAS NOTAS (para Favoritos)
    // ------------------------------
    public List<Nota> findAll() {
        List<Nota> lista = new ArrayList<>();

        File baseDir = base.toFile(); // carpeta "carpetas"

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

    // ------------------------------
    // CARGAR NOTA DESDE ARCHIVO
    // ------------------------------
    private Nota cargarNotaDesdeArchivo(File archivo, String carpeta) {
        try {
            String titulo = archivo.getName().replace(".txt", "");
            String contenido = Files.readString(archivo.toPath());

            Nota n = new Nota();
            n.setTitulo(titulo);
            n.setContenido(contenido);
            n.setCarpeta(carpeta);

            // archivo .fav
            File favFile = new File(archivo.getParent(), titulo + ".fav");
            n.setFavorita(favFile.exists());

            return n;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void borrarNota(String carpeta, String titulo) throws IOException {
        Path archivo = base.resolve(carpeta).resolve(titulo + ".txt");
        Path fav = base.resolve(carpeta).resolve(titulo + ".fav");

        if (Files.exists(archivo)) Files.delete(archivo);
        if (Files.exists(fav)) Files.delete(fav);
    }

}
