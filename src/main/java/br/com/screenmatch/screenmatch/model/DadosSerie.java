package br.com.screenmatch.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie(
        @JsonAlias("Title")
        String titulo,
        @JsonAlias("imdbRating")
        String avaliacao,
        @JsonAlias("totalSeasons")
        Integer totalTemporadas
) {
}
