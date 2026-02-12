package com.mdi;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración principal de Spring para la aplicación.
 *
 * <p>Esta clase habilita el escaneo automático de componentes dentro del paquete
 * {@code com.mdi}, permitiendo que Spring detecte y registre automáticamente:</p>
 *
 * <ul>
 *     <li>Servicios anotados con {@code @Service}</li>
 *     <li>Repositorios anotados con {@code @Repository}</li>
 *     <li>Componentes generales anotados con {@code @Component}</li>
 * </ul>
 *
 * <p>Es utilizada por {@link App} para inicializar el contexto de Spring
 * antes de cargar la interfaz JavaFX.</p>
 */
@Configuration
@ComponentScan("com.mdi")
public class SpringConfig {
}
