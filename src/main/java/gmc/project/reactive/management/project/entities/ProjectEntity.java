package gmc.project.reactive.management.project.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Document(collection = "projects")
public @Data class ProjectEntity implements Serializable {

	private static final long serialVersionUID = 6694643477023020830L;
	
	@MongoId
	private String id;
	
	private String tittle;
	
	private String description;
	
	private String icon = "https://avatars.githubusercontent.com/u/71096790?v=4";
	
	private Boolean status = true;
		
	private Set<String> requestedDevelopers = new HashSet<>();
	
	private Set<String> developers = new HashSet<>();
	
    private String createdBy;
	
	@LastModifiedDate
	private LocalDate updatedAt;
	
	@CreatedDate
	private LocalDate createdAt;

}
