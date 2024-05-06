package gmc.project.reactive.management.project.security;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import gmc.project.reactive.management.project.models.DeveloperModel;
import reactor.core.publisher.Mono;

public class OAuthSuccessHandler implements ServerAuthenticationSuccessHandler {
	
	private JwtTokenProvider jwtTokenProvider;
	
	private AuthService authService;
	
	private ServerRedirectStrategy serverRedirectStrategy;
	
	public OAuthSuccessHandler( JwtTokenProvider jwtTokenProvider, AuthService authService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.authService = authService;
		this.serverRedirectStrategy = new DefaultServerRedirectStrategy();
	}
			
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    	DeveloperModel signinUser = new DeveloperModel(authentication.getPrincipal().toString());
    	CompletableFuture<DeveloperModel> saved = authService.registerUser(signinUser).toFuture();
    	ServerWebExchange exchange = webFilterExchange.getExchange();
    	Authentication newAuthentication = new Authentication() {			
			private static final long serialVersionUID = -3909253054119418051L;
			@Override
			public String getName() {
				try {
					return saved.get().getId();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}			
			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
			}			
			@Override
			public boolean isAuthenticated() {
				// TODO Auto-generated method stub
				return true;
			}			
			@Override
			public Object getPrincipal() {
				return null;
			}			
			@Override
			public Object getDetails() {
				return null;
			}			
			@Override
			public Object getCredentials() {
				return null;
			}			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return new ArrayList<>();
			}
		};
		String token = jwtTokenProvider.createToken(newAuthentication);;
		Supplier<Mono<Void>> responseSuplier = () -> serverRedirectStrategy
            .sendRedirect(exchange,
                    resolveRedirectUri(exchange.getRequest(), token)
            );
    	return responseSuplier.get();
    }

    private URI resolveRedirectUri(ServerHttpRequest httpRequest, String token) {
//        String encodedUrlSafeState = httpRequest.getQueryParams().getFirst("state");
//        if (!StringUtils.hasText(encodedUrlSafeState))
//            return URI.create(httpRequest.getURI().getHost());
//        byte[] redirectUriByte = Base64.getDecoder().decode(encodedUrlSafeState);
        return URI.create(new String("http://localhost:3000/oauth/" + token));
    }

}
