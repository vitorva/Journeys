/**
 * @team Journeys
 * @file WebSecurityConfig.java
 * @date January 21st, 2022
 */

package com.journeys.main.config;

import com.journeys.main.authentication.JourneysAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    JourneysAuthenticationProvider authenticationProvider;
    JwtRequestFilter jwtRequestFilter;

    /**
     * Constructor of WebSecutiryConfig
     * @param authenticationProvider
     * @param requestFilter
     */
    @Autowired
    public WebSecurityConfig(JourneysAuthenticationProvider authenticationProvider, JwtRequestFilter requestFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtRequestFilter = requestFilter;
    }

    /**
     * Authorises GET methods on the API for everyone
     * POST methods only authorised when a user is connected
     * @param http a HttpSecurity object
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/authenticate", "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy((SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     *
     * @param auth
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Creates the bean for the password encoder (used by the custom authentication provide)r
     * @return the bean for the password encoder (BCryptPasswordEncoder())
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}