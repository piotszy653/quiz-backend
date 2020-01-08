package projects.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"projects.storage", "projects.user", "projects.core", "projects.quiz"})
public class CoreModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreModuleApplication.class, args);
    }

}
