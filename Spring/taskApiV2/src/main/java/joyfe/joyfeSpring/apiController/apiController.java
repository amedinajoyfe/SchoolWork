package joyfe.joyfeSpring.apiController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import joyfe.joyfeSpring.secondaryClasses.Task;
import joyfe.joyfeSpring.secondaryClasses.Subject;
import joyfe.joyfeSpring.services.SubjectService;

@Tag(name = "Tareas 1", description = "Documentacion de la api")
@RestController
@RequestMapping("/${api-version}/${apiName}")
@CrossOrigin(origins = "*", allowedHeaders = {"POST", "GET", "PUT"})

public class apiController {
	@Autowired
	SubjectService subjectService;
	
	@Operation(summary = "Página principal", description = "Este endpoint sirve para comprobar si la api se encuentra activa")
	@GetMapping(path = "/${taskEndpoint}")
	public String salute() {
		return "Pagina principal de la api";
	}
	
	@Operation(summary = "Guardar tarea", description = "Este endpoint te permite guardar una tarea")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Tarea creada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
	})
	@PostMapping(path = "/${taskEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> addTask(@RequestBody @Valid Task newTask) throws URISyntaxException {
		return subjectService.addTask(newTask) ?  ResponseEntity.created(new URI("/tasks/" + newTask.getId())).body(newTask) : ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Recibir tarea", description = "Este endpoint te permite obtener la tarea cuyo Id sea introducido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarea encontrada"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Tarea no encontrada")
	})
	@GetMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> getTaskById(@PathVariable long id) {
		Task result = subjectService.getTaskById(id);
		return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(subjectService.getTaskById(id));
	}
	
	@Operation(summary = "Actualizar tarea", description = "Este endpoint te permite actualizar la información de una tarea")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Tarea modificada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Tarea no encontrada")
	})
	@PutMapping(path = "/${taskEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody @Valid Task newTask) throws URISyntaxException {
		return subjectService.updateTask(id, newTask) ? ResponseEntity.created(new URI("/tasks/" + id)).body(newTask) : ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Borrar tarea", description = "Este endpoint te permite eliminar una tarea")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarea eliminada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Tarea no encontrada")
	})
	@DeleteMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> deleteTask(@PathVariable long id) {
		return subjectService.deleteTask(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
	
	
	
	/* -------------------------------------------------------------------------------------------------------------------------------------- */
	

	@Operation(summary = "Guardar asignatura", description = "Este endpoint te permite crear una asignatura")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Asignatura creada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
	})
	@PostMapping(path = "/${subjectEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> addSubject(@RequestBody @Valid Subject newSubject) throws URISyntaxException {
		subjectService.addSubject(newSubject);
		return ResponseEntity.created(new URI("/taskEndpoint/1")).body(newSubject);
	}
	
	@Operation(summary = "Recibir asignatura", description = "Este endpoint te permite obtener la asignatura cuyo Id sea introducido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Asignatura recuperada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
	})
	@GetMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> getSubjectById(@PathVariable long id) {
		return ResponseEntity.ok(subjectService.getSubjectById(id));
	}
	
	@Operation(summary = "Recibir tareas de una asignatura", description = "Este endpoint te permite obtener todas las tareas de una asignatura una asignatura")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tareas recibidas correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
	})
	@GetMapping(path = "/${subjectEndpoint}/{id}/{taskEndpoint}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Task>> getTasksBySubject(@PathVariable long id) {
		return ResponseEntity.ok(subjectService.getTasksBySubject(id));
	}
	
	@Operation(summary = "Actualizar asignatura", description = "Este endpoint te permite actualizar la información de una asignatura")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Asignatura modificada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
	})
	@PutMapping(path = "/${subjectEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> updateSubject(@PathVariable long id, @RequestBody @Valid Subject newSubject) throws URISyntaxException {
		return subjectService.updateSubject(id, newSubject) ? ResponseEntity.created(new URI("/tasks/" + id)).body(newSubject) : ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Borrar asignatura", description = "Este endpoint te permite eliminar una asignatura")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Asignatura eliminada correctamente"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
	})
	@DeleteMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> deleteSubject(@PathVariable long id) {
		return subjectService.deleteSubject(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
}