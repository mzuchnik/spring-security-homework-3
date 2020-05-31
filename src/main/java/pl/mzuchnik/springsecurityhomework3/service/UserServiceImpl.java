package pl.mzuchnik.springsecurityhomework3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mzuchnik.springsecurityhomework3.entity.*;
import pl.mzuchnik.springsecurityhomework3.repository.AuthorityRepo;
import pl.mzuchnik.springsecurityhomework3.repository.UserRepo;
import pl.mzuchnik.springsecurityhomework3.repository.VerifyTokenRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private VerifyTokenService verifyTokenService;
    private AuthorityRepo authorityRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, VerifyTokenService verifyTokenService, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.verifyTokenService = verifyTokenService;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public void addNewUser(User user, Set<String> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        updateUserRoles(user, roles);
        userRepo.save(user);
    }

    @Override
    public void sendVerifyTokenEmail(User user, String token, String type) {
        if(type.equals("ENABLE_ACCOUNT")) {
            verifyTokenService.sendEmailWithToken(user, token, VerifyTokenType.ENABLE_ACCOUNT);
        }
        if (type.equals("ENABLE_ADMIN")){
            List<Long> allAdminIds = userRepo.findAllWithRoleAdmin();
            allAdminIds.forEach(adminId -> {
                User admin =  userRepo.findById(adminId).get();
                verifyTokenService.sendEmailWithUserTokenToAdmin(admin,user,token,VerifyTokenType.ENABLE_ADMIN);
            });
        }

    }

    @Override
    public boolean isTokenCorrect(String token, VerifyTokenType type) {
        if(verifyTokenService.validateToken(token)){
            VerifyToken verifyToken = verifyTokenService.getVerifyTokenByTokenValue(token);
            User user = verifyToken.getUser();

            if(type.equals(VerifyTokenType.ENABLE_ACCOUNT)) {
                user.setEnabled(true);
            }
            if(type.equals(VerifyTokenType.ENABLE_ADMIN)) {
                user.getAuthorities().add(authorityRepo.findByName(AuthorityType.ROLE_ADMIN));
            }
            userRepo.save(user);
            verifyTokenService.removeToken(token);
            return true;
        }
        return false;
    }

    @Override
    public void updateUserRoles(User user, Set<String> roles) {
        Set<Authority> userRoles = authorityRepo.findAll().stream()
                    .filter(authority -> roles.contains(authority.getAuthority()))
                    .collect(Collectors.toSet());

            //Jeśli zawiera role Admina zabierz mu ją i zrób wpis do bazy danych z odpowiednim tokenem i typem
            Authority adminAuthority = authorityRepo.findByName(AuthorityType.ROLE_ADMIN);
            if(userRoles.contains(adminAuthority))
            {
                String token = TokenGenerator.getRandomToken();
                userRoles.remove(adminAuthority);
                verifyTokenService.save(user,token,VerifyTokenType.ENABLE_ADMIN);
                sendVerifyTokenEmail(user, token, "ENABLE_ADMIN");
            }
            user.setAuthorities(userRoles);

    }
}
