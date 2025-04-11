package anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //permiso de educar la etiqueta durante tiempo de ejecución
@Target(ElementType.METHOD) //Solo permite usar la etiqueta para marcar metodos
public @interface Reportar {


}
