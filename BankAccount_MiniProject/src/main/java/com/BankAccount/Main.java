package com.BankAccount;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.interfaces.InterfaceAccount;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	@Bean
	public CommandLineRunner init(InterfaceAccount domainAccount) {
		return (args) -> {
			// create account
			domainAccount.save(new DomainAccount(0));
		};
	}
}
