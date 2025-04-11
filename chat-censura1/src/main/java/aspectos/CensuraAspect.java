package aspectos;

import models.Mensaje;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CensuraAspect {

    private final CensuraService censuraService;

    public CensuraAspect(CensuraService censuraService) {
        this.censuraService = censuraService;
    }

    @Around("execution(* services.MensajeService.procesarMensaje(..))")
    public Object censurarMensaje(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Mensaje mensaje = (Mensaje) args[0];

        String censurado = censuraService.censurar(mensaje.getContenido());
        mensaje.setContenido(censurado);

        return joinPoint.proceed(new Object[]{mensaje});
    }
}
