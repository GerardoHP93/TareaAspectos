package models;


import org.springframework.stereotype.Component;

@Component
public class Mensaje {
    private String autor;
    private String contenido;

    public Mensaje() {}

    public Mensaje(String autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "Autor: " + autor + ", Mensaje: " + contenido;
    }
}
