# MDI Notas  
Aplicación de escritorio desarrollada en **JavaFX + Spring**, diseñada para gestionar notas de forma sencilla mediante una interfaz estilo **MDI (Multiple Document Interface)**.  
Incluye análisis automático del texto, generación de informes y almacenamiento persistente en archivos locales.

---

## Características principales

- **Interfaz MDI**: navegación fluida entre ventanas internas.
- **Gestión de notas**:
  - Crear nuevas notas.
  - Listar notas existentes.
  - Visualizar notas en ventanas independientes.
- **Análisis de texto**:
  - Conteo de palabras.
  - Número de líneas.
  - Número de caracteres.
  - Frecuencia de palabras (Top 10).
- **Generación de informes**:
  - Resumen automático.
  - Gráfico de barras con frecuencias.
  - Exportación a archivo `.txt`.
- **Persistencia local**:
  - Las notas se guardan como archivos `.txt` en `data/notas/`.
  - ID incremental automático.

---

## Arquitectura del proyecto

El proyecto sigue una estructura modular clara:

src/main/java/com/mdi/notas/
│
├── App.java                  # Punto de entrada JavaFX + Spring
├── SpringConfig.java         # Configuración de Spring
│
├── controller/              # Controladores JavaFX
│   ├── NotaFxController.java
│   ├── ListaNotasController.java
│   └── FormNuevaController.java
│
├── informe/                 # Lógica de análisis e informes
│   ├── InformeController.java
│   └── InformeGenerator.java
│
├── model/
│   └── Nota.java             # Entidad Nota
│
└── service/
└── NotaService.java      # Persistencia basada en archivos


---

## Requisitos

- **Java 17** o superior  
- **JavaFX 17+**  
- **Spring Framework (Context)**  
- Sistema operativo compatible con Java (Windows, Linux, macOS)

---

##  Ejecución del proyecto

1. Clona el repositorio:

```bash
git clone https://github.com/anibalmoreno98/notas.git
```

2. Asegúrate de tener JavaFX configurado en tu IDE o en tu PATH.

3. Ejecuta la aplicación:
mvn clean install
mvn javafx:run

O desde tu IDE:
Ejecutar App.java

---

## Estructura de almacenamiento

Las notas se guardan automáticamente en:
data/notas/

Cada nota se almacena como:
<ID>.txt

Con el formato:
Título
---
Contenido...


## Análisis de texto

El módulo de análisis permite obtener:

    Total de palabras

    Total de líneas

    Total de caracteres

    Top 10 palabras más frecuentes

    Gráfico de barras generado con JavaFX

Ejemplo de resumen generado:
INFORME DEL DOCUMENTO
---------------------
Palabras: 120
Líneas: 8
Caracteres: 650

## Exportación de informes

Desde la ventana de informe puedes exportar un archivo .txt que incluye:

    Resumen del documento

    Frecuencia de palabras

## Tecnologías utilizadas

    Java 17

    JavaFX

    Spring Framework

    FXML

    CSS (opcional para estilos)

    FileSystem API (NIO.2)

## Autor

Aníbal  
Desarrollador Java con enfoque en arquitectura limpia, modularidad y aplicaciones de escritorio profesionales.

## Licencia

Este proyecto puede distribuirse libremente para fines educativos o personales.
Para uso comercial, consultar previamente.