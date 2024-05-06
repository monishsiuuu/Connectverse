package gmc.project.reactive.management.project.graphql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.entities.ProjectEntity;
import gmc.project.reactive.management.project.entities.TaskEntity;
import lombok.Data;

@Data
public class DeveloperGraphModel implements Serializable {
	
	public DeveloperGraphModel() {}
	
	public DeveloperGraphModel(DeveloperEntity developerEntity) {
		this.id = developerEntity.getId();
		this.email = developerEntity.getEmail();
		this.profilePicUrl = developerEntity.getProfilePicUrl();
		this.name = developerEntity.getName();
		this.githubProfile = developerEntity.getGithubProfile();
		this.linkedInProfile = developerEntity.getLinkedInProfile();
		this.username = developerEntity.getUsername();
	}
	
	private static final long serialVersionUID = 8554517519694149552L;
	
	private String id;
	
	private String profilePicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDtd0soCSRdpo8Y5klekJdABh4emG2P29jwg&usqp=CAU";
	
	private String name;
	
	private String username;
	
	private String email;
	
	private String githubProfile;
	
	private String linkedInProfile;
	
	private List<TaskEntity> tasks = new ArrayList<>();
	
	private List<ProjectEntity> projects = new ArrayList<>();
	
	private List<ProjectEntity> createdProjects = new ArrayList<>();
	
	private List<ProjectEntity> requestedProjects = new ArrayList<>();
	
}
