package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/clients").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/clients/current/accounts").hasRole("CLIENT")
                .antMatchers(HttpMethod.POST, "/clients/current/cards").hasRole("CLIENT")
                .antMatchers(HttpMethod.POST, "/clients/current/transactions").hasRole("CLIENT")
                .antMatchers(HttpMethod.POST, "/clients/current/loans").hasRole("CLIENT")
                .antMatchers(HttpMethod.PUT, "/clients/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/clients/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/confirm-account").permitAll()
                .antMatchers(HttpMethod.POST,"/confirm-account").permitAll();

        http.authorizeRequests()
                .antMatchers( "/web/js/**", "/web/assets/**","/web/styles/**", "/web/index.html", "/web/confirm-account.html").permitAll()
                .antMatchers("/rest/**", "/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html", "/web/account.html", "/api/clients/{id}").hasAuthority("CLIENT");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}

