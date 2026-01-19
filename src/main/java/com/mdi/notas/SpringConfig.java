package com.mdi.notas;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración principal de Spring para la aplicación.
 * <p>
 * Esta clase habilita el escaneo automático de componentes dentro del paquete
 * {@code com.mdi.notas}, permitiendo que Spring detecte y registre:
 * </p>
 * <ul>
 *     <li>Controladores (@Controller)</li>
 *     <li>Servicios (@Service)</li>
 *     <li>Componentes (@Component)</li>
 * </ul>
 * <p>
 * Es utilizada por {@link App} para inicializar el contexto de Spring
 * antes de cargar la interfaz JavaFX.
 * </p>
 */
@Configuration
@ComponentScan("com.mdi.notas")
public class SpringConfig {
}
