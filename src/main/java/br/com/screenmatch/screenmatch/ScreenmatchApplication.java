package br.com.screenmatch.screenmatch;

import br.com.screenmatch.screenmatch.model.DadosEpisodio;
import br.com.screenmatch.screenmatch.model.DadosSerie;
import br.com.screenmatch.screenmatch.model.DadosTemporada;
import br.com.screenmatch.screenmatch.service.ConsumoApi;
import br.com.screenmatch.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		ConverteDados converteDados = new ConverteDados();
		var serie = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(serie);
		var dadosSerie = converteDados.obterDados(serie, DadosSerie.class);
		System.out.println(dadosSerie);
		var episodio = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=927558f0");
		var dadosEpisodio = converteDados.obterDados(episodio, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++ ){
			var temporada = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season="+i+"&apikey=927558f0");
			var dadosTemporada = converteDados.obterDados(temporada, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.stream().forEach(System.out::println);

	}
}
