package org.iamliterallyhim.OSWars;

import org.iamliterallyhim.OSWars.Controller.ProtectionController;

import java.io.IOException;
import java.util.Base64;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        try {
            authHeader = authHeader.substring(6);
            authHeader = new String(Base64.getDecoder().decode(authHeader));
            String[] creds = authHeader.split(":");
            authHeader = creds[0] + ":" + ProtectionController.hash(creds[1]);
            authHeader = "Basic " + Base64.getEncoder().encodeToString(authHeader.getBytes());
            String finalAuthHeader = authHeader;

            HttpServletRequest newRequest = (HttpServletRequest) request;
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(newRequest) {
                @Override
                public String getHeader(String name) {
                    if (name.equalsIgnoreCase("Authorization")) {
                        return finalAuthHeader;
                    } else {
                        return super.getHeader(name);
                    }
                }
            };
            filterChain.doFilter(wrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/addUser");
    }
}

