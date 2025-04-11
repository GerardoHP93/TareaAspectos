package anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Disponible en tiempo de ejecución
@Target(ElementType.METHOD) // Se puede aplicar solamente a métodos
public @interface RevisarContenido {
    // Se puede definir tipo de censura si se desea personalizar
    String tipoCensura() default "default"; // Valores: "default", "asteriscos", "personalizado"
}