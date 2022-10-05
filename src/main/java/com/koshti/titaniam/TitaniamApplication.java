package com.koshti.titaniam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TitaniamApplication {
	public static ConfigurableApplicationContext context = null;
	public static void main(String[] args) {
		context = SpringApplication.run(TitaniamApplication.class, args);
	}

}
