# Practica de conceptos básicos de Spring - Aspectos
- **Nombre:** Gerardo Isidro Herrera Pacheco
- **Matrícula:** ISC 68612
- **Semestre:** 8vo
- **Materia**: Temas selectos de Programación
- **Maestro:** Jose C Aguilar Canepa
- **Institución:** Universidad Autónoma de Campeche, Facultad de Ingeniería

# Sistema de Censura de Mensajes con Spring AOP

Este proyecto implementa una aplicación en Java utilizando Spring Framework que permite a los usuarios enviar mensajes desde consola o una interfaz gráfica. El sistema detecta palabras prohibidas en el contenido del mensaje y aplica un proceso de censura utilizando la programación orientada a aspectos (AOP).

## Estructura del Proyecto

```
src/main/java/
├── anotaciones
│   └── RevisarContenido.java
├── aspectos
│   └── CensorAspect.java
├── config
│   └── ProjectConfiguration.java
├── gui
│   └── MensajeGUI.java
├── main
│   ├── AppLauncher.java
│   └── Main.java
├── models
│   └── Mensaje.java
├── services
│   ├── impl
│   │   └── MensajeServiceImpl.java
│   ├── CensuraService.java
│   └── MensajeService.java
```

## Características  
- Interfaz dual: consola y GUI (Swing)
- Detección de palabras prohibidas
- Censura de mensajes con caracteres especiales (`!#?%@`)
- Bloqueo completo de mensajes con más de 3 palabras prohibidas
- Implementación basada en aspectos para interceptar la lógica de censura


##  Diagrama de Clases UML

