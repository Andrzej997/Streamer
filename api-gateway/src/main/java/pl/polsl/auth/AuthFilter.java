package pl.polsl.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mateusz on 21.11.2016.
 */
@Component
public class AuthFilter implements Filter {

    @Value("${zuul.routes.auth-service.url}")
    private String authEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        ResponseEntity<?> entity = null;
        if (checkAuthRegex(requestURI)) {
            HttpClientErrorException exception = null;
            try {
                entity = (ResponseEntity) restTemplate.getForEntity(authEndpoint + "/auth/authorize", ResponseEntity.class);
            } catch (HttpClientErrorException e) {
                exception = e;
            }
            if (entity != null && entity.getStatusCode() == HttpStatus.OK) {
                chain.doFilter(request, response);
            } else if (exception != null) {
                httpResponse.sendError(exception.getRawStatusCode(), exception.getLocalizedMessage());
                chain.doFilter(request, httpResponse);
            }
        }
        chain.doFilter(request, httpResponse);
    }

    private Boolean checkAuthRegex(String URI) {
        if (URI.contains("/auth/auth/") || URI.contains("/auth/noauth/")) {
            return false;
        }
        if (URI.contains("/auth/")) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
