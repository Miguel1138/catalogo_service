package br.com.scripta_api.catalogo_service.repository.mapper;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import br.com.scripta_api.catalogo_service.repository.mapper.LivroMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LivroMapperTest {

    private LivroMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LivroMapper();
    }

    @Test
    @DisplayName("Deve converter Entity para Domain")
    void toDomain() {
        LivroEntity entity = LivroEntity.builder()
                .id(1L)
                .titulo("Domain Driven Design")
                .autor("Eric Evans")
                .isbn("123456")
                .anoPublicacao(2003)
                .quantidadeTotal(5)
                .quantidadeDisponivel(5)
                .build();

        Livro domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getTitulo(), domain.getTitulo());
        assertEquals(entity.getIsbn(), domain.getIsbn());
    }

    @Test
    @DisplayName("Deve converter Domain para Entity")
    void toEntity() {
        // Precisamos garantir valores válidos pro Builder do domínio passar
        Livro domain = br.com.scripta_api.catalogo_service.application.domain.LivroBuilder.builder()
                .id(1L)
                .titulo("Domain Driven Design")
                .autor("Eric Evans")
                .isbn("123456")
                .anoPublicacao(2003)
                .quantidadeTotal(5)
                .quantidadeDisponivel(5)
                .build();

        LivroEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getTitulo(), entity.getTitulo());
        assertEquals(domain.getQuantidadeDsiponivel(), entity.getQuantidadeDisponivel());
    }
}