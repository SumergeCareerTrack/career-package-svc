package com.sumerge.careertrack.career_package_svc.filters;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sumerge.careertrack.career_package_svc.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        boolean isValidToken = jwtService.isTokenValid(token);

        if (!isValidToken) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().println("Token missing or incorrect.");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication());
        filterChain.doFilter(request, response);
        return;

    }

}
