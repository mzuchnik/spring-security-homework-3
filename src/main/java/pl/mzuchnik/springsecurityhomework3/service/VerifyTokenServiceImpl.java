package pl.mzuchnik.springsecurityhomework3.service;

import org.springframework.stereotype.Service;
import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyToken;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;
import pl.mzuchnik.springsecurityhomework3.repository.VerifyTokenRepo;
import javax.transaction.Transactional;

@Service
public class VerifyTokenServiceImpl implements VerifyTokenService{

    private VerifyTokenRepo verifyTokenRepo;
    private MailService mailService;

    public VerifyTokenServiceImpl(VerifyTokenRepo verifyTokenRepo, MailService mailService) {
        this.verifyTokenRepo = verifyTokenRepo;
        this.mailService = mailService;
    }

    @Override
    public void save(User user, String token, VerifyTokenType type){
        VerifyToken verifyToken = new VerifyToken();
        verifyToken.setToken(token);
        verifyToken.setUser(user);
        verifyToken.setVerifyTokenType(type);
        verifyTokenRepo.save(verifyToken);
    }

    @Override
    public void sendEmailWithToken(User user, String token, VerifyTokenType type){
        save(user,token,type);
        mailService.sendEmail(user.getUsername(),
                    "server@server.com",
                    "Verify Token",
                    "Hi, please confirm your account:\nEnter to this link: " + getServerPathForVerifyToken(token, type));
    }

    @Override
    public void sendEmailWithUserTokenToAdmin(User admin, User user, String token, VerifyTokenType type) {
        mailService.sendEmail(admin.getUsername(),
                "server@server.com",
                user.getUsername() + " asks for " + type.name(),
                "Hi " + admin.getUsername() + "\nUser "+ user.getUsername() + " is asking for " + type.name()+ "\nIf u want to enable this click this link: "+ getServerPathForVerifyToken(token,type));
    }

    @Override
    public boolean validateToken(String token) {
        VerifyToken verifyToken = verifyTokenRepo.findByToken(token);
        return verifyToken.getToken().equals(token);
    }

    @Override
    @Transactional
    public void removeToken(String token) {
        verifyTokenRepo.deleteAllByToken(token);
    }

    private String getServerPathForVerifyToken(String token, VerifyTokenType type)
    {
        return TokenGenerator.getGeneratedPathWithToken(token, type);
    }

    @Override
    public VerifyToken getVerifyTokenByTokenValue(String token){
        return verifyTokenRepo.findByToken(token);
    }
}
