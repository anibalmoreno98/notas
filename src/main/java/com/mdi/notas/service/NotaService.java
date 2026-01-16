package com.mdi.notas.service;

import com.mdi.notas.model.Nota;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

@Service
public class NotaService {

    private final Path carpeta = Paths.get("data/notas");

    public NotaService() throws Exception {
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
    }

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

    public Nota buscarPorTitulo(String titulo) throws Exception {
        for (Nota n : listar()) {
            if (n.getTitulo().equals(titulo)) {
                return n;
            }
        }
        return null;
    }
}

