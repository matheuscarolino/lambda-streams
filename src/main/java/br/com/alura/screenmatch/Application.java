package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private Principal principal = new Principal();
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@java.lang.Override
	public void run(java.lang.String... args) throws Exception {

//		var carros = principal.getMarcas("carros");
//		var modelos = principal.getModelos("carros", 56);
//		var carros = principal.getVeiculos("carros", 59, 5940);

		principal.exibeMenu();
	}

}
