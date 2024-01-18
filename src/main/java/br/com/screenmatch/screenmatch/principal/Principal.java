package br.com.screenmatch.screenmatch.principal;

import br.com.screenmatch.screenmatch.model.DadosSerie;
import br.com.screenmatch.screenmatch.service.ConsumoApi;
import br.com.screenmatch.screenmatch.service.ConverteDados;

import java.util.Scanner;

public class Principal {

    private Scanner scan = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();

    public void exibeMenu(){
        var menu = """
                Digite o nome da série para busca:
                
                """;

        System.out.println(menu);
        var nomeSerie = scan.nextLine();
        var endereco = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        var serie = consumoApi.obterDados(endereco);
        var dadosSerie = converteDados.deserializa(serie, DadosSerie.class);
        System.out.println(dadosSerie);

    }
}
