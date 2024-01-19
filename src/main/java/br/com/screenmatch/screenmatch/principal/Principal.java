package br.com.screenmatch.screenmatch.principal;

import br.com.screenmatch.screenmatch.model.DadosEpisodio;
import br.com.screenmatch.screenmatch.model.DadosSerie;
import br.com.screenmatch.screenmatch.model.DadosTemporada;
import br.com.screenmatch.screenmatch.model.Episodio;
import br.com.screenmatch.screenmatch.service.ConsumoApi;
import br.com.screenmatch.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++ ){
            var temporada = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season="+i+API_KEY);
            var dadosTemporada = converteDados.deserializa(temporada, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        for (int i = 0; i < temporadas.size(); i++){
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            episodiosTemporada.forEach(e -> System.out.println(e.titulo()));
        }

        temporadas.stream().forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 5 episódios: ");
//        dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                        .peek(e -> System.out.println("Primeiro filtro(N/A)" + e))
//                        .sorted(Comparator.comparing(DadosEpisodio::avaliacao)
//                        .reversed())
//                        .peek(e -> System.out.println("Ordenação pela avaliação: " + e))
//                        .limit(5)
//                        .peek(e -> System.out.println("Limitando a 5: " + e))
//                        .map(e -> e.titulo().toUpperCase())
//                        .peek(e -> System.out.println("Colocando em maiúsculo"))
//                        .forEach(System.out::println);

        List<Episodio> episodios = temporadas
                .stream()
                .flatMap(t -> t.episodios().stream().map(e -> new Episodio(t.numeroTemporada(), e)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Qual nome de um episódio que você gostaria de assistir ? ");
//        var trechoEpisodio = scan.nextLine();
//        Optional<Episodio> episodioEncontrado = episodios.stream()
//                .filter(e -> e.getTitulo().toLowerCase().contains(trechoEpisodio.toLowerCase())) //contains apenas uma parte do titutlo digitado
//                .findFirst();
//
//        if(episodioEncontrado.isPresent()){
//            Episodio episodio = episodioEncontrado.get();
//            System.out.println("Episódio encontrado");
//            System.out.println("Temporada: " + episodio.getTemporada());
//            System.out.println(episodio);
//        }else {
//            System.out.println("Não foi encontrado nenhum episódio");
//        }

//        System.out.println("A partir de que ano você deseja ver os episódios ? ");
//        var anoEpisodio = scan.nextInt();
//        scan.nextLine();
//        LocalDate dataBusca = LocalDate.of(anoEpisodio, 1, 1);
//
//        List<Episodio> episodiosPorData = episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca) )
//                .collect(Collectors.toList());
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodiosPorData.forEach(e -> System.out.println(
//                "Temporada: " + e.getTemporada() +
//                    " Episódio: " + e.getTitulo() +
//                        " Data de lançamento: " + e.getDataLancamento().format(formatador)
//        ));


        Map<Integer, Double> notaPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        for (int i = 1; i <= notaPorTemporada.size(); i++){
            var ep = notaPorTemporada.get(i);
            System.out.println("Temporada: " + i + ", nota: " + ep);
        }

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média da série: " + est.getAverage());
        System.out.println("Nota máxima de episódio: " + est.getMax());
        System.out.println("Nota mínima de episódio: " + est.getMin());
        System.out.println("Total de episódios levados em conta: " + est.getCount());

        //
//        List<String> nome = Arrays.asList("bruno", "leandra", "jose", "paulo", "rodrigo", "nico");
//
//        List<String> nomes = nome.stream()
//                .sorted().limit(2).collect(Collectors.toList());
//
//        System.out.println(nomes);

    }
}
