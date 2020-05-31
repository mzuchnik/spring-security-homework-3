package pl.mzuchnik.springsecurityhomework3.autostart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.mzuchnik.springsecurityhomework3.entity.Role;
import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.repository.RoleRepo;
import pl.mzuchnik.springsecurityhomework3.repository.UserRepo;

import java.util.HashSet;

@Component
public class AutoStart {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public AutoStart(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addRolesToDatabase()
    {
        Role user = new Role("ROLE_USER");
        Role admin = new Role("ROLE_ADMIN");

        roleRepo.save(user);
        roleRepo.save(admin);

        User administrator = new User("admin@admin.com",
                encoder.encode("admin12345"),
                new HashSet<>(roleRepo.findAll()));
        administrator.setEnabled(true);

        userRepo.save(administrator);
    }
}
