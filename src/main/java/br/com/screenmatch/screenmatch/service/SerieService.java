package br.com.screenmatch.screenmatch.service;

import br.com.screenmatch.screenmatch.dto.SerieDTO;
import br.com.screenmatch.screenmatch.model.episodio.Episodio;
import br.com.screenmatch.screenmatch.model.episodio.EpisodioDTO;
import br.com.screenmatch.screenmatch.model.serie.Categoria;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import br.com.screenmatch.screenmatch.repository.SerieRepository;
import org.antlr.v4.runtime.atn.EpsilonTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries(){
        return transformaSerieEmDto(repository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return transformaSerieEmDto(repository.findTop5ByOrderByAvaliacaoDesc());
    }
    public List<SerieDTO> obterLancamentos() {
        return transformaSerieEmDto(repository.lancamentosMaisRecentes());
    }

    public SerieDTO pegaSeriePeloId(Long id){
        var serie = repository.findById(id);
        if(serie.isPresent()){
            return new SerieDTO(serie.get());
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public List<EpisodioDTO> todosOsEpisodiosPorSerie(Long id) {
        var serie = repository.findById(id);
        if(serie.isPresent()){
            return serie.stream()
                    .flatMap(s -> s.getEpisodios().stream()
                            .map(e -> new EpisodioDTO(e))).collect(Collectors.toList());
        }
        throw new RuntimeException("Série não encontrada");
    }
    public List<EpisodioDTO> obterEpisodiosPorTemporada(Long id, Integer numeroTemporada) {
        var episodios = repository.episodiosPorTemporadaEspecifica(id, numeroTemporada);
        if(episodios.isEmpty()){
            throw new RuntimeException("Erro ao carregar episódios da temporada " + numeroTemporada);
        }

        return transformaEpisodioEmDto(episodios);
    }
    public List<SerieDTO> obterSeriesPorCategoria(String categoria) {
        var cat = Categoria.fromPortugues(categoria);
        List<Serie> series = repository.findByGenero(cat);
        if(series.isEmpty()){
            throw new RuntimeException("Não foi encontrado nenhum filme para a categoria: " + categoria);
        }
        return transformaSerieEmDto(series);
    }

    private List<SerieDTO> transformaSerieEmDto(List<Serie> series){
        return series.stream().map(s -> new SerieDTO(s)).collect(Collectors.toList());
    }

    private List<EpisodioDTO> transformaEpisodioEmDto(List<Episodio> episodios){
        return episodios.stream()
                .map(e -> new EpisodioDTO(e)).collect(Collectors.toList());
    }

}
