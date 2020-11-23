package com.passwordwallet;

import com.passwordwallet.security.EncryptionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
public class PasswordwaletApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordwaletApplication.class, args);

//		System.out.println(EncryptionService.generateSalt());
//		System.out.println(EncryptionService.encryptPassword("Test1", "[B@ae90205", "SHA-512"));
//		System.out.println(EncryptionService.encryptPassword("Test2", "", "HMAC"));
	}

}
