package gmc.project.reactive.management.project.dao;

import java.util.Set;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeveloperDao extends ReactiveMongoRepository<DeveloperEntity, String> {
	
	public Flux<DeveloperEntity> findAllById(Set<String> id);

	public Mono<DeveloperEntity> findByEmail(String email);
	
	public Mono<Boolean> existsByEmail(String email);
	
	public Mono<Boolean> existsByEmailAndEnabledM2F(String email, Boolean enabledM2F);
	
	@Query("{ '_id': ?0 }")
	@Update("{ '$set': { 'm2FId': ?1, 'enabledM2F': ?2 } }")
	public Mono<Void> m2FStatus(String id, String m2FId, Boolean status);
	
}
