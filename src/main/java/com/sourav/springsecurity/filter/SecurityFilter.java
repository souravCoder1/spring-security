package com.sourav.springsecurity.filter;

import com.sourav.springsecurity.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
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
            if (null != usernameFromClaims && SecurityContextHolder.getContext().getAuthentication() == null) {
                // load from DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromClaims);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usernameFromClaims,
                                userDetails.getPassword(),
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
