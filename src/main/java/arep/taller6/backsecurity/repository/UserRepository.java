package arep.taller6.backsecurity.repository;

import arep.taller6.backsecurity.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}
