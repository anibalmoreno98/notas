package com.mdi.notas.informe;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase encargada de analizar un texto y generar estadísticas básicas
 * como número de palabras, líneas, caracteres y frecuencia de palabras.
 * También permite generar un resumen formateado del análisis.
 */
public class InformeGenerator {

    /** Texto original sobre el que se realiza el análisis. */
    private final String texto;

    /**
     * Constructor del generador de informes.
     * Si el texto recibido es null, se sustituye por una cadena vacía.
     *
     * @param texto contenido a analizar
     */
    public InformeGenerator(String texto) {
        this.texto = texto == null ? "" : texto;
    }

    /**
     * Calcula el número total de palabras del texto.
     * Se consideran palabras separadas por uno o más espacios.
     *
     * @return número total de palabras
     */
    public int getTotalPalabras() {
        if (texto.isBlank()) return 0;
        return texto.trim().split("\\s+").length;
    }

    /**
     * Calcula el número de líneas del texto.
     * Las líneas se separan por saltos de línea '\n'.
     *
     * @return número total de líneas
     */
    public int getNumeroLineas() {
        if (texto.isBlank()) return 0;
        return texto.split("\n").length;
    }

    /**
     * Devuelve el número total de caracteres del texto,
     * incluyendo espacios y saltos de línea.
     *
     * @return número de caracteres
     */
    public int getNumeroCaracteres() {
        return texto.length();
    }

    /**
     * Calcula la frecuencia de las palabras del texto.
     * <p>
     * El proceso incluye:
     * <ul>
     *     <li>Convertir el texto a minúsculas</li>
     *     <li>Eliminar signos de puntuación</li>
     *     <li>Separar por espacios</li>
     *     <li>Contar ocurrencias</li>
     *     <li>Ordenar por frecuencia descendente</li>
     *     <li>Limitar a las 10 palabras más frecuentes</li>
     * </ul>
     *
     * @return mapa ordenado (LinkedHashMap) con las 10 palabras más frecuentes
     */
    public Map<String, Integer> getFrecuenciaPalabras() {
        Map<String, Integer> freq = new HashMap<>();

        // Normalizar texto
        String limpio = texto
                .toLowerCase()
                .replaceAll("[^a-záéíóúñ0-9 ]", "");

        String[] palabras = limpio.split("\\s+");

        // Contar frecuencia
        for (String p : palabras) {
            if (p.isBlank()) continue;
            freq.put(p, freq.getOrDefault(p, 0) + 1);
        }

        // Ordenar y limitar a top 10
        return freq.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * Genera un resumen formateado del análisis del texto.
     * Incluye:
     * <ul>
     *     <li>Total de palabras</li>
     *     <li>Total de líneas</li>
     *     <li>Total de caracteres</li>
     * </ul>
     *
     * @return resumen del análisis en formato de texto
     */
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
