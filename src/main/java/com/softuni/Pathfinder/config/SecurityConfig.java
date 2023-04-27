package com.softuni.Pathfinder.config;

import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.impl.PathfinderUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // define witch requests are allowed and witch not
                .authorizeHttpRequests()
                // everyone can download static resources(html, css, js)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // pages available for everyone
                .requestMatchers("/", "/routes", "/routes/pedestrian", "/routes/bicycle",
                        "/routes/car", "/routes/motorcycle", "/routes/{id}/details", "/about",
                        "/users/login", "/users/login-error", "/users/register").permitAll()
                // pages available only for admins
                .requestMatchers("/statistics").hasRole(RoleEnum.ADMIN.name())
                // all other pages required logged-in user
                .anyRequest().authenticated()
                .and()
                // configuration of login form
                .formLogin()
                // custom login form
                .loginPage("/users/login")
                // the name of the username form field
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                // the name of the password form field
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                // where to go when login is successful
                .defaultSuccessUrl("/", true)
                // where to go if login failed
                .failureForwardUrl("/users/login-error")
                .and()
                // configure logout
                .logout()
                // logout url
                .logoutUrl("/users/logout")
                // where to go if logout is successful
                .logoutSuccessUrl("/")
                // invalidate http session and delete cookies
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new PathfinderUserDetailsServiceImpl(userRepository);
    }
}
