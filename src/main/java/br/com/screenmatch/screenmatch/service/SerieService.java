package br.com.screenmatch.screenmatch.service;

import br.com.screenmatch.screenmatch.dto.SerieDTO;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import br.com.screenmatch.screenmatch.repository.SerieRepository;
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
        return transformaSerieEmDto(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
    }

    public SerieDTO pegaSeriePeloId(Long id){
        var serie = repository.findById(id);
        if(serie.isPresent()){
            return new SerieDTO(serie.get());
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    private List<SerieDTO> transformaSerieEmDto(List<Serie> series){
        return series.stream().map(s -> new SerieDTO(s)).collect(Collectors.toList());
    }

}
