package br.com.screenmatch.screenmatch.controller;

import br.com.screenmatch.screenmatch.dto.SerieDTO;
import br.com.screenmatch.screenmatch.model.episodio.Episodio;
import br.com.screenmatch.screenmatch.model.episodio.EpisodioDTO;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import br.com.screenmatch.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public ResponseEntity<List<SerieDTO>> obterSeries(){
        return ResponseEntity.ok(serieService.obterTodasAsSeries());
    }

    @GetMapping("/top5")
    public ResponseEntity<List<SerieDTO>> top5series(){
        return ResponseEntity.ok(serieService.obterTop5Series());
    }

    @GetMapping("/lancamentos")
    public ResponseEntity<List<SerieDTO>> obterLancamentos(){
        return ResponseEntity.ok(serieService.obterLancamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> pegaSeriePeloId(@PathVariable Long id) {
        var serie = serieService.pegaSeriePeloId(id);
        return ResponseEntity.ok(serie);
    }

    @GetMapping("/{id}/temporadas/todas")
    public ResponseEntity<List<EpisodioDTO>> todosEpisodiosPorSerie(@PathVariable Long id){
        return ResponseEntity.ok(serieService.todosOsEpisodiosPorSerie(id));
    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public ResponseEntity<List<EpisodioDTO>> episodiosPorTemporada(@PathVariable Long id, @PathVariable Integer numeroTemporada){
        return ResponseEntity.ok(serieService.obterEpisodiosPorTemporada(id, numeroTemporada));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<SerieDTO>> seriesPorCategoria(@PathVariable String categoria){
        return ResponseEntity.ok(serieService.obterSeriesPorCategoria(categoria));
    }
}
