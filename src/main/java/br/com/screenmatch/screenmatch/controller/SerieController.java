package br.com.screenmatch.screenmatch.controller;

import br.com.screenmatch.screenmatch.dto.SerieDTO;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import br.com.screenmatch.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController()
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
        var series = repository.findAll();
        List<SerieDTO> serie = series
                .stream()
                .map(s -> new SerieDTO(s))
                .collect(Collectors.toList());
        return serie;
    }

}
