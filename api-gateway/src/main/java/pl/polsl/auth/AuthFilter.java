package pl.polsl.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Mateusz on 21.11.2016.
 */
@Component
@Order(2)
public class AuthFilter implements Filter {

    @Value("${zuul.routes.auth-service.service-id}")
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
        ResponseEntity<String> entity = null;
        if (HttpMethod.OPTIONS.name().equals(httpRequest.getMethod())) {
            chain.doFilter(request, httpResponse);
            return;
        }

        if (checkAuthRegex(requestURI)) {
            HttpClientErrorException exception = null;
            try {
                entity = restTemplate.exchange("http://" + authEndpoint + "/auth/authorize", HttpMethod.GET,
                        generateHeaders(httpRequest), String.class);
            } catch (HttpClientErrorException e) {
                exception = e;
            }
            if (entity != null && entity.getStatusCode() == HttpStatus.OK) {
                chain.doFilter(request, response);
                return;
            } else if (exception != null) {
                httpResponse.sendError(exception.getRawStatusCode(), exception.getLocalizedMessage());
                //chain.doFilter(request, httpResponse);
                return;
            }
        }
        chain.doFilter(request, httpResponse);
    }

    private HttpEntity<String> generateHeaders(HttpServletRequest httpRequest) {
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        String authToken = "";
        if (httpRequest.getMethod().equals(HttpMethod.GET.name())
                && httpRequest.getRequestURI() != null
                && httpRequest.getRequestURI().contains("/auth/download")) {
            authToken = httpRequest.getParameter("authToken");
        }
        HttpHeaders headers = new HttpHeaders();
        List<String> headerNamesList = Collections.list(headerNames);
        for (String name : headerNamesList) {
            String headerValue = httpRequest.getHeader(name);
            if (headerValue != null) {
                headers.add(name, headerValue);
            }
        }
        if (!StringUtils.isEmpty(authToken)) {
            headers.add("AuthHeader", authToken);
        }
        return new HttpEntity<>(null, headers);
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
