package io.zoemeow.pbl6.phonestoremanager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.zoemeow.pbl6.phonestoremanager.utils.SSLOptions;

@SpringBootApplication
public class PhoneStoreManagerApp {
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		SSLOptions.turnOffSslChecking();
		SpringApplication.run(PhoneStoreManagerApp.class, args);
	}
}
