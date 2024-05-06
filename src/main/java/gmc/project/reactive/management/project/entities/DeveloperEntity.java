package gmc.project.reactive.management.project.entities;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Document(collection = "developers")
public @Data class DeveloperEntity implements Serializable {

	private static final long serialVersionUID = -4555444170398157663L;
	
	@MongoId
	private String id;
	
	private String profilePicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDtd0soCSRdpo8Y5klekJdABh4emG2P29jwg&usqp=CAU";
	
	private String name;
	
	private String username;
	
	private String password;
	
	private String m2FId;
	
	private Boolean enabledM2F = false;
	
	private String email;
	
	private String githubProfile;
	
	private String linkedInProfile;
	
	private String authProvider;
	
}
