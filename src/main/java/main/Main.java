package main;

import config.ProjectConfiguration;
import models.Mensaje;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import services.MensajeService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicializar el contexto de Spring
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        // Obtener el servicio de mensajes
        var mensajeService = context.getBean(MensajeService.class);

        // Crear un scanner para leer de la terminal
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sistema de Mensajes ===");

        // Pedir el nombre del usuario una sola vez al inicio
        System.out.print("Ingrese su nombre: ");
        String autor = scanner.nextLine();

        // Menú simple
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nOpciones:");
            System.out.println("1. Escribir un mensaje");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción (1 o 2): ");

            String opcion = scanner.nextLine();

            if (opcion.equals("2") || opcion.equalsIgnoreCase("salir")) {
                continuar = false;
                System.out.println("¡Hasta pronto, " + autor + "!");
            } else if (opcion.equals("1")) {
                enviarMensaje(scanner, mensajeService, autor);
            } else {
                System.out.println("Opción no válida. Por favor ingrese 1 o 2.");
            }
        }

        // Cerrar recursos
        scanner.close();
        context.close();
    }

    private static void enviarMensaje(Scanner scanner, MensajeService mensajeService, String autor) {
        Mensaje mensaje = new Mensaje();
        mensaje.setAutor(autor);

        System.out.print("Ingrese su mensaje: ");
        String contenido = scanner.nextLine();
        mensaje.setContenido(contenido);

        // Procesar el mensaje (el aspecto interceptará este método)
        Mensaje mensajeProcesado = mensajeService.procesarMensaje(mensaje);

        // Mostrar el mensaje procesado
        System.out.println("\n=== Mensaje procesado ===");
        mensajeService.mostrarMensaje(mensajeProcesado);
    }
}