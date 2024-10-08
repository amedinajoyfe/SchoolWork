package joyfe.joyfeSpring.exceptionClasses;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomErrorMessages {
	@NotNull
	private HttpStatus status;
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss.SSS")
	private LocalDateTime timestamp = LocalDateTime.now();
	@NotNull
	private String message;
	
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public CustomErrorMessages(@NotNull HttpStatus status, @NotNull String message) {
		this.status = status;
		this.message = message;
	}
}
