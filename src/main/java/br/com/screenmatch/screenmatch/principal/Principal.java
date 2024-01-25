package br.com.screenmatch.screenmatch.principal;

import br.com.screenmatch.screenmatch.model.episodio.Episodio;
import br.com.screenmatch.screenmatch.model.serie.Categoria;
import br.com.screenmatch.screenmatch.model.serie.DadosSerie;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import br.com.screenmatch.screenmatch.repository.SerieRepository;
import br.com.screenmatch.screenmatch.model.temporada.DadosTemporada;
import br.com.screenmatch.screenmatch.service.ConsumoApi;
import br.com.screenmatch.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=927558f0";
    List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Serie serieEncontrada;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar série por ator
                    6 - Top 5 séries
                    7 - Busca por gênero
                    8 - Buscar por temporada e avaliação
                    9 - Buscar episódio por trecho
                    10 - Buscar os 5 melhores episódios
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeries();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    topCincoSeries();
                    break;
                case 7:
                    buscaPorGenero();
                    break;
                case 8:
                    buscarPorTemporadaMaxima();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    cincoMelhoresEpisodios();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");

            }
        }
    }

    private void cincoMelhoresEpisodios() {
        buscarSeriePorTitulo();
        List<Episodio> topCincoEpisodios = repository.topEpisodiosPorSerie(serieEncontrada);
        if(topCincoEpisodios.isEmpty()){
            System.out.println("Não foi possível realizar essa operação");
        }
        topCincoEpisodios.forEach(e -> System.out.println("Nome do episódio: " +
                e.getTitulo() +
                ", temporada: " + e.getTemporada() +
                ", episódio: " + e.getNumeroEpisodio() +
                ", avaliação: " + e.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite um trecho do nome do episódio que você está buscando: ");
        var trecho = leitura.nextLine().toLowerCase();
        List<Episodio> episodiosEncontrados = repository.episodioPorTrechoDoTitulo(trecho);
            if (episodiosEncontrados.isEmpty()){
                System.out.println("Não foi encontrado nenhum episódio com o trecho digitado");
            }
            episodiosEncontrados.forEach(e -> System.out.println("Série: " +
                    e.getSerie().getTitulo() +
                    ", episódio: " + e.getTitulo() +
                    ", temporada: " + e.getTemporada() +
                    ", número do episódio: " + e.getNumeroEpisodio()));
    }

    private void buscarPorTemporadaMaxima() {
        System.out.println("Uma série que contenha até quantas temporadas você deseja assistir ? ");
        var temporadas = leitura.nextInt();
        System.out.println("Qual a nota do seriado que você procura ? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> series = repository.seriesPorTemporadaEAvaliacao(temporadas, avaliacao);
        if(series.isEmpty()){
            System.out.println("Não foi encontrada nenhuma série com esses requisitos");
        }else {
            series.forEach(s -> System.out.println(s.getTitulo() + ", nota: " + s.getAvaliacao()));
        }
    }

    private void buscaPorGenero() {
        System.out.println("Qual categoria gostaria de pesquisar ? ");
        var categoriaBuscada = leitura.nextLine();
        var categoria = Categoria.fromPortugues(categoriaBuscada);
        List<Serie> series = repository.findByGenero(categoria);
        if (series.isEmpty()) {
            System.out.println("Não foi encontrada nenhuma série com essa categoria");
        } else {
            series.forEach(System.out::println);
        }


    }

    private void topCincoSeries() {
        List<Serie> series = repository.findTop5ByOrderByAvaliacaoDesc();
        series.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator que você deseja pesquisar: ");
        var atorNome = leitura.nextLine();
        System.out.println("A partir de qual avaliação ? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(atorNome, avaliacao);
        if (seriesEncontradas.isEmpty()) {
            System.out.println("Ator não encontrado");
        } else {
            System.out.println("Séries em que " + atorNome + " trabalhou: ");
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao())
            );
        }

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série que você deseja buscar: ");
        var nomeSerie = leitura.nextLine().toLowerCase();
        var serie = repository.findByTituloContainingIgnoreCase(nomeSerie); // titulo = titulo da classe serie
        if (serie.isPresent()) {
            serieEncontrada = serie.get();
            System.out.println("Série");
            System.out.println(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
            exibeMenu();
        }
    }

    private void listarSeries() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        dadosSeries.add(dados);
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);

    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.deserializa(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeries();
        System.out.println("De qual série você gostaria de pesquisar os episódios ? ");
        var nomeSerie = leitura.nextLine().toLowerCase();
        var serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.deserializa(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e))).collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios); //relacionando os episódios a instância da série
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }
}
