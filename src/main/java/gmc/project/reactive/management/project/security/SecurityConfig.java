package gmc.project.reactive.management.project.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Component
@Configuration
public class SecurityConfig {

	@Value("${settings.app.disableSecurity}")
	private Boolean disableSecurity;

	@Autowired
	private AuthConfig authConfig;

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, JwtTokenProvider tokenProvider,
			AuthService authService, ReactiveAuthenticationManager reactiveAuthenticationManager) {
		if (disableSecurity)
			return http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
					.authenticationManager(reactiveAuthenticationManager)
					.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
					.authorizeExchange(it -> it.pathMatchers("*", "/**").permitAll()).build();
		final String SWAGGER = "/webjars/swagger-ui/**";
		return http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.authenticationManager(reactiveAuthenticationManager)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(it -> it.pathMatchers(HttpMethod.GET, SWAGGER).permitAll()
						.pathMatchers(HttpMethod.GET, "/auth/*").permitAll().pathMatchers(HttpMethod.POST, "/auth")
						.permitAll().pathMatchers(HttpMethod.POST, authConfig.getAuthUrl()).permitAll()
						.pathMatchers(authConfig.getOauthPath()).permitAll().pathMatchers("/**").authenticated()
						.anyExchange().permitAll())
				.oauth2Login(oauth -> {
					oauth.authenticationSuccessHandler(new OAuthSuccessHandler(tokenProvider, authService));
				}).addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
				.build();
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ReactiveAuthenticationManager reactiveAuthenticationManager(BCryptPasswordEncoder bCryptPasswordEncoder,
			AuthService authService) {
		var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(authService);
		authenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
		return authenticationManager;
	}
	
	private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", 
                                       "Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
