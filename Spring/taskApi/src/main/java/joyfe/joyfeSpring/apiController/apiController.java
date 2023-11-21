package joyfe.joyfeSpring.apiController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import joyfe.joyfeSpring.exceptionClasses.TaskNotFound;
import joyfe.joyfeSpring.secondaryClasses.Task;

@Tag(name = "Tareas 1", description = "Documentacion de la api")
@RestController
@RequestMapping("/${api-version}/${apiName}")
@CrossOrigin(origins = "*", allowedHeaders = {"POST", "GET", "PUT"})

public class apiController {
	
	List<Subject> subjectList = new ArrayList<>();
	List<Task> taskList = new ArrayList<>();

	@PostMapping(path = "/${taskEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> addTask(@RequestBody @Valid Task newTask) throws URISyntaxException {
		newTask.setId(generateId());
		taskList.add(newTask);
		return ResponseEntity.created(new URI("/taskEndpoint/1")).body(newTask);
	}
	
	@GetMapping(path = "/${taskEndpoint}")
	public String salute() {
		return "Pagina principal de la api";
	}
	
	@Operation(summary = "Recibir tarea", description = "Este endpoint te permite obtener la tarea cuyo Id sea introducido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarea encontrada"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Tarea no encontrada")
	})
	@GetMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> getTaskById(@PathVariable long id) {
		return ResponseEntity.ok(taskList.stream().filter(task -> task.getId() == id).findFirst().orElseThrow());
	}
	
	@PutMapping(path = "/${taskEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateTask(@PathVariable long id, @RequestBody @Valid Task newTask) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		newTask.setId(id);
		taskList.set((int)(id - 1), newTask);
		return "La tarea ha sido actualizada";
	}
	
	@DeleteMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteTask(@PathVariable long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		taskList.set((int)(id - 1), new Task(true));
		return "La tarea ha sido borrada";
	}
	
	
	
	
	
	private long generateId() {
		return taskList == null? 0:taskList.size() + 1;
	}
	
	
	
	
	
	@PostMapping(path = "/${subjectEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> addSubject(@RequestBody @Valid Task newTask) throws URISyntaxException {
		newTask.setId(generateId());
		taskList.add(newTask);
		return ResponseEntity.created(new URI("/subjectEndpoint/1")).body(newTask);
	}
	
	@GetMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> getSubjectById(@PathVariable long id) {
		return ResponseEntity.ok(taskList.stream().filter(task -> task.getId() == id).findFirst().orElseThrow(() -> new TaskNotFound(id)));
	}
	
	@PutMapping(path = "/${subjectEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateSubject(@PathVariable long id, @RequestBody @Valid Task newTask) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa materia no existe";
		newTask.setId(id);
		taskList.set((int)(id - 1), newTask);
		return "La materia ha sido actualizada";
	}
	
	@DeleteMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteSubject(@PathVariable long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa materia no existe";
		taskList.set((int)(id - 1), new Task(true));
		return "La materia ha sido borrada";
	}
}