package projects.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"projects.user", "projects.core"})
public class CoreModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreModuleApplication.class, args);
    }

}
