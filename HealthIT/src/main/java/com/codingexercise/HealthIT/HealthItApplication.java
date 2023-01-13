package com.codingexercise.HealthIT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.codingexercise.HealthIT.controller"})
public class HealthItApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthItApplication.class, args);
		System.out.println("server started...........");
	}
}
