package services.impl;

import anotaciones.RevisarContenido;
import models.Mensaje;
import org.springframework.stereotype.Service;
import services.MensajeService;

@Service
public class MensajeServiceImpl implements MensajeService {

    @Override
    @RevisarContenido
    public Mensaje procesarMensaje(Mensaje mensaje) {
        // La lógica de censura se maneja en el aspecto
        System.out.println("Procesando mensaje de: " + mensaje.getAutor());
        return mensaje;
    }

    @Override
    public void mostrarMensaje(Mensaje mensaje) {
        if (mensaje.isCensurado()) {
            System.out.println("MENSAJE CENSURADO - Autor: " + mensaje.getAutor());
        }
        System.out.println(mensaje.getAutor() + " dice: " + mensaje.getContenido());
    }
}