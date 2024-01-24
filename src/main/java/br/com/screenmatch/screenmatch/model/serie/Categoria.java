package br.com.screenmatch.screenmatch.model.serie;

public enum Categoria {
    COMEDIA("Comedy", "Comédia"),
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String categoriaOmdb;

    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }


    public static Categoria fromString(String texto){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaOmdb.equalsIgnoreCase(texto)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria foi encontrada com a string fornecida: " + texto);
    }

    public static Categoria fromPortugues(String texto){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaPortugues.equalsIgnoreCase(texto)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria foi encontrada com a string fornecida: " + texto);
    }

}
