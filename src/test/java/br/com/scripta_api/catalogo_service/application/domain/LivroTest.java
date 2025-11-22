package br.com.scripta_api.catalogo_service.application.domain;

import br.com.scripta_api.catalogo_service.exception.EstoqueInsuficienteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    @Test
    @DisplayName("Deve criar um livro válido através do Builder")
    void deveCriarLivroValido() {
        Livro livro = LivroBuilder.builder()
                .id(1L)
                .titulo("Clean Code")
                .autor("Robert C. Martin")
                .isbn("9780132350884")
                .anoPublicacao(2008)
                .quantidadeTotal(10)
                .quantidadeDisponivel(10)
                .build();

        assertNotNull(livro);
        assertEquals("Clean Code", livro.getTitulo());
        assertEquals(10, livro.getQuantidadeTotal());
    }

    @Test
    @DisplayName("Deve lançar exceção se tentar criar livro com estoque negativo ou zero")
    void deveFalharAoCriarComEstoqueInvalido() {
        assertThrows(EstoqueInsuficienteException.class, () -> {
            LivroBuilder.builder()
                    .titulo("Livro Teste")
                    .quantidadeTotal(5)
                    .quantidadeDisponivel(0) // Regra do Builder atual impede <= 0
                    .build();
        });
    }

    @Test
    @DisplayName("Deve lançar exceção se quantidade total for negativa")
    void deveFalharAoCriarComTotalNegativo() {
        assertThrows(EstoqueInsuficienteException.class, () -> {
            LivroBuilder.builder()
                    .titulo("Livro Teste")
                    .quantidadeTotal(-1)
                    .quantidadeDisponivel(5)
                    .build();
        });
    }
}