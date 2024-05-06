package gmc.project.reactive.management.project.graphql;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.reactive.management.project.dao.DeveloperDao;
import gmc.project.reactive.management.project.dao.TaskDao;
import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.entities.ProjectEntity;
import gmc.project.reactive.management.project.entities.TaskEntity;
import gmc.project.reactive.management.project.services.ProjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GraphQLServiceImpl implements GraphQLService {
	
	@Autowired
	private DeveloperDao developerDao;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TaskDao taskDao;

	@Override
	public Flux<ProjectGraphModel> projects() {
		Flux<ProjectEntity> projects = projectService.findManyByStatus(true);
		return projects.flatMap(projectToGraph);
	}

	@Override
	public Mono<ProjectGraphModel> project(String id) {
		return projectService.findOne(id).flatMap(projectToGraph);
	}

	@Override
	public Mono<DeveloperGraphModel> developer(String id) {
		Mono<DeveloperEntity> developerEntity = developerDao.findById(id);
		Flux<TaskEntity> userTasks = taskDao.findByAssignedTo(id);
		Flux<ProjectEntity> requested = projectService.findManyByDevelopersRequested(id);
		Flux<ProjectEntity> projects = projectService.findManyByDeveloper(id);
		Flux<ProjectEntity> adminOfProjects = projectService.findManyByAdminId(id);
		return developerEntity.flatMap(developer -> {
			DeveloperGraphModel developerGraphModel = new DeveloperGraphModel(developer);		
			Flux<DeveloperGraphModel> returnValue = Flux.zip(userTasks.collectList(), requested.collectList(), projects.collectList(), adminOfProjects.collectList())
					.map(tuple -> {
						developerGraphModel.setTasks(tuple.getT1());
						developerGraphModel.setRequestedProjects(tuple.getT2());
						developerGraphModel.setProjects(tuple.getT3());
						developerGraphModel.setCreatedProjects(tuple.getT4());
						return developerGraphModel;
					});
			return returnValue.next();
		});
	}
	
	private Function<ProjectEntity, Mono<ProjectGraphModel>> projectToGraph = (project) -> {
		Mono<DeveloperEntity> projectOwner = developerDao.findById(project.getCreatedBy());
		ProjectGraphModel returnValue = new ProjectGraphModel(project);
		projectOwner.subscribe(owner -> {	
			returnValue.setCreatedBy(owner);
		});
		Flux<DeveloperEntity> requestedUsers = developerDao.findAllById(project.getRequestedDevelopers());
		Flux<DeveloperEntity> developers = developerDao.findAllById(project.getDevelopers());
		Flux<TaskEntity> projectTasks = taskDao.findByProjectId(project.getId());		
		Flux<ProjectGraphModel> returnFlux = Flux.zip(projectTasks.collectList(), developers.collectList(), requestedUsers.collectList()).map(tuple -> {		
			returnValue.setTasks(tuple.getT1());
			returnValue.setDevelopers(tuple.getT2());
			returnValue.setRequestedDevelopers(tuple.getT3());			
			return returnValue;
		});
		return returnFlux.next();
	};

}
