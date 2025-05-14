package br.ufpr.tcc.MSAuth.repositories;

import br.ufpr.tcc.MSAuth.models.AuthenticatedUser;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AuthenticatedUserRepository extends MongoRepository<AuthenticatedUser, String> {
    AuthenticatedUser findByToken(String token);
    long deleteByToken(String token);
    
    @Query("{ 'userId' : ?0, 'expiration' : { $gt: ?1 } }")
    Optional<AuthenticatedUser> findByUserIdAndExpirationAfter(String userId, Date currentTime);

}