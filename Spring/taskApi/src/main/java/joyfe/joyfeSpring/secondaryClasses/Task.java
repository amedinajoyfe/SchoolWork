package joyfe.joyfeSpring.secondaryClasses;

import java.util.Date;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Task {
	long id;
	String name;
	String description;
	@JsonFormat(pattern = "dd/MM/yyyy")
	Date deadline;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public Task() {
		this.id = -99;
		this.name = "Error";
		this.description = "This task does not exist";
	}
	
	public Task(boolean deleted) {
		this.id = -99;
		this.name = "Deleted";
	}
}
