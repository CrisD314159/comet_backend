package com.crisd.comet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    /**
     * This class is used to configure spring security framework
     * passwordEncoder is a bean that handles password encryption or comparison
     * authenticationManager handles the authentication whe we requested on the account serivce
     * securityFilterChain is where we set the protected routes and where we can set up Oauth2
     */

     private final CustomUserDetailsService customUserDetailsService;
     private final AuthTokenFilter authTokenFilter;
     private final CustomSuccessHandler customSuccessHandler;
     private final CustomFailureHandler customFailureHandler;

    /*
     * Password encoder bean (uses BCrypt hashing)
     * Critical for secure password storage
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*
     * Authentication manager bean
     * Required for programmatic authentication (e.g., in /generateToken)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/auth/login", "/auth/refresh","auth/resetAccount","/auth/changePassword",
                                "/auth/logout", "/users/signUp", "/users/verifyAccount").permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Oauth for google authentication
                .oauth2Login(oauth -> {

                    oauth.successHandler(customSuccessHandler);
                    oauth.failureHandler(customFailureHandler);

                })
                .userDetailsService(customUserDetailsService)
                // Stateless session (required for JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
