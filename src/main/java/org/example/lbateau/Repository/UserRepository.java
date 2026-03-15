package org.example.lbateau.Repository;

import org.example.lbateau.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // For password reset lookup
    Optional<User> findByEmail(String email);
    Optional<User> findByTelephone(String telephone);

    // Find by reset code (to verify)
    Optional<User> findByResetCode(String resetCode);
}