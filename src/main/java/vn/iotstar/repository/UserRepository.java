package vn.iotstar.repository;

import vn.iotstar.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:login) OR LOWER(u.email) = LOWER(:login)")
    Optional<User> findByUsernameOrEmailIgnoreCase(@Param("login") String login);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
