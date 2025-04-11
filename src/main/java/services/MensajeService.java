package services;

import models.Mensaje;

public interface MensajeService {
    /**
     * Procesa un mensaje para envío
     * @param mensaje El mensaje a procesar
     * @return El mensaje procesado (posiblemente censurado)
     */
    Mensaje procesarMensaje(Mensaje mensaje);

    /**
     * Muestra un mensaje en la consola
     * @param mensaje El mensaje a mostrar
     */
    void mostrarMensaje(Mensaje mensaje);
}