package pl.polsl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("CanBeFinal")
@Configuration
@EnableAutoConfiguration(exclude = FreeMarkerAutoConfiguration.class)
@EnableHystrix
@RestController
public class HystrixDashboardApplicationTests {

    @Autowired
    Service service;

    @RequestMapping("/")
    public String slash() {
        return service.hello();
    }

    @Bean
    Service service() {
        return new Service();
    }

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplicationTests.class, args);
    }
}

class Service {

    @SuppressWarnings("SameReturnValue")
    @HystrixCommand
    public String hello() {
        return "Hello";
    }
}
