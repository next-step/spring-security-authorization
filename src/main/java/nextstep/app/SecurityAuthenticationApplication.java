package nextstep.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"nextstep"}
)
public class SecurityAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthenticationApplication.class, args);
    }

}
