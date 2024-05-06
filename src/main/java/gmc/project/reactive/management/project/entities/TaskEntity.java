package gmc.project.reactive.management.project.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Document(collection = "tasks")
public @Data class TaskEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@MongoId
	private String id;
	
	private String tittle;
	
	private String description;
	
	private Set<String> comments = new HashSet<>();
	
	private Boolean status = true;
	
	private String assignedTo;
	
	private String projectId;
	
	private LocalDate deadline;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@CreatedDate
	private LocalDateTime createdAt;

}
