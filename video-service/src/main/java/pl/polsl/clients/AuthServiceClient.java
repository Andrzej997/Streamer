package pl.polsl.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="auth-service")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/noauth/token/verify/username")
    Boolean checkUsernameMatchesToken(@RequestParam(value = "token") String token,
                                             @RequestParam(value = "username") String username);
}
