package cr.una.bolsaempleo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception e) {
        log.error("Error no controlado", e);
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("status", 500);
        mav.addObject("error", "Error interno del servidor");
        mav.addObject("message", e.getMessage());
        return mav;
    }

}
