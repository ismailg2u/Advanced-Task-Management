package dev.definex.finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "dev.definex.finalproject")
public class FinalprojectApplication {


	public static void main(String[] args) {
		SpringApplication.run(FinalprojectApplication.class, args);
	}

}
