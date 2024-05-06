package gmc.project.reactive.management.project.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {
	private String authUrl;
	private String oauthPath;
	private String issuer;
	private String jwtSecret;
	private long expeiry;
	private String refreshToken;
}
