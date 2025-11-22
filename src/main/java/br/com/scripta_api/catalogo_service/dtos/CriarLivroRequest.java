package br.com.scripta_api.catalogo_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriarLivroRequest {

    @NotBlank(message = "O título não pode estar vazio")
    private String titulo;

    @NotBlank(message = "O autor não pode estar vazio")
    private String autor;

    @NotBlank(message = "O ISBN não pode estar vazio")
    private String isbn;

    @Min(value = 1000, message = "Ano de publicação inválido")
    private Integer anoPublicacao;

    @Min(value = 0, message = "A quantidade total não pode ser negativa")
    private Integer quantidadeTotal;

    @NotBlank(message = "O quantidade total não pode estar vazio")
    @Min(value = 0, message = "quantidade total não pode ser zero")
    private Integer quantidadeDsiponivel;
}
