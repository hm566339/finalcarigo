package com.carigo.drivinglicense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DrivinglicenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrivinglicenseApplication.class, args);
	}

}
