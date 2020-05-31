package pl.mzuchnik.springsecurityhomework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mzuchnik.springsecurityhomework3.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String role);

}
