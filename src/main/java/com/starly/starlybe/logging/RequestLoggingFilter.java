package com.starly.starlybe.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString();

        try {
            MDC.put("traceId", traceId);
            MDC.put("httpMethod", request.getMethod());
            MDC.put("requestURI", request.getRequestURI());

            response.setHeader("X-Trace-Id", traceId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}