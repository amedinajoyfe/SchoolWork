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
	public boolean updateTask(long id, Task newTask) {
		if(taskList == null || id > taskList.size() || id < 1)
			return false;
		newTask.setId(id);
		taskList.set((int)(id - 1), newTask);
		return true;
	}
	public boolean deleteTask(long id) {
		if(taskList == null || id > taskList.size() || id < 1)
			return false;
		taskList.set((int)(id - 1), new Task(true));
		return true;
	}

	public void addSubject(Subject newSubject) {
		newSubject.setId(generateIdSubject());
		subjectList.add(newSubject);
	}
	public Subject getSubjectById(long id) {
		return subjectList.stream().filter(subject -> subject.getId() == id).findFirst().orElse(null);
	}
	public List<Task> getTasksBySubject(long id){
		List<Task> filteredTaskList = new ArrayList<Task>();
		filteredTaskList = taskList.stream().filter(task -> task.getSubjectId() == id).collect(Collectors.toList());;
		return filteredTaskList;
	}
	public boolean updateSubject(long id, Subject newSubject) {
		if(subjectList == null || id > subjectList.size() || id < 1)
			return false;
		newSubject.setId(id);
		subjectList.set((int)(id - 1), newSubject);
		return true;
	}
	public boolean deleteSubject(long id) {
		if(subjectList == null || id > subjectList.size() || id < 1)
			return false;
		subjectList.set((int)(id - 1), new Subject());
		return true;
	}	
	
	private long generateIdTask() {
		return taskList == null? 0:taskList.size() + 1;
	}
	private long generateIdSubject() {
		return subjectList == null? 0:subjectList.size() + 1;
	}
}
