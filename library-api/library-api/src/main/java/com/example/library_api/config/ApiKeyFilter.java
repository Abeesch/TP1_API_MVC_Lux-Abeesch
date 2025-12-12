package com.example.library_api.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    @Value("${app.api-key}")
    private String apiKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();

        // GET = public, les autres => clé API obligatoire
        if (!"GET".equalsIgnoreCase(method)) {
            String headerKey = req.getHeader("X-API-KEY");
            if (headerKey == null || !headerKey.equals(apiKey)) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("""
                        { "status": 401, "message": "Missing or invalid X-API-KEY" }
                        """);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
