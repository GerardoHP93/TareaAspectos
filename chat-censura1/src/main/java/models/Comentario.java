package models;

import org.springframework.stereotype.Component;

@Component
public class Comentario {
    String Autor;
    String texto;

    public Comentario() {}

    public String getAutor() {
        return Autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
