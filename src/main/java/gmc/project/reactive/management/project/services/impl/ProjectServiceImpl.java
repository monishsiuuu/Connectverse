package gmc.project.reactive.management.project.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.reactive.management.project.dao.ProjectDao;
import gmc.project.reactive.management.project.entities.ProjectEntity;
import gmc.project.reactive.management.project.services.ProjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;

	@Override
	public Mono<ProjectEntity> findOne(String id) {
		return projectDao.findById(id);
	}

	@Override
	public Flux<ProjectEntity> findManyByStatus(Boolean status) {
		return projectDao.findByStatus(status);
	}

	@Override
	public Flux<ProjectEntity> findManyByAdminId(String createdBy) {
		return projectDao.findByCreatedBy(createdBy);
	}

	@Override
	public Flux<ProjectEntity> findManyByDeveloper(String developerId) {
		return projectDao.findByDevelopersContaining(developerId);
	}

	@Override
	public Flux<ProjectEntity> findManyByDevelopersRequested(String developerId) {
		return projectDao.findByRequestedDevelopersContaining(developerId);
	}

	@Override
	public Mono<ProjectEntity> save(ProjectEntity newProject) {
		return projectDao.save(newProject);
	}

	@Override
	public Mono<Void> switchProjectStatus(String projectId, Boolean status) {
		projectDao.updateStatus(projectId, status).subscribe();
		return null;
	}

	@Override
	public Mono<Void> requestJoin(String projectId, String userId) {
		projectDao.pushToJoinRequests(projectId, userId).subscribe();
		return null;
	}

	@Override
	public Mono<Void> acceptJoinRequest(String projectId, String userId) {
		projectDao.findById(projectId).subscribe(foundProject -> {
			foundProject.getRequestedDevelopers().remove(userId);
			foundProject.getDevelopers().add(userId);
			save(foundProject).subscribe();
		});
		return null;
	}

	@Override
	public Mono<Void> rejectJoinRequest(String projectId, String userId) {
		projectDao.findById(projectId).subscribe(foundProject -> {
			foundProject.getRequestedDevelopers().remove(userId);
			save(foundProject).subscribe();
		});
		return null;
	}

	@Override
	public Mono<Boolean> saveAll(Iterable<ProjectEntity> projectEntities) {
		return projectDao.saveAll(projectEntities).flatMap(data -> Mono.just(true)).next();
	}

}
