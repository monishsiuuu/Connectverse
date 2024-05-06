package gmc.project.reactive.management.project.security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.reactive.management.project.entities.DeveloperEntity;
import gmc.project.reactive.management.project.models.DeveloperModel;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/auth")
@RestController
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private ReactiveAuthenticationManager authenticationManager;

	@GetMapping(path = "/{userId}")
	private Mono<Boolean> isM2FEnabled(@PathVariable String userId) {
		return authService.isM2FEnabled(userId);
	}

	@GetMapping(path = "/{userId}/m2f/{status}")
	private Mono<ResponseEntity<Map<String, String>>> authM2F(@PathVariable String userId, @PathVariable Boolean status) {
		return authService.toggleM2F(userId, status)
				.map(qrUrl -> ResponseEntity.status(HttpStatus.OK).body(Map.of("qrcode_url", qrUrl)));
	}

	@PostMapping("/login")
	private Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Mono<LoginModel> authRequest) {
		return authRequest.flatMap(login -> {
			if(!login.getOtp().equals("null")) {
				login.setUserName(login.getUserName() + AuthService.OTP_SEPERATOR + login.getOtp());
			}
			return this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword()))
					.map(this.tokenProvider::createToken);
		}).map(jwt -> {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
			Map<String, String> tokenBody = Map.of("access_token", jwt);
			return new ResponseEntity<Map<String, String>>(tokenBody, httpHeaders, HttpStatus.OK);
		});
	}

	@PostMapping
	private Mono<DeveloperModel> registerUser(@RequestBody Mono<DeveloperModel> newUser) {
		return newUser.flatMap(authService::registerUser);
	}
	
	@PostMapping(path = "/save-many")
	private Mono<Boolean> registerManyUser(@RequestBody List<DeveloperEntity> newUsers) {
		return authService.registerMany(newUsers);
	}

	@PutMapping
	private Mono<DeveloperModel> completeProfile(@RequestBody Mono<DeveloperModel> newUser) {
		return newUser.flatMap(authService::completeProfile);
	}

}
