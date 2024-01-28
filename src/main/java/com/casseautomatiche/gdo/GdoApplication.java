package com.casseautomatiche.gdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class GdoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdoApplication.class, args);
	}
}
