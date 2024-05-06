package gmc.project.reactive.management.project.graphql;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.entities.ProjectEntity;
import gmc.project.reactive.management.project.entities.TaskEntity;
import lombok.Data;

@Data
public class ProjectGraphModel implements Serializable {
	
	public ProjectGraphModel() {}
	
	public ProjectGraphModel(ProjectEntity project) {
		this.id = project.getId();
		this.tittle = project.getTittle();
		this.description = project.getDescription();
		this.icon = project.getIcon();
		this.status = project.getStatus();
		this.createdAt = project.getCreatedAt();
	}
	
	private static final long serialVersionUID = -1318727392845400532L;
	
	private String id;
	
	private String tittle;
	
	private String description;
	
	private String icon;
	
	private Boolean status;
	
	private List<TaskEntity> tasks = new ArrayList<>();
	
	private List<DeveloperEntity> requestedDevelopers = new ArrayList<>();
	
	private List<DeveloperEntity> developers = new ArrayList<>();
	
    private DeveloperEntity createdBy;
    
    private LocalDate createdAt;

}
