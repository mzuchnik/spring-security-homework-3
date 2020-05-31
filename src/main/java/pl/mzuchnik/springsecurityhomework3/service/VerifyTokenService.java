package pl.mzuchnik.springsecurityhomework3.service;

import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyToken;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;


public interface VerifyTokenService {

    void save(User user, String token, VerifyTokenType type);

    void sendEmailWithToken(User user, String token, VerifyTokenType type);

    void sendEmailWithUserTokenToAdmin(User admin, User user, String token, VerifyTokenType type);

    VerifyToken getVerifyTokenByTokenValue(String tokenValue);

    boolean validateToken(String token);

    void removeToken(String token);
}
