package gmc.project.reactive.management.project.graphql;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GraphQLService {
	
	public Flux<ProjectGraphModel> projects();
	
	public Mono<ProjectGraphModel> project(String id);
	
	public Mono<DeveloperGraphModel> developer(String id);

}
