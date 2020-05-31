package pl.mzuchnik.springsecurityhomework3.service;

import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;

public interface UserService {

    void addNewUser(User user);

    void sendVerifyTokenEmail(User user, String token, String type);

    void updateUserRoles(User user);

    boolean isTokenCorrect(String token,VerifyTokenType type);
}
