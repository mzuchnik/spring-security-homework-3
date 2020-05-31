package pl.mzuchnik.springsecurityhomework3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyToken;

public interface VerifyTokenRepo extends JpaRepository<VerifyToken,Long> {

    VerifyToken findByToken(String token);

    VerifyToken findByUser(User user);

    void deleteAllByToken(String token);
}
