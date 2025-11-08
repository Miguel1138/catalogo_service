package br.com.scripta_api.catalogo_service.application.domain;

//TODO: Implementar lógica de negócio nos setters ou no build() (ex: quantidadeDisponivel não pode ser negativa).

public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidadeTotal;
    private Integer quantidadeDsiponivel;

    protected Livro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public Integer getQuantidadeDsiponivel() {
        return quantidadeDsiponivel;
    }

    public void setQuantidadeDsiponivel(Integer quantidadeDsiponivel) {
        if(this.quantidadeDsiponivel == 0) {

        }
        this.quantidadeDsiponivel = quantidadeDsiponivel;
    }

}
