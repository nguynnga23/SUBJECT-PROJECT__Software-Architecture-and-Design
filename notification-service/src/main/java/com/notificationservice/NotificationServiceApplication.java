package com.notificationservice;

import com.notificationservice.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {

	public static void main(String[] args) {
		EnvLoader.load(); // Load biến môi trường từ file .env
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
