package com.e_commerce;

import com.e_commerce.entity.User;
import com.e_commerce.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class ECommerceApplication {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			if (userRepo.findByRole("ADMIN").isEmpty()) {
				User adminUser = new User();
				adminUser.setEmail("admin@gmail.com");
				adminUser.setPassword(passwordEncoder.encode("admin123"));
				adminUser.setRole("ADMIN");
				adminUser.setCreatedAt(LocalDateTime.now());

				userRepo.save(adminUser);
				System.out.println("Admin user created: " + adminUser.getEmail());
			}
		};
	}

}
