package pl.mzuchnik.springsecurityhomework3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringSecurityHomework3Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityHomework3Application.class, args);
	}

}
