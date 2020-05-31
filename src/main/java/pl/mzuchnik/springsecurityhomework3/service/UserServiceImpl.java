package pl.mzuchnik.springsecurityhomework3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mzuchnik.springsecurityhomework3.entity.*;
import pl.mzuchnik.springsecurityhomework3.repository.RoleRepo;
import pl.mzuchnik.springsecurityhomework3.repository.UserRepo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private VerifyTokenService verifyTokenService;
    private RoleRepo roleRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, VerifyTokenService verifyTokenService, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.verifyTokenService = verifyTokenService;
        this.roleRepo = roleRepo;
    }

    @Override
    public void addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        updateUserRoles(user);
        userRepo.save(user);
    }

    @Override
    public void sendVerifyTokenEmail(User user, String token, String type) {
        if(type.equals("ENABLE_ACCOUNT")) {
            verifyTokenService.sendEmailWithToken(user, token, VerifyTokenType.ENABLE_ACCOUNT);
        }
        if (type.equals("ENABLE_ADMIN")){
            List<User> admins = userRepo.findByRoles(roleRepo.findByName("ROLE_ADMIN"));
            admins.forEach(admin->verifyTokenService.sendEmailWithUserTokenToAdmin(admin,user,token,VerifyTokenType.ENABLE_ADMIN));
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
                user.getRoles().add(roleRepo.findByName("ROLE_ADMIN"));
            }
            userRepo.save(user);
            verifyTokenService.removeToken(token);
            return true;
        }
        return false;
    }

    @Override
    public void updateUserRoles(User user) {
        Set<Role> userRoles = user.getRoles();

            //Jeśli zawiera role Admina zabierz mu ją i zrób wpis do bazy danych z odpowiednim tokenem i typem
            Role adminRole = roleRepo.findByName("ROLE_ADMIN");
            if(userRoles.contains(adminRole))
            {
                String token = TokenGenerator.getRandomToken();
                userRoles.remove(adminRole);
                verifyTokenService.save(user,token,VerifyTokenType.ENABLE_ADMIN);
                sendVerifyTokenEmail(user, token, "ENABLE_ADMIN");
            }
            user.setRoles(userRoles);

    }
}
