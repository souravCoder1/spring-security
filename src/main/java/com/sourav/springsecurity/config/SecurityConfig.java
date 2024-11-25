package com.sourav.springsecurity.config;

import com.sourav.springsecurity.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private InvalidUserAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for H2 console
                .authorizeHttpRequests((requests) -> {
                    try {
                        requests
                                .requestMatchers("/h2-console/**", "api/users/create/user", "api/users/login").permitAll()
                                .anyRequest().authenticated()
                        .and()
                                .exceptionHandling()
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                                .headers().frameOptions().disable() // Allow framing for H2 console
                        .frameOptions().sameOrigin(); // Allow framing of the console;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }*/
}
