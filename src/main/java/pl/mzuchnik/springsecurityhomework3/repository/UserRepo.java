package pl.mzuchnik.springsecurityhomework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.mzuchnik.springsecurityhomework3.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT user_id FROM user_authority ua WHERE ua.authority_id = 2", nativeQuery=true)
    List<Long> findAllWithRoleAdmin();


}
