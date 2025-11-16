package br.com.scripta_api.catalogo_service.application.domain;

public final class LivroBuilder {
    private final Livro livro;

    private LivroBuilder() {
        livro = new Livro();
    }

    public static LivroBuilder builder() {
        return new LivroBuilder();
    }

    public LivroBuilder id(Long id) {
        livro.setId(id);
        return this;
    }

    public LivroBuilder titulo(String titulo) {
        livro.setTitulo(titulo);
        return this;
    }

    public LivroBuilder anoPublicacao(Integer anoPublicacao) {
        livro.setAnoPublicacao(anoPublicacao);
        return this;
    }

    public LivroBuilder quantidadeDisponivel(Integer quantidadeDisponivel) {
        livro.setQuantidadeDisponivel(quantidadeDisponivel);
        return this;
    }

    public LivroBuilder quantidadeTotal(Integer quantidadeTotal) {
        livro.setQuantidadeTotal(quantidadeTotal);
        return this;
    }

    public LivroBuilder autor(String autor) {
        livro.setAutor(autor);
        return this;
    }

    public LivroBuilder isbn(String isbn) {
        livro.setIsbn(isbn);
        return this;
    }

    public Livro build() {
        return this.livro;
    }

}
