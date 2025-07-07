package br.ufpr.tcc.MSAuth.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        String envSecret = System.getenv("JWT_SECRET");
        if (envSecret != null && !envSecret.isEmpty()) {
            secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(envSecret.getBytes()));
        } else {
            logger.warn("JWT_SECRET environment variable not found! Generating a random secret key for this session. Tokens will not be valid across restarts.");
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }


	public String createToken(String username, List<String> roles, Long userId, String name, String microservice) {
	    Claims claims = Jwts.claims().setSubject(username);
	    claims.put("roles", roles);
	    claims.put("userId", userId);
	    claims.put("name", name);
	    claims.put("microservice", microservice);

	    Date now = new Date();
	    Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

	    return Jwts.builder()
	        .setClaims(claims)
	        .setIssuedAt(now)
	        .setExpiration(validity)
			.signWith(secretKey, SignatureAlgorithm.HS256)
	        .compact();
	}
	public static class JwtTokenFilter extends OncePerRequestFilter {

	    private final JwtTokenProvider jwtTokenProvider;

	    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
	        this.jwtTokenProvider = jwtTokenProvider;
	    }

		@Override
		protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request,
										@org.springframework.lang.NonNull HttpServletResponse response,
										@org.springframework.lang.NonNull FilterChain filterChain)
				throws ServletException, IOException, java.io.IOException {
	        
	        String token = resolveToken(request);
	        
	        try {
	            if (token != null && jwtTokenProvider.validateToken(token)) {
	                Authentication auth = jwtTokenProvider.getAuthentication(token);
	                SecurityContextHolder.getContext().setAuthentication(auth);
	            }
	        } catch (JwtException | IllegalArgumentException e) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
	            return;
	        }
	        
	        filterChain.doFilter(request, response);
	    }

	    private String resolveToken(HttpServletRequest req) {
	        String bearerToken = req.getHeader("Authorization");
	        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	            return bearerToken.substring(7);
	        }
	        return null;
	    }
	}

	public Authentication getAuthentication(String token) {
		String username = getUsername(token);
		List<SimpleGrantedAuthority> authorities = getRolesFromToken(token);
		
		return new UsernamePasswordAuthenticationToken(
			username,
			null,
			authorities
		);
	}

	public String getUsername(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
	    try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}

	@SuppressWarnings("unchecked")
	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	    
	    return ((List<String>) claims.get("roles"))
	        .stream()
	        .map(SimpleGrantedAuthority::new)
	        .collect(Collectors.toList());
	}

}