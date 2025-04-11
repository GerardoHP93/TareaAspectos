package aspectos;

import anotaciones.RevisarContenido;
import models.Mensaje;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import services.CensuraService;

import java.util.logging.Logger;

@Aspect
@Component
public class CensorAspect {
    private static final Logger logger = Logger.getLogger(CensorAspect.class.getName());

    private final CensuraService censuraService;

    public CensorAspect(CensuraService censuraService) {
        this.censuraService = censuraService;
    }

    @Around("@annotation(revisarContenido)")
    public Object censurarContenido(ProceedingJoinPoint joinPoint, RevisarContenido revisarContenido) throws Throwable {
        logger.info("Iniciando revisión de contenido del mensaje");

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Mensaje) {
            Mensaje mensaje = (Mensaje) args[0];
            String contenidoOriginal = mensaje.getContenido();

            // Aplicar censura usando el servicio dedicado
            CensuraService.ResultadoCensura resultado = censuraService.censurar(contenidoOriginal);

            // Actualizar el mensaje
            mensaje.setContenido(resultado.getTextoCensurado());
            mensaje.setCensurado(resultado.isCensurado());

            // Log de la acción
            if (resultado.isCensurado()) {
                logger.warning("Mensaje de " + mensaje.getAutor() + " ha sido censurado. Contenía " +
                        resultado.getCantidadPalabrasProhibidas() + " palabras prohibidas.");
            } else {
                logger.info("Mensaje de " + mensaje.getAutor() + " ha pasado la revisión de contenido.");
            }

            // Sustituir el argumento original con el mensaje modificado
            args[0] = mensaje;
        }

        return joinPoint.proceed(args);
    }

    // También podemos mantener el pointcut basado en execution como alternativa
    @Around("execution(* services.MensajeService.procesarMensaje(..))")
    public Object censurarMensajeAlternativo(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Interceptando mediante pointcut de ejecución");

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Mensaje) {
            Mensaje mensaje = (Mensaje) args[0];

            // Aplicar la misma lógica de censura
            CensuraService.ResultadoCensura resultado = censuraService.censurar(mensaje.getContenido());
            mensaje.setContenido(resultado.getTextoCensurado());
            mensaje.setCensurado(resultado.isCensurado());

            // Log simplificado
            logger.info("Mensaje procesado mediante pointcut de ejecución");
        }

        return joinPoint.proceed(args);
    }
}