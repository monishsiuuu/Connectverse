package gmc.project.reactive.management.project.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.models.DeveloperModel;
import reactor.core.publisher.Mono;

public interface AuthService extends ReactiveUserDetailsService {
	
	public static String OTP_SEPERATOR = "@NAVIN@";
	
	public Mono<Boolean> isM2FEnabled(String userId);
	public Mono<String> toggleM2F(String userId, Boolean status);
	
	public Mono<DeveloperModel> registerUser(DeveloperModel developerModel);
	public Mono<Boolean> registerMany(Iterable<DeveloperEntity> developerEntities);
	public Mono<DeveloperModel> completeProfile(DeveloperModel developerModel);

}
