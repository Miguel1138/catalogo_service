package br.com.scripta_api.catalogo_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Importante adicionar este import
import lombok.Data;

@Data
public class AtualizarLivroRequest {

    @NotBlank(message = "O título não pode estar vazio")
    private String titulo;

    @NotBlank(message = "O autor não pode estar vazio")
    private String autor;

    @NotBlank(message = "O ISBN não pode estar vazio")
    private String isbn;

    @NotNull(message = "O ano de publicação é obrigatório") // Garante que não venha null
    @Min(value = 1000, message = "Ano de publicação inválido")
    private Integer anoPublicacao;

    @NotNull(message = "A quantidade total é obrigatória") // Garante que não venha null
    @Min(value = 0, message = "A quantidade total não pode ser negativa")
    private Integer quantidadeTotal;
}