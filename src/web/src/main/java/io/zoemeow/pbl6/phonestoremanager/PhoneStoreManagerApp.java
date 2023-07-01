package io.zoemeow.pbl6.phonestoremanager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.zoemeow.pbl6.phonestoremanager.utils.SSLOptions;

@SpringBootApplication
public class PhoneStoreManagerApp extends SpringBootServletInitializer {
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PhoneStoreManagerApp.class);
    }

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		SSLOptions.turnOffSslChecking();
		SpringApplication.run(PhoneStoreManagerApp.class, args);
	}
}
