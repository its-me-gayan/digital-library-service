package org.gayan.dls.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/23/25
 * Time: 12:54 AM
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll() // health endpoint allowed
                        .anyRequest().authenticated() // everything else requires authentication
                )
                .httpBasic(httpBasic -> {}); // ✅ new style (lambda config)

        return http.build();
    }
}
