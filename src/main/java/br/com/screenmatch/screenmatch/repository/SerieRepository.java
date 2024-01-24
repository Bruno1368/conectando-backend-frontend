package br.com.screenmatch.screenmatch.repository;

import br.com.screenmatch.screenmatch.model.serie.Categoria;
import br.com.screenmatch.screenmatch.model.serie.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nome, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie>findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer temporadas, Double nota);

    @Query("SELECT a FROM Serie a WHERE a.totalTemporadas <= :temporada AND a.avaliacao >= :nota")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer temporada, Double nota);
}
