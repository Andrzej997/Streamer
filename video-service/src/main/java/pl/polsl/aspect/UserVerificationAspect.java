package pl.polsl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.annotations.VerifyUsername;
import pl.polsl.clients.AuthServiceClient;
import pl.polsl.dto.TranscodeRequestDTO;
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoDTO;
import pl.polsl.service.VideoMetadataService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Aspect
@Component
public class UserVerificationAspect {

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Before("@annotation(pl.polsl.annotations.VerifyUsername) && execution(* pl.polsl.controller.*.*(..))")
    public void aroundAdvice(JoinPoint joinPoint) throws ResponseStatusException {
        String usernameFromArgs = getUsernameFromArgs(joinPoint);
        String authToken = getAuthToken(joinPoint);
        VerifyUsername annotation = getAnnotation(joinPoint);
        if (annotation != null && !annotation.required() && StringUtils.isEmpty(usernameFromArgs)) {
            return;
        }
        if (StringUtils.isEmpty(authToken) || StringUtils.isEmpty(usernameFromArgs)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Boolean result = authServiceClient.checkUsernameMatchesToken(authToken, usernameFromArgs);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private VerifyUsername getAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        VerifyUsername annotation = method.getAnnotation(VerifyUsername.class);
        if (annotation == null) {
            return null;
        }
        return annotation;
    }

    private Map<String, Object> getParameterNamesAndValues(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null || parameterNames.length <= 0) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>();
        Object[] args = joinPoint.getArgs();
        IntStream.range(0, parameterNames.length).forEach(i -> result.put(parameterNames[i], args[i]));
        return result;
    }

    private Class getJointpointParameterType(JoinPoint joinPoint) {
        VerifyUsername annotation = getAnnotation(joinPoint);
        return annotation != null ? annotation.parameterType() : null;
    }

    private String getJointpointParameterName(JoinPoint joinPoint) {
        VerifyUsername annotation = getAnnotation(joinPoint);
        return annotation != null ? annotation.parameterName() : null;
    }

    private String getUsernameFromArgs(JoinPoint joinPoint) {
        Map<String, Object> namesAndValues = getParameterNamesAndValues(joinPoint);
        if (namesAndValues == null || namesAndValues.size() <= 0) {
            return null;
        }
        Class parameterType = getJointpointParameterType(joinPoint);
        final String parameterName = getJointpointParameterName(joinPoint);
        if (parameterType == null) {
            parameterType = String.class;
        }
        final Class fParameterType = parameterType;
        Object usernameArg = namesAndValues.entrySet().stream().filter(entry -> {
            if (fParameterType.isInstance(entry.getValue())) {
                return StringUtils.isEmpty(parameterName) || parameterName.equalsIgnoreCase(entry.getKey());
            }
            return false;
        }).map(entry -> entry.getValue()).findFirst().orElse(null);

        if (usernameArg == null) {
            return null;
        }
        if (String.class.isAssignableFrom(parameterType)) {
            return (String) usernameArg;
        } else if (UploadVideoMetadataDTO.class.isAssignableFrom(parameterType)) {
            UploadVideoMetadataDTO param = (UploadVideoMetadataDTO) usernameArg;
            return param.getUsername();
        } else if (VideoDTO.class.isAssignableFrom(parameterType)) {
            VideoDTO param = (VideoDTO) usernameArg;
            return videoMetadataService.getUsernameByVideoId(param.getVideoId());
        } else if (TranscodeRequestDTO.class.isAssignableFrom(parameterType)){
            TranscodeRequestDTO param = (TranscodeRequestDTO) usernameArg;
            return param.getUsername();
        } else {
            return null;
        }
    }

    private String getAuthToken(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        if (parameters == null || parameters.length <= 0) {
            return null;
        }
        Map<Parameter, Object> result = new LinkedHashMap<>();
        Object[] args = joinPoint.getArgs();
        IntStream.range(0, parameters.length).forEach(i -> result.put(parameters[i], args[i]));
        Optional<String> token = result.entrySet().stream().map(entry -> {
            RequestHeader annotation = entry.getKey().getAnnotation(RequestHeader.class);
            if (annotation != null) {
                return (String) entry.getValue();
            }
            RequestParam requestParam = entry.getKey().getAnnotation(RequestParam.class);
            if (requestParam != null && "authToken".equals(requestParam.value())) {
                return (String) entry.getValue();
            }
            return null;
        }).filter(Objects::nonNull).findFirst();
        return token.orElse(null);
    }

}
