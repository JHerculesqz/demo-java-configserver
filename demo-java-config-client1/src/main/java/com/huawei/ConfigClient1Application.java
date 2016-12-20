package com.huawei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ConfigClient1Application {
	// #region main

	public static void main(String[] args) {
		SpringApplication.run(ConfigClient1Application.class, args);
	}

	// #endregion
}
