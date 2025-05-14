package br.ufpr.tcc.MSAuth.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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

@Service
public class JwtTokenProvider {

	private static final long VALIDITY_IN_MILLISECONDS = 3600000;

	@Value("${security.jwt.token.secret-key:e9dea54074f73ae6f665a3b1724a5e5cf2db88d5405f03fa3fc3acf468961a86faebf7872be220b27266676ab85a9c4eba8cfa3f9947c85edc022f4dc8f2f2cf}")
	private String secretKeyString;

	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKeyString.getBytes()));
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
	        .signWith(SignatureAlgorithm.HS256, secretKey)
	        .compact();
	}
	public static class JwtTokenFilter extends OncePerRequestFilter {

	    private final JwtTokenProvider jwtTokenProvider;

	    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
	        this.jwtTokenProvider = jwtTokenProvider;
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain)
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

	public boolean validateToken(String token) {
	    try {
	        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}

	@SuppressWarnings("unchecked")
	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
	    Claims claims = Jwts.parser()
	        .setSigningKey(secretKey)
	        .parseClaimsJws(token)
	        .getBody();
	    
	    return ((List<String>) claims.get("roles"))
	        .stream()
	        .map(SimpleGrantedAuthority::new)
	        .collect(Collectors.toList());
	}

	public String getUsername(String token) {
	    return Jwts.parser()
	        .setSigningKey(secretKey)
	        .parseClaimsJws(token)
	        .getBody()
	        .getSubject();
	}

}