package services;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CensuraService {

    private final Set<String> palabrasProhibidas = new HashSet<>(Arrays.asList("malo", "feo", "tonto", "grosero"));

    public String censurar(String contenido) {
        int contador = 0;
        String[] palabras = contenido.split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (palabrasProhibidas.contains(palabra.toLowerCase())) {
                contador++;
                resultado.append("!#?%@ ");
            } else {
                resultado.append(palabra).append(" ");
            }
        }

        if (contador > 3) {
            return "[ADVERTENCIA] Este mensaje contiene demasiadas palabras prohibidas.";
        }

        return resultado.toString().trim();
    }
}
