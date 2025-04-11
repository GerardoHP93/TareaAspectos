package services;

import models.Mensaje;
import org.springframework.stereotype.Service;

@Service
public class MensajeServiceImpl implements MensajeService {

    @Override
    public String procesarMensaje(Mensaje mensaje) {
        // Este método será interceptado por el aspecto
        return mensaje.toString();
    }
}
