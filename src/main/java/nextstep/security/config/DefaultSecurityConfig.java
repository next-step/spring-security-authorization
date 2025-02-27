package nextstep.security.config;

import nextstep.security.access.NullRoleHierarchy;
import nextstep.security.access.RoleHierarchy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultSecurityConfig {

    @Bean
    @ConditionalOnMissingBean
    public RoleHierarchy roleHierarchy() {
        return new NullRoleHierarchy();
    }
}
