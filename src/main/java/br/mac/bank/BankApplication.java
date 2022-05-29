package br.mac.bank;

import br.mac.bank.model.*;
import br.mac.bank.repositories.AccountRepo;
import br.mac.bank.repositories.CurrencyRepo;
import br.mac.bank.repositories.TaxRepo;
import br.mac.bank.service.RoleService;
import br.mac.bank.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}


	@Bean
	CommandLineRunner run(UserService userService,
						  RoleService roleService,
						  AccountRepo repo,
						  TaxRepo taxRepo,
						  CurrencyRepo currencyRepo){
		return args -> {
			createDefaultRecords(userService, roleService, repo, taxRepo, currencyRepo);
		};
	}

	private void createDefaultRecords(UserService userService, RoleService roleService, AccountRepo repo, TaxRepo taxRepo, CurrencyRepo currencyRepo) {

		roleService.saveRole(new Role(null, "ADMIN"));
		roleService.saveRole(new Role(null, "USER"));

		User user = userService.saveUser(new User(null, "Admin", "admin", "123", new ArrayList<>()));
		User user2 = userService.saveUser(new User(null, "User", "user", "123", new ArrayList<>()));

		roleService.addToUser(user.getUsername(), "ADMIN");
		roleService.addToUser(user2.getUsername(), "USER");

		currencyRepo.save(new Currency(null, "USD", "American Dollar"));

		repo.save(new Account(null, new BigDecimal(2000), null));
		repo.save(new Account(null, new BigDecimal(2000), null));

		taxRepo.save(new Tax(null, BigDecimal.ZERO, new BigDecimal(100), new BigDecimal(2)));
		taxRepo.save(new Tax(null, new BigDecimal(100), null, new BigDecimal(5)));
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
