package joyfe.joyfeSpring.apiController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import joyfe.joyfeSpring.secondaryClasses.Task;

@RestController
@RequestMapping("/myApi")
public class apiController {
	
	List<Task> taskList = new ArrayList<>();
	
	@PostMapping("/tasks")
	public String addTask(@RequestBody Task newTask) {
		newTask.setId(generateId());
		taskList.add(newTask);
		return "Tarea registrada";
	}
	
	@GetMapping("/tasks/{id}")
	public Task getTaskById(@PathVariable long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return new Task();
		return taskList.get((int)(id - 1));
	}
	
	@PutMapping("/tasks/{id}")
	public String updateTask(@PathVariable long id, @RequestBody Task newTask) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		newTask.setId(id);
		taskList.set((int)(id - 1), newTask);
		return "La tarea ha sido actualizada";
	}
	
	@DeleteMapping("/tasks/{id}")
	public String deleteTask(@PathVariable long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		taskList.set((int)(id - 1), new Task(true));
		return "La tarea ha sido borrada";
	}
	
	@GetMapping("/hola")
	public String saludar() {
		return "Hola";
	}
	
	private long generateId() {
		return taskList == null? 0:taskList.size() + 1;
	}
}
