package pl.polsl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.security.service.SecurityService;

/**
 * Created by Mateusz on 16.11.2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private EntrypointUnauthorizedHandler entrypointUnauthorizedHandler;

    @Autowired
    private SecurityService securityService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.securityService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ShaEncrypter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthFilter getAuthFilter() throws Exception {
        AuthFilter authFilter = new AuthFilter();
        authFilter.setAuthenticationManager(authenticationManagerBean());
        return authFilter;
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.exceptionHandling().authenticationEntryPoint(this.entrypointUnauthorizedHandler);
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/noauth/**").permitAll()
                .anyRequest().authenticated();
        httpSecurity
                .addFilterBefore(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public EntrypointUnauthorizedHandler getEntrypointUnauthorizedHandler() {
        return entrypointUnauthorizedHandler;
    }

    public void setEntrypointUnauthorizedHandler(EntrypointUnauthorizedHandler entrypointUnauthorizedHandler) {
        this.entrypointUnauthorizedHandler = entrypointUnauthorizedHandler;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
