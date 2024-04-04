package com.sourav.springsecurity.filter;

import com.sourav.springsecurity.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter{
    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the JWT token from the Authorization header
        String token = request.getHeader("Authorization");

        // If token is not null and starts with "Bearer ", extract the token value
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            // Your custom logic to validate the token and authenticate the user
            // For example, you can use a JWT library to parse and verify the token
            String usernameFromClaims = jwtUtil.getUsernameFromClaims(token);
            if(null != usernameFromClaims && SecurityContextHolder.getContext().getAuthentication() == null) {
                // load from DB
            }

            // If the token is valid, you may set the authentication in SecurityContextHolder
            // For example, if using Spring Security:
             Authentication authentication = yourCustomAuthenticationLogic(token);
             SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
