package br.com.screenmatch.screenmatch.model.serie;

public enum Categoria {
    COMEDIA("Comedy"),
    ACAO("Action"),
    ROMANCE("Romance"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }
    public static Categoria fromString(String texto){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaOmdb.equalsIgnoreCase(texto)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nnehuma categoria foi encontrada com a string fornecida: " + texto);
    }

}
