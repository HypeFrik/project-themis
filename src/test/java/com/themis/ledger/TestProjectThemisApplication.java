package com.themis.ledger;

import org.springframework.boot.SpringApplication;

public class TestProjectThemisApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProjectThemisApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
