package com.mdi.notas.informe;

import java.util.*;
import java.util.stream.Collectors;

public class InformeGenerator {

    private final String texto;

    public InformeGenerator(String texto) {
        this.texto = texto == null ? "" : texto;
    }

    public int getTotalPalabras() {
        if (texto.isBlank()) return 0;
        return texto.trim().split("\\s+").length;
    }

    public int getNumeroLineas() {
        if (texto.isBlank()) return 0;
        return texto.split("\n").length;
    }

    public int getNumeroCaracteres() {
        return texto.length();
    }

    public Map<String, Integer> getFrecuenciaPalabras() {
        Map<String, Integer> freq = new HashMap<>();

        String limpio = texto
                .toLowerCase()
                .replaceAll("[^a-záéíóúñ0-9 ]", "");

        String[] palabras = limpio.split("\\s+");

        for (String p : palabras) {
            if (p.isBlank()) continue;
            freq.put(p, freq.getOrDefault(p, 0) + 1);
        }

        return freq.entrySet()
                .stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a,b)->a,
                        LinkedHashMap::new
                ));
    }

    public String generarResumen() {
        return """
                INFORME DEL DOCUMENTO
                ---------------------
                Palabras: %d
                Líneas: %d
                Caracteres: %d
                """.formatted(
                getTotalPalabras(),
                getNumeroLineas(),
                getNumeroCaracteres()
        );
    }
}
