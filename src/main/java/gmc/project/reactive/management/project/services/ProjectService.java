package gmc.project.reactive.management.project.services;

import gmc.project.reactive.management.project.entities.ProjectEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {
	
	public Mono<ProjectEntity> findOne(String id);	
	public Flux<ProjectEntity> findManyByStatus(Boolean status);
	public Flux<ProjectEntity> findManyByAdminId(String createdBy);
	public Flux<ProjectEntity> findManyByDeveloper(String developerId);
	public Flux<ProjectEntity> findManyByDevelopersRequested(String developerId);
	
	public Mono<ProjectEntity> save(ProjectEntity project);	
	public Mono<Void> switchProjectStatus(String projectId, Boolean status);
	
	public Mono<Void> requestJoin(String projectId, String userId);
	public Mono<Void> acceptJoinRequest(String projectId, String userId);
	public Mono<Void> rejectJoinRequest(String projectId, String userId);
	
	public Mono<Boolean> saveAll(Iterable<ProjectEntity> projectEntities);
	
}
