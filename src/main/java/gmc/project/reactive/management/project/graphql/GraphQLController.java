package gmc.project.reactive.management.project.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class GraphQLController {
	
	@Autowired
	private GraphQLService graphService;
	
	@QueryMapping
	private Flux<ProjectGraphModel> projects() {
		return graphService.projects();
	}
	
	@QueryMapping
	private Mono<ProjectGraphModel> project(@Argument String id) {
		return graphService.project(id);
	}
	
	@QueryMapping
	private Mono<DeveloperGraphModel> developer(@Argument String id) {
		return graphService.developer(id);
	}

}
