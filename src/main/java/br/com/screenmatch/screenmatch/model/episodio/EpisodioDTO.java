package br.com.screenmatch.screenmatch.model.episodio;

public record EpisodioDTO(String titulo, Integer temporada, Integer numeroEpisodio) {

    public EpisodioDTO(Episodio episodio){
        this(episodio.getTitulo(), episodio.getTemporada(), episodio.getNumeroEpisodio());
    }
}
