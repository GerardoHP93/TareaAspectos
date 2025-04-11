package models;

import org.springframework.stereotype.Component;

@Component
public class Mensaje {
    private String contenido;
    private String autor;
    private boolean censurado;

    public Mensaje() {
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isCensurado() {
        return censurado;
    }

    public void setCensurado(boolean censurado) {
        this.censurado = censurado;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "autor='" + autor + '\'' +
                ", contenido='" + contenido + '\'' +
                ", censurado=" + censurado +
                '}';
    }
}