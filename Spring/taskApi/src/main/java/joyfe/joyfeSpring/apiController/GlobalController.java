package joyfe.joyfeSpring.apiController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import joyfe.joyfeSpring.exceptionClasses.CustomErrorMessages;
import joyfe.joyfeSpring.exceptionClasses.TaskNotFound;

@RestControllerAdvice
public class GlobalController {

	@ExceptionHandler(TaskNotFound.class)
	public ResponseEntity<CustomErrorMessages> taskNotFoundHandle(TaskNotFound ex){
		CustomErrorMessages error = new CustomErrorMessages(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);		
	}
}