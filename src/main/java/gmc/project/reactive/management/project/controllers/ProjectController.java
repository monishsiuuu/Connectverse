package gmc.project.reactive.management.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.reactive.management.project.entities.ProjectEntity;
import gmc.project.reactive.management.project.services.ProjectService;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/project")
@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping(path = "/{projectId}/request/{userId}")
	private Mono<Void> requestJoin(@PathVariable String projectId, @PathVariable String userId) {
		return projectService.requestJoin(projectId, userId);
	}
	
	@GetMapping(path = "/{projectId}/accept/{userId}")
	private Mono<Void> acceptRequest(@PathVariable String projectId, @PathVariable String userId) {
		return projectService.acceptJoinRequest(projectId, userId);
	}
	
	@GetMapping(path = "/{projectId}/reject/{userId}")
	private Mono<Void> rejectRequest(@PathVariable String projectId, @PathVariable String userId) {
		return projectService.rejectJoinRequest(projectId, userId);
	}
	
	@GetMapping(path = "/{projectId}/{status}")
	private Mono<Void> changeProjectStatus(@PathVariable String projectId, @PathVariable Boolean status) {
		return projectService.switchProjectStatus(projectId, status);
	}
	
	@PostMapping
	private Mono<ProjectEntity> save(@RequestBody Mono<ProjectEntity> newProject) {
		return newProject.flatMap(projectService::save);
	}
	
	@PostMapping(path = "/save-many")
	private Mono<Boolean> saveMany(@RequestBody List<ProjectEntity> newProjects) {
		return projectService.saveAll(newProjects);
	}
	
}
