package pl.mzuchnik.springsecurityhomework3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mzuchnik.springsecurityhomework3.entity.Authority;
import pl.mzuchnik.springsecurityhomework3.entity.AuthorityType;
import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.repository.AuthorityRepo;
import pl.mzuchnik.springsecurityhomework3.repository.UserRepo;

@Service
@Primary
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepo userRepo;
    private AuthorityRepo authorityRepo;

    @Autowired
    public UserDetailServiceImpl(UserRepo userRepo, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        return user;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addTestUser()
    {
        Authority userRole = new Authority(AuthorityType.ROLE_USER);
        Authority adminRole = new Authority(AuthorityType.ROLE_ADMIN);
        Authority moderatorRole = new Authority(AuthorityType.ROLE_MODERATOR);

        authorityRepo.save(userRole);
        authorityRepo.save(adminRole);
        authorityRepo.save(moderatorRole);


        User user = new User("user",new BCryptPasswordEncoder().encode("user"));
        user.setEnabled(true);
        user.getAuthorities().add(authorityRepo.findByName(AuthorityType.ROLE_USER));
        user.getAuthorities().add(authorityRepo.findByName(AuthorityType.ROLE_ADMIN));
        user.getAuthorities().add(authorityRepo.findByName(AuthorityType.ROLE_MODERATOR));

        userRepo.save(user);
    }
}
