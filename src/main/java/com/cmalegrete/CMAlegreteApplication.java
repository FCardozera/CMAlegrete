package com.cmalegrete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import io.swagger.v3.oas.annotations.OpenAPIDefinition;
// import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
// @OpenAPIDefinition(info = @Info(title = "CMAlegrete API", version = "v1", description = "API for management of the CMA website"))
public class CMAlegreteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CMAlegreteApplication.class, args);
	}

}
