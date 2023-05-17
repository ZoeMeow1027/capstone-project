package io.zoemeow.pbl6.phonestoremanager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.zoemeow.pbl6.utils.utils;

@SpringBootApplication
public class PhonestoremanagerApplication {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		utils.turnOffSslChecking();
		SpringApplication.run(PhonestoremanagerApplication.class, args);
	}

}
