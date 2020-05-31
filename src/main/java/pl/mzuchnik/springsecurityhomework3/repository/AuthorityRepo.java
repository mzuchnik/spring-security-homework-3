package pl.mzuchnik.springsecurityhomework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mzuchnik.springsecurityhomework3.entity.Authority;
import pl.mzuchnik.springsecurityhomework3.entity.AuthorityType;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityType authorityType);

}
