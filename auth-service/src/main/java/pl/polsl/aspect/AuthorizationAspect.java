package pl.polsl.aspect;

import jdk.nashorn.internal.parser.JSONParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.polsl.annotation.AuthMethod;
import pl.polsl.dto.RegistrationDTO;
import pl.polsl.security.Tokenizer;
import pl.polsl.security.service.SecurityService;

import java.lang.reflect.Method;

/**
 * Created by Mateusz on 17.11.2016.
 */
@Component
@Aspect
public class AuthorizationAspect {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Tokenizer tokenizer;

    @Autowired
    private SecurityService securityService;

    @Around("@annotation(pl.polsl.annotation.AuthMethod) && execution(* pl.polsl.controler.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = null;
        try {
            Credentials credentials = beforeProceed(joinPoint);

            returnObject = joinPoint.proceed();

            if (returnObject instanceof ResponseEntity) {
                returnObject = afterProceed((ResponseEntity) returnObject, credentials);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
        }
        return returnObject;
    }

    private Credentials beforeProceed(ProceedingJoinPoint joinPoint) {
        String[] jointpointAuthParameters = getJointpointAuthParameters(joinPoint);
        Object[] args = joinPoint.getArgs();
        Credentials credentials = getCredentialsFromArgs(jointpointAuthParameters, args);
        if (credentials != null) {
            Authentication authentication = generateAuthentication(
                    credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return credentials;
    }

    private ResponseEntity<?> afterProceed(ResponseEntity returnObject, Credentials credentials) {
        ResponseEntity<?> loginResponse = (ResponseEntity<?>) returnObject;
        String token = null;
        if (credentials != null) {
            token = tokenizer.generateToken(getUserDetails(
                    credentials.getUsername(), credentials.getEmail(), credentials.getPassword()));
        }
        if (token != null) {
            return new ResponseEntity<>(JSONParser.quote(token), loginResponse.getHeaders(), loginResponse.getStatusCode());
        } else {
            return new ResponseEntity<>(null, loginResponse.getHeaders(), loginResponse.getStatusCode());
        }
    }

    private String[] getJointpointAuthParameters(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AuthMethod annotation = method.getAnnotation(AuthMethod.class);
        if (annotation == null) {
            return null;
        }
        return annotation.params();
    }

    private Credentials getCredentialsFromArgs(String[] jointpointAuthParameters, Object[] args) {
        if (jointpointAuthParameters == null || jointpointAuthParameters.length <= 0) {
            return null;
        }
        Credentials credentials = new Credentials();
        for (String param : jointpointAuthParameters) {
            if ("username".equals(param)) {
                credentials.setUsername((String) args[0]);
            }
            if ("email".equals(param)) {
                credentials.setEmail((String) args[0]);
            }
            if ("password".equals(param)) {
                credentials.setPassword((String) args[1]);
            }
            if ("registrationDTO".equals(param)) {
                RegistrationDTO dto = (RegistrationDTO) args[0];
                if (dto != null) {
                    credentials.setUsername(dto.getUsername());
                    credentials.setPassword(dto.getPassword());
                    credentials.setEmail(dto.getEmail());
                }
            }
        }
        return credentials;
    }

    private Authentication generateAuthentication(String username, String password, String email) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        }
        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(password)) {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        }
        return null;
    }

    private UserDetails getUserDetails(String username, String email, String password) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            return securityService.getUserByUsername(username);
        }
        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(password)) {
            return securityService.getUserByEmail(email, password);
        }
        return null;
    }

    class Credentials {
        private String username;
        private String password;
        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