![Diagrama de clases para CensureAplication](https://github.com/user-attachments/assets/39fad56c-12bf-4672-b2a5-5c0b626ad74b)


## Componentes Principales

### 1. Modelo de Datos (`models`)

#### `Mensaje.java`
Define la estructura de un mensaje con:
- Contenido textual
- Autor
- Estado de censura

```java
@Component
public class Mensaje {
    private String contenido;
    private String autor;
    private boolean censurado;
    // Getters y setters
}
```

### 2. Anotaciones Personalizadas (`anotaciones`)

#### `RevisarContenido.java`
Anotación personalizada para marcar métodos que deben ser interceptados para censura.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RevisarContenido {
    String tipoCensura() default "default";
}
```

### 3. Aspectos (`aspectos`)

#### `CensorAspect.java`
Implementa la lógica AOP para interceptar y censurar mensajes.

```java
@Aspect
@Component
public class CensorAspect {
    @Around("@annotation(revisarContenido)")
    public Object censurarContenido(ProceedingJoinPoint joinPoint, 
                                   RevisarContenido revisarContenido) throws Throwable {
        // Lógica de censura
    }
}
```

Este aspecto:
- Intercepta métodos anotados con `@RevisarContenido`
- Extrae el mensaje de los argumentos
- Aplica censura usando `CensuraService`
- Modifica el mensaje original
- Continúa con la ejecución del método original

### 4. Servicios (`services`)

#### `CensuraService.java`
Servicio principal para la lógica de censura:
- Mantiene lista de palabras prohibidas
- Detecta palabras prohibidas (insensible a mayúsculas y acentos)
- Reemplaza palabras prohibidas por caracteres especiales
- Bloquea mensajes con más de 3 palabras prohibidas

```java
@Component
public class CensuraService {
    private final Set<String> palabrasProhibidas = new HashSet<>(Arrays.asList(
        "recorcholis", "bobo", "imbecil", "gay", "arturo",
        "estupido", "feo", "tonto", "wey", "cabron", "idiota"));
    
    // Métodos de censura
}
```

#### `MensajeService.java` e `Impl`
Interfaz e implementación para el procesamiento de mensajes:

```java
public interface MensajeService {
    Mensaje procesarMensaje(Mensaje mensaje);
    void mostrarMensaje(Mensaje mensaje);
}

@Service
public class MensajeServiceImpl implements MensajeService {
    @Override
    @RevisarContenido
    public Mensaje procesarMensaje(Mensaje mensaje) {
        // La lógica de censura se ejecuta por el aspecto
        return mensaje;
    }
}
```

### 5. Configuración (`config`)

#### `ProjectConfiguration.java`
Habilita AOP y configura el escaneo de componentes:

```java
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"models", "services", "aspectos"})
public class ProjectConfiguration {
}
```

### 6. Interfaces de Usuario

#### `Main.java`
Implementa la interfaz de consola:
- Inicializa el contexto de Spring
- Proporciona menú interactivo
- Utiliza `MensajeService` para procesar mensajes

#### `MensajeGUI.java`
Implementa la interfaz gráfica usando Swing:
- Campos para autor y mensaje
- Botón para enviar
- Visualización de mensajes procesados

#### `AppLauncher.java`
Permite elegir entre la interfaz de consola o gráfica.

## Interacción de los Componentes

1. **Flujo principal**:
   - El usuario escribe un mensaje (GUI o consola)
   - `MensajeService.procesarMensaje()` se invoca con el mensaje
   - El aspecto intercepta la llamada al estar anotada con `@RevisarContenido`
   - `CensorAspect` utiliza `CensuraService` para analizar y censurar
   - El mensaje modificado continúa al método original
   - Se muestra el resultado al usuario

2. **Implementación AOP**:
   - Spring AOP crea un proxy alrededor de `MensajeService`
   - Cuando se invoca `procesarMensaje()`, el proxy redirige al aspecto
   - El aspecto ejecuta su lógica antes/después de la llamada original

## Tecnologías y Librerías Utilizadas

- **Spring Framework 6.1.5/6.2.4**:
  - `spring-context`: Contenedor IoC y gestión de beans
  - `spring-aspects`: Soporte para AOP
  - `spring-jdbc`: Dependencia para Spring core

- **AspectJ**:
  - Habilitado mediante `@EnableAspectJAutoProxy`
  - Anotaciones: `@Aspect`, `@Around`, etc.
  - Point cuts: expresiones para seleccionar puntos de interceptación

## Conceptos AOP Aplicados

1. **Aspecto (Aspect)**: `CensorAspect` encapsula la funcionalidad transversal de censura.

2. **Punto de Corte (Pointcut)**: Define dónde se aplica el aspecto:
   - `@annotation(revisarContenido)`: Métodos con anotación
   - `execution(* services.MensajeService.procesarMensaje(..))`: Método específico

3. **Consejo (Advice)**: `@Around` define cómo y cuándo se ejecuta:
   - Accede a argumentos antes de la ejecución
   - Modifica los argumentos
   - Controla la ejecución del método original

4. **Unión (Join Point)**: Representado por `ProceedingJoinPoint`, permite:
   - Acceder a argumentos
   - Modificar argumentos
   - Ejecutar o evitar la ejecución del método interceptado

5. **Anotación Personalizada**: `@RevisarContenido` para marcar métodos a interceptar

   ## Proceso de uso de la aplicación
   
   ### Paso 1: Selección de Interfaz

   ![Elegir UI o consola](https://github.com/user-attachments/assets/e75dbc3a-6d6a-4ff6-8822-297548a53580)

- La aplicación inicia mostrando un cuadro de diálogo que pregunta al usuario cómo desea ejecutar la aplicación.
- Las opciones disponibles son:
  - **Interfaz Gráfica**: Para usar una GUI (Graphical User Interface)
  - **Consola**: Para usar la interfaz de línea de comandos


  ### Paso 2: Interfaz Gráfica de Mensajes

  ![Escribir mensaje en UI](https://github.com/user-attachments/assets/79452664-2197-4667-9396-6d47b5bdd46f)


- Cuando se selecciona la interfaz gráfica, se muestra una ventana con:
  1. **Campo "Tu nombre"**: Donde el usuario ingresa su nombre (en este caso "Arturo")
  2. **Área de texto "Escribe tu mensaje"**: Donde el usuario escribe su mensaje (contiene texto ofensivo)
  3. **Área "Mensaje procesado"**: Vacía, esperando el resultado del procesamiento
  4. **Botón "Enviar Mensaje"**: Para enviar el mensaje al sistema de procesamiento
 
  ![Mensaje procesado](https://github.com/user-attachments/assets/03b1b2f8-bfb7-4fc6-bb0b-ba0b38af9431)

- El mensaje escrito contiene palabras prohibidas ("idiota") que serán censuradas por el sistema.

  ### Paso 3: Interfaz de Consola - Inicio
- ![Consola, escribir nombre y bucle para escribir mensajes](https://github.com/user-attachments/assets/255d872e-569c-4ab9-b9bb-25f20f385d00)
  
- Cuando se selecciona la opción de consola, se muestra:
  1. **Solicitud de nombre**: Pide al usuario ingresar su nombre (en este caso "Gerardo")
  2. **Menú de opciones**:
     - 1. Escribir un mensaje
     - 2. Salir
  3. **Prompt de selección**: Espera que el usuario ingrese 1 o 2

- Esta interfaz está lista para recibir la selección del usuario y actuar en consecuencia.


- ### Paso 4: Procesamiento de Mensaje en Consola

  ![consola_ procesar respuesta](https://github.com/user-attachments/assets/380299ac-557c-4c61-bde1-a00277369ca8)

1. **Selección de opción**: El usuario eligió "1" (Escribir un mensaje)
2. **Entrada de mensaje**: El usuario escribió un mensaje ofensivo ("Ya Arturo no seas tonto, demasiado feo ya eres cabron")
3. **Procesamiento**:
   - El sistema detecta 4 palabras prohibidas (tonto, feo, cabron, Arturo)
   - Muestra logs del proceso de censura:
     - INFO: Iniciando revisión de contenido
     - WARNING: Mensaje censurado (4 palabras prohibidas)
     - INFO: Procesamiento mediante pointcut
4. **Resultado**:
   - Muestra mensaje bloqueado por contenido inapropiado
   - Vuelve a mostrar el menú para nuevas acciones.
- El aspecto (AOP) interceptó el mensaje y aplicó la censura según las reglas definidas (más de 3 palabras prohibidas = bloqueo total).
