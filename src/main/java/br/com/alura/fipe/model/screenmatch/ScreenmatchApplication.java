package br.com.alura.fipe.model.screenmatch;

import br.com.alura.fipe.model.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	private Principal principal = new Principal();
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		var dados = principal.obterDadosSerie();
		principal.obterDadosEpisodio();
//		principal.obterDadosEpisodio(dados.totalTemporadas());

	}


}
