package pl.mzuchnik.springsecurityhomework3.service;

import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface UserService {

    void addNewUser(User user, Set<String> roles);

    void sendVerifyTokenEmail(User user, String token, String type);

    void updateUserRoles(User user, Set<String> roles);

    boolean isTokenCorrect(String token,VerifyTokenType type);
}
