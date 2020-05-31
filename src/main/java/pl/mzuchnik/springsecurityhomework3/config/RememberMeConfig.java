package pl.mzuchnik.springsecurityhomework3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;

@Configuration
public class RememberMeConfig implements Customizer<RememberMeConfigurer<HttpSecurity>> {

    @Override
    public void customize(RememberMeConfigurer rememberMeConfigurer) {
        rememberMeConfigurer.key("MySecurityKey");
        rememberMeConfigurer.tokenValiditySeconds(120);
    }
}
