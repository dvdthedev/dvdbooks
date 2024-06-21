package com.dvdthedev.dvdbooks;

import com.dvdthedev.dvdbooks.principal.Principal;
import com.dvdthedev.dvdbooks.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DvdbooksApplication implements CommandLineRunner {
	@Autowired
	private AutorRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(DvdbooksApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();

	}
}
