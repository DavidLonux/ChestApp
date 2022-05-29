package pl.lonux.clientapplication.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(auth -> {
            auth.antMatchers("/").hasRole("USER");
            auth.anyRequest().authenticated();
        });

        httpSecurity.formLogin();
        httpSecurity.logout();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(List.of(
            User.builder()
                .username("David")
                .password(passwordEncoder().encode("user"))
                .roles("USER").build()
        ));
    }
    
}