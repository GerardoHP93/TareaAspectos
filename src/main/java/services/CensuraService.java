package services;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CensuraService {

    private final Set<String> palabrasProhibidas = new HashSet<>(Arrays.asList(
            "recorcholis", "bobo", "imbecil", "gay", "arturo",
            "estupido", "feo", "tonto", "wey", "cabron","idiota"));

    // Caracteres de sustitución
    private final String sustitucion = "!#?%@";

    /**
     * Censura un texto reemplazando palabras prohibidas
     * @param texto El texto a censurar
     * @return Objeto con el texto censurado y metadata
     */
    public ResultadoCensura censurar(String texto) {
        int contador = contarPalabrasProhibidas(texto);
        String contenidoFinal;
        boolean censurado = false;

        // Si hay más de 3 palabras prohibidas, reemplazar todo el mensaje
        if (contador > 3) {
            contenidoFinal = "⚠️ Este mensaje ha sido bloqueado por contener demasiado contenido inapropiado ⚠️";
            censurado = true;
        }
        // Si hay palabras prohibidas, reemplazarlas
        else if (contador > 0) {
            contenidoFinal = censurarPalabras(texto);
            censurado = true;
        } else {
            contenidoFinal = texto;
        }

        return new ResultadoCensura(contenidoFinal, contador, censurado);
    }

    public int contarPalabrasProhibidas(String texto) {
        int contador = 0;
        // Normalizar el texto de entrada eliminando acentos
        String textoNormalizado = eliminarAcentos(texto);

        for (String palabra : palabrasProhibidas) {
            // Normalizar también la palabra prohibida
            String palabraNormalizada = eliminarAcentos(palabra);
            // Usar \\b para detectar límites de palabras
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(palabraNormalizada) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(textoNormalizado);
            while (matcher.find()) {
                contador++;
            }
        }
        return contador;
    }

    private String censurarPalabras(String texto) {
        // Trabajaremos con una copia del texto original para mantener los caracteres originales
        String resultado = texto;
        // Versión normalizada para búsqueda
        String textoNormalizado = eliminarAcentos(texto);

        for (String palabra : palabrasProhibidas) {
            String palabraNormalizada = eliminarAcentos(palabra);
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(palabraNormalizada) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(textoNormalizado);

            // Lista para almacenar las posiciones de inicio y fin de cada coincidencia
            java.util.List<int[]> coincidencias = new java.util.ArrayList<>();

            // Encontrar todas las coincidencias y guardar sus posiciones
            while (matcher.find()) {
                coincidencias.add(new int[]{matcher.start(), matcher.end()});
            }

            // Reemplazar desde el final para no afectar los índices previos
            for (int i = coincidencias.size() - 1; i >= 0; i--) {
                int inicio = coincidencias.get(i)[0];
                int fin = coincidencias.get(i)[1];

                // Extraer la palabra original con sus acentos del texto original
                String palabraOriginal = resultado.substring(inicio, fin);

                // Crear la cadena de censura con la longitud adecuada
                StringBuilder censura = new StringBuilder();
                for (int j = 0; j < palabraOriginal.length(); j++) {
                    censura.append(sustitucion.charAt(j % sustitucion.length()));
                }

                // Reemplazar en el texto original
                resultado = resultado.substring(0, inicio) + censura + resultado.substring(fin);

                // También actualizar el texto normalizado para mantenerlo sincronizado
                textoNormalizado = textoNormalizado.substring(0, inicio) + censura + textoNormalizado.substring(fin);
            }
        }
        return resultado;
    }

    /**
     * Elimina acentos y diacríticos de un texto
     * @param texto Texto a normalizar
     * @return Texto sin acentos ni diacríticos
     */
    private String eliminarAcentos(String texto) {
        return java.text.Normalizer
                .normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
    }

    // Clase interna para devolver resultados de censura
    public static class ResultadoCensura {
        private final String textoCensurado;
        private final int cantidadPalabrasProhibidas;
        private final boolean censurado;

        public ResultadoCensura(String textoCensurado, int cantidadPalabrasProhibidas, boolean censurado) {
            this.textoCensurado = textoCensurado;
            this.cantidadPalabrasProhibidas = cantidadPalabrasProhibidas;
            this.censurado = censurado;
        }

        public String getTextoCensurado() {
            return textoCensurado;
        }

        public int getCantidadPalabrasProhibidas() {
            return cantidadPalabrasProhibidas;
        }

        public boolean isCensurado() {
            return censurado;
        }
    }
}