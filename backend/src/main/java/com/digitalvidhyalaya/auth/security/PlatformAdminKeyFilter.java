package com.digitalvidhyalaya.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Temporary protection layer for management endpoints until token-based authorization is added.
 * This keeps school and user administration APIs from being publicly writable.
 */
@Component
public class PlatformAdminKeyFilter extends OncePerRequestFilter {

    private static final Set<String> PROTECTED_PREFIXES = Set.of(
            "/api/v1/tenants",
            "/api/v1/users"
    );

    private final String platformAdminKey;

    public PlatformAdminKeyFilter(
            @Value("${app.security.platform-admin-key:change-me-before-production}") String platformAdminKey
    ) {
        this.platformAdminKey = platformAdminKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/v1/auth")) {
            return true;
        }
        return PROTECTED_PREFIXES.stream().noneMatch(requestUri::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestKey = request.getHeader("X-Platform-Admin-Key");

        if (platformAdminKey.equals(requestKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {"success":false,"message":"Missing or invalid X-Platform-Admin-Key","data":null}
                """);
    }
}
