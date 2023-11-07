package joyfe.joyfeSpring.exceptionClasses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskNotFound(Long id) {
		super("No se ha encontrado una tarea con ID " + id);
	}
}
