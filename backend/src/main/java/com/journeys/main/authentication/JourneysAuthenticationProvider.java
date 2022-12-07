/**
 * @team Journeys
 * @file JourneysAuthenticationProvider.java
 * @date January 21st, 2022
 */

package com.journeys.main.authentication;

import com.journeys.main.model.User;
import com.journeys.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * A custom authentication provider used to authenticate
 * by verifying authentication inputs (username/passwords)
 */
@Component
public class JourneysAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserService usrService;


    /**
     * Authenticate the user passed in the authentication object
     * @param authentication
     * @return returns a token when the authentication is successful
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        User usr = usrService.getUser(userName);
        //TODO encrypt
        if (encoder.matches(password, usr.getPassword()))
            return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
        throw new BadCredentialsException("Bad Credentials");
    }

    /**
     * Get the application Encoder
     * @return a encoder
     */
    public PasswordEncoder getEncoder() {
        return encoder;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
