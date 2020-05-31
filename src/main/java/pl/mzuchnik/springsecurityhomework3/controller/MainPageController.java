package pl.mzuchnik.springsecurityhomework3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.mzuchnik.springsecurityhomework3.entity.User;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;
import pl.mzuchnik.springsecurityhomework3.repository.AuthorityRepo;
import pl.mzuchnik.springsecurityhomework3.service.TokenGenerator;
import pl.mzuchnik.springsecurityhomework3.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
public class MainPageController {

    private AuthorityRepo authorityRepo;
    private UserService userService;

    public MainPageController(AuthorityRepo authorityRepo, UserService userService) {
        this.authorityRepo = authorityRepo;
        this.userService = userService;
    }

    @GetMapping
    public String showMainPage(Principal principal,Model model)
    {
        if(principal != null)
            model.addAttribute("username", principal.getName());
        else
            model.addAttribute("username","Niezalogowany");
        return "index";
    }

    @GetMapping("/sign-up")
    public String getSignUpForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", authorityRepo.findAll());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUserSignUpForm(@ModelAttribute(name = "user") User user,
                                     @RequestParam(name = "roles") Set<String> roles) {
        String token = TokenGenerator.getRandomToken();
        userService.sendVerifyTokenEmail(user, token, VerifyTokenType.ENABLE_ACCOUNT.name());
        userService.addNewUser(user, roles);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @GetMapping("/verify-token")
    public String verifyToken(@RequestParam(name = "token") String token,
                              @RequestParam(name= "type") String type)
    {
        if(type.equals(VerifyTokenType.ENABLE_ACCOUNT.name())) {
            if (userService.isTokenCorrect(token,VerifyTokenType.ENABLE_ACCOUNT)) {
                return "redirect:/login";
            }
        }
        if(type.equals(VerifyTokenType.ENABLE_ADMIN.name())){
            if (userService.isTokenCorrect(token,VerifyTokenType.ENABLE_ADMIN)) {
                return "redirect:/login";
            }
        }
        return "redirect:";
    }

}
