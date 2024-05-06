package gmc.project.reactive.management.project.services;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import reactor.core.publisher.Mono;

public interface DeveloperService {

	public Mono<DeveloperEntity> findOne(String uniqueId);
	
	public Mono<DeveloperEntity> save(DeveloperEntity developerEntity);
	
	public Mono<Boolean> saveAll(Iterable<DeveloperEntity> developerEntities);
	
}
