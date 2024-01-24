package br.com.screenmatch.screenmatch.principal;

import br.com.screenmatch.screenmatch.model.episodio.Episodio;
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

    public Principal(SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0){
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
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
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");

            }
        }
    }

    private void listarSeries(){
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

    private void buscarEpisodioPorSerie(){
        listarSeries();
        System.out.println("De qual série você gostaria de pesquisar os episódios ? ");
        var nomeSerie = leitura.nextLine().toLowerCase();
        var serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie))
                .findFirst();

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
                            .map(e -> {
                                Episodio episodio = new Episodio(d.numeroTemporada(), e);
                                episodio.setSerie(serieEncontrada); // Associa o episódio à série
                                return episodio;
                            }))
                    .collect(Collectors.toList());

            episodioRepository.saveAll(episodios); // Salva os episódios usando o EpisodioRepository
        } else {
            System.out.println("Série não encontrada");
        }
    }
}
