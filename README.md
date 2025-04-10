Descripción de los Componentes
Anotaciones
RevisarContenido.java

Propósito: Anotación personalizada para marcar métodos que deben ser interceptados por el aspecto de censura.
Características:

Anotación de tiempo de ejecución (@Retention(RetentionPolicy.RUNTIME))
Se aplica solo a métodos (@Target(ElementType.METHOD))
Permite configurar el tipo de censura 
