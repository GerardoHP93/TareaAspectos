package main;

import models.Mensaje;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import services.MensajeService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);
        var servicio = context.getBean(MensajeService.class);

        Scanner sc = new Scanner(System.in);
        System.out.print("Autor del mensaje: ");
        String autor = sc.nextLine();

        System.out.print("Escribe tu mensaje: ");
        String contenido = sc.nextLine();

        Mensaje mensaje = new Mensaje(autor, contenido);
        System.out.println(servicio.procesarMensaje(mensaje));
    }
}
