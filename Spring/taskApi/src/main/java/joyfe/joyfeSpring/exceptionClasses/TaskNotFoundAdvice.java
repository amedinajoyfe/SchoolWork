package joyfe.joyfeSpring.exceptionClasses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskNotFoundAdvice {
	/* //Esta clase se puede usr como handler de errores tambien pero GlobalController va a ser una clase para controlar todos los errores
	@ResponseBody
	@ExceptionHandler(TaskNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	
	String TaskNotFoundHandler(TaskNotFound ex) {
		return ex.getMessage();
	}*/
}
