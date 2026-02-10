package com.mdi.notas.service;

import com.mdi.notas.model.Nota;
import com.mdi.notas.repository.RepositoryNota;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NotaService {

    private final RepositoryNota repo;

    public NotaService(RepositoryNota repo) {
        this.repo = repo;
    }

    public List<Nota> listarPorCarpeta(String carpeta) throws IOException {
        return repo.listarPorCarpeta(carpeta);
    }

    public void guardar(Nota nota) throws IOException {
        repo.guardar(nota);
    }

    public void moverAEliminadas(Nota nota) throws IOException {
        nota.setCarpeta("Eliminadas");
        repo.guardar(nota);
    }

    public void borrarDefinitivamente(String carpeta, String titulo) throws IOException {
        repo.borrar(carpeta, titulo);
    }

    public Nota buscarPorTitulo(String carpeta, String titulo) throws IOException {
        return repo.buscarPorTitulo(carpeta, titulo);
    }

    public List<Nota> listarTodas() {
        return repo.findAll();
    }

    public Nota buscarEnTodas(String titulo) {
        List<Nota> todas = repo.findAll();
        for (Nota n : todas) {
            if (n.getTitulo().equals(titulo)) {
                return n;
            }
        }
        return null;
    }

    public List<Nota> listarFavoritas() {
        return repo.findAll().stream()
                .filter(Nota::isFavorita)
                .toList();
    }
}
