package com.scentalux.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORS implements Filter {

    // List of trusted domains
    private static final List<String> TRUSTED_ORIGINS = Arrays.asList(
        "https://trustedsite1.com",
        "https://trustedsite2.com"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // You can initialize configurations here if needed
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // Get the 'Origin' header from the request
        String origin = request.getHeader("Origin");

        // If the origin is in the trusted list, set it in the response
        if (origin != null && TRUSTED_ORIGINS.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        // Allow specific methods
        response.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT");

        // Set max age to cache pre-flight requests
        response.setHeader("Access-Control-Max-Age", "3600");

        // Set allowed headers
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");

        // Handle pre-flight OPTIONS request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // Cleanup resources if needed
    }
}
