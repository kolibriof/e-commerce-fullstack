package com.ecommerce.ecommerce_be.security;

import com.ecommerce.ecommerce_be.dtos.UserFilterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class SessionFilter extends OncePerRequestFilter {
    // IMPLEMENT JWT TOKEN AUTHENTICATION!

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionId = session.getId();
            System.out.println("Existing SESSION ID: " + sessionId);
        } else {
            System.out.println("No existing session found");
        }

        CustomHttpServletRequestWrapper wrapperRequest = new CustomHttpServletRequestWrapper(request);

        UserFilterDTO loginReceived = new ObjectMapper().readValue(wrapperRequest.getBody(), UserFilterDTO.class);
        String login = loginReceived.getLogin();
        String password = loginReceived.getPassword();

        System.out.println("Received login: " + login);
        System.out.println("Received password: " + password);
        System.out.println("REQUEST URI: " + request.getRequestURI());

        if (!"/login".equals(request.getRequestURI())) {
            if (!checkSessionValidity(session)) {
                throw new SessionAuthenticationException(login+" is not authenticated!");
            }

            filterChain.doFilter(wrapperRequest, response);
            return;
        }

        session = request.getSession(true);

        System.out.println("New SESSION ID: " + session.getId());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(login, password, Collections.emptyList()));
        filterChain.doFilter(wrapperRequest, response);
    }

    private boolean checkSessionValidity(HttpSession session) {
        if(session == null) {
            return false;
        }

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if(securityContext != null) {
            Authentication authentication =  securityContext.getAuthentication();
            return null == authentication || authentication.isAuthenticated();
        } else {
            return false;
        }
     }
}