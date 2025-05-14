package br.ufpr.tcc.MSAuth.models;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authenticated_users")
public class AuthenticatedUser {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private boolean success;
    private String name;
    private String perfil;
    private String token;
    
    @Indexed(name = "expiration_index", expireAfter = "0")
    private Date expiration;

    public AuthenticatedUser() {}

    public AuthenticatedUser(String userId, String name, String perfil, String token) {
        this.userId = userId;
        this.name = name;
        this.perfil = perfil;
        this.token = token;
        this.success = true;
        this.expiration = new Date(System.currentTimeMillis() + 600_000);
    }

    // Getters and setters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPerfil() { return perfil; }
    public String getToken() { return token; }
    public Date getExpiration() { return expiration; }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}