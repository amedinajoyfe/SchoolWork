package joyfe.joyfeSpring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import joyfe.joyfeSpring.secondaryClasses.Subject;
import joyfe.joyfeSpring.secondaryClasses.Task;

@Service("subjectService")
public class SubjectService {

	List<Subject> subjectList = new ArrayList<>();
	List<Task> taskList = new ArrayList<>();
	
	
	public List<Subject> getSubjectList() {
		return subjectList;
	}
	public List<Task> getTaskList() {
		return taskList;
	}
	
	public boolean addTask(Task newTask) {
		if(subjectList.stream().filter(subject -> subject.getId() == newTask.getSubjectId()).findFirst().orElse(null) == null) 
			return false;
		newTask.setId(generateIdTask());
		taskList.add(newTask);
		return true;
	}
	public Task getTaskById(long id) {
		return taskList.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
	}
	public String updateTask(long id, Task newTask) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		newTask.setId(id);
		taskList.set((int)(id - 1), newTask);
		return "La tarea ha sido actualizada";
	}
	public String deleteTask(long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return "Esa tarea no existe";
		taskList.set((int)(id - 1), new Task(true));
		return "La tarea ha sido borrada";
	}

	public Subject addSubject(Subject newSubject) {
		newSubject.setId(generateIdSubject());
		subjectList.add(newSubject);
		return newSubject;
	}
	public Subject getSubjectById(long id) {
		return subjectList.stream().filter(subject -> subject.getId() == id).findFirst().orElse(null);
	}
	public List<Task> getTasksBySubject(long id){
		List<Task> filteredTaskList = new ArrayList<Task>();
		filteredTaskList = taskList.stream().filter(task -> task.getSubjectId() == id).collect(Collectors.toList());;
		return filteredTaskList;
	}
	public String updateSubject(long id, Subject newSubject) {
		if(subjectList == null || id > subjectList.size() || id < 1)
			return "Esa materia no existe";
		newSubject.setId(id);
		subjectList.set((int)(id - 1), newSubject);
		return "La materia ha sido actualizada";
	}
	public String deleteSubject(long id) {
		if(subjectList == null || id > subjectList.size() || id < 1)
			return "Esa materia no existe";
		subjectList.set((int)(id - 1), new Subject());
		return "La materia ha sido borrada";
	}	
	
	private long generateIdTask() {
		return taskList == null? 0:taskList.size() + 1;
	}
	private long generateIdSubject() {
		return subjectList == null? 0:subjectList.size() + 1;
	}
}
