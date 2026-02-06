package com.mdi.notas.service;

import com.mdi.notas.repository.RepositoryCarpeta;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CarpetaService {

    private final RepositoryCarpeta repo;

    public CarpetaService(RepositoryCarpeta repo) {
        this.repo = repo;
    }

    public List<String> listar() throws IOException {
        return repo.listar();
    }

    public void crear(String nombre) throws IOException {
        repo.crear(nombre);
    }

    public List<String> abrir(String nombre) throws IOException {
        return repo.abrir(nombre);
    }

    public void renombrar(String actual, String nuevo) throws IOException {
        repo.renombrar(actual, nuevo);
    }

    public void borrar(String nombre) throws IOException {
        repo.borrar(nombre);
    }
}
