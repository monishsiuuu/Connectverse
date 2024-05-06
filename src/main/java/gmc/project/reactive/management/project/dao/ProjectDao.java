package gmc.project.reactive.management.project.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;

import gmc.project.reactive.management.project.entities.ProjectEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectDao extends ReactiveMongoRepository<ProjectEntity, String> {
	
	public Flux<ProjectEntity> findByStatus(Boolean status);		
	
	public Flux<ProjectEntity> findByCreatedBy(String createdBy);
	public Flux<ProjectEntity> findByRequestedDevelopersContaining(String developerId);
	public Flux<ProjectEntity> findByDevelopersContaining(String developerId);

	@Query("{'_id': ?0}")
	@Update("{ '$set': { 'status': ?1 } }")
	public Mono<Void> updateStatus(String id, Boolean status);
	
	@Query("{'_id': ?0}")
	@Update("{ '$addToSet': { 'requestedDevelopers': ?1 } }")
	public Mono<Void> pushToJoinRequests(String id, String userId);
	
}
