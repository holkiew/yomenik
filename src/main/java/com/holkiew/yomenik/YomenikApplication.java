package com.holkiew.yomenik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
public class YomenikApplication {

	public static void main(String[] args) {
		SpringApplication.run(YomenikApplication.class, args);
	}

}
