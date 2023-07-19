package com.cocus.doctorLablling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DoctorLabllingApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(DoctorLabllingApplication.class, args);
	}

}
