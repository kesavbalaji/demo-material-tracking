package com.example.demo.utility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class LicenseCheckFilter extends OncePerRequestFilter {

    private final LocalDate licenseExpiryDate = LocalDate.of(2024, 10, 15); // Set your license expiry date here

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (LocalDate.now().isAfter(licenseExpiryDate)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "License expired");
            return;
        }
        filterChain.doFilter(request, response);
    }
}


