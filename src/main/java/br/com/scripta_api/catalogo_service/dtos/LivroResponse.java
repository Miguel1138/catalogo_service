package br.com.scripta_api.catalogo_service.dtos;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LivroResponse {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidadeTotal;
    private Integer quantidadeDsiponivel;

    public static LivroResponse fromDomain(Livro domain) {
        return LivroResponse.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .autor(domain.getAutor())
                .isbn(domain.getIsbn())
                .anoPublicacao(domain.getAnoPublicacao())
                .quantidadeDsiponivel(domain.getQuantidadeDsiponivel())
                .quantidadeTotal(domain.getQuantidadeTotal())
                .build();
    }
}
