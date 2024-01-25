package br.com.screenmatch.screenmatch.dto;

import br.com.screenmatch.screenmatch.model.serie.Categoria;
import br.com.screenmatch.screenmatch.model.serie.Serie;

public record SerieDTO(Long id, String titulo, Double avaliacao, Integer totalTemporadas, Categoria genero, String atores, String poster, String sinopse) {


    public SerieDTO(Serie serie) {
        this(serie.getId(), serie.getTitulo(), serie.getAvaliacao(), serie.getTotalTemporadas(), serie.getGenero(), serie.getAtores(), serie.getPoster(), serie.getSinopse());
    }
}
