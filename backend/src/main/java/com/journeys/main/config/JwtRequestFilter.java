/**
 * @team Journeys
 * @file JwtRequestFilter.java
 * @date January 21st, 2022
 */

package com.journeys.main.config;

import com.journeys.main.model.User;
import com.journeys.main.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Verify the access token  if one exist
 * in every http-request
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties cookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Cookie accessCookie = getCookieByName(request.getCookies());

        String username = null;
        String jwt = null;

        if (accessCookie != null && !accessCookie.getValue().isEmpty()) {
            jwt = accessCookie.getValue();
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User userDetails = this.authenticationService.loadByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, null);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Method getting the Cookie by its name
     * @param cookies an array of Cookie
     * @return a Cookie
     */
    private Cookie getCookieByName(Cookie[] cookies) {
        Cookie requestedCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName.getAccessToken())) {
                    requestedCookie = cookie;
                    break;
                }
            }
        }
        return requestedCookie;
    }
}