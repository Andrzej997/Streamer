package pl.polsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EbookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbookServiceApplication.class, args);
    }

}
