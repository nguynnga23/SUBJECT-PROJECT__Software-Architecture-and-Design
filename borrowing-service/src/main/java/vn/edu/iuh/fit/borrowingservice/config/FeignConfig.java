package vn.edu.iuh.fit.borrowingservice.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer retryer() {
        // period = 3000ms, maxPeriod = 5000ms, maxAttempts = 3
        return new Retryer.Default(3000, 5000, 3);
    }
}
