package joyfe.joyfeSpring.apiController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
	
	@GetMapping(path = "/${taskEndpoint}")
	public String salute() {
		return "Pagina principal de la api";
	}

	@PostMapping(path = "/${taskEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addTask(@RequestBody @Valid Task newTask) throws URISyntaxException {
		if(subjectService.addTask(newTask))
			return ResponseEntity.created(new URI("/tasks/1")).body(newTask);
		return ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Recibir tarea", description = "Este endpoint te permite obtener la tarea cuyo Id sea introducido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarea encontrada"),
			@ApiResponse(responseCode = "400", description = "Error inesperado"),
			@ApiResponse(responseCode = "404", description = "Tarea no encontrada")
	})
	@GetMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> getTaskById(@PathVariable long id) {
		return ResponseEntity.ok(subjectService.getTaskById(id));
	}
	
	@PutMapping(path = "/${taskEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateTask(@PathVariable long id, @RequestBody @Valid Task newTask) {
		return subjectService.updateTask(id, newTask);
	}
	
	@DeleteMapping(path = "/${taskEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteTask(@PathVariable long id) {
		return subjectService.deleteTask(id);
	}
	
	/* -------------------------------------------------------------------------------------------------------------------------------------- */
	
	@PostMapping(path = "/${subjectEndpoint}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> addSubject(@RequestBody @Valid Subject newSubject) throws URISyntaxException {
		subjectService.addSubject(newSubject);
		return ResponseEntity.created(new URI("/taskEndpoint/1")).body(newSubject);
	}
	
	@GetMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subject> getSubjectById(@PathVariable long id) {
		return ResponseEntity.ok(subjectService.getSubjectById(id));
	}
	@GetMapping(path = "/${subjectEndpoint}/{id}/{taskEndpoint}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List> getTasksBySubject(@PathVariable long id) {
		return ResponseEntity.ok(subjectService.getTasksBySubject(id));
	}
	
	@PutMapping(path = "/${subjectEndpoint}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateSubject(@PathVariable long id, @RequestBody @Valid Subject newSubject) {
		return subjectService.updateSubject(id, newSubject);
	}
	
	@DeleteMapping(path = "/${subjectEndpoint}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteSubject(@PathVariable long id) {
		return subjectService.deleteSubject(id);
	}
}