package com.gyt.managementservice.configurations;


import com.gyt.corepackage.configuration.BaseSecurityService;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final BaseSecurityService baseSecurityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        baseSecurityService.configureCoreSecurity(http);
        http
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/addOrganization").hasAnyAuthority("admin")
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/getById/{id}").hasAnyAuthority("admin")
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/getAll").hasAnyAuthority("admin")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/user/update").hasAnyAuthority("admin","organization")
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

}
