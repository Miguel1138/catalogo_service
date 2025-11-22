package br.com.scripta_api.catalogo_service.repository;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.exception.EstoqueInsuficienteException;
import br.com.scripta_api.catalogo_service.exception.LivroJaCadastradoException;
import br.com.scripta_api.catalogo_service.exception.LivroNaoEncontradoException;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import br.com.scripta_api.catalogo_service.infra.gateways.LivroEntityRepository;
import br.com.scripta_api.catalogo_service.repository.mapper.LivroMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroRepositoryTest {

    @Mock
    private LivroEntityRepository entityRepository;

    @Mock
    private LivroMapper mapper;

    @InjectMocks
    private LivroRepository livroRepository;

    @Test
    @DisplayName("Criar Livro: Deve salvar com sucesso se ISBN for único")
    void criarLivro_Sucesso() {
        // Arrange
        Livro domain = createValidLivroDomain();
        LivroEntity entity = new LivroEntity();

        when(entityRepository.findByIsbn(domain.getIsbn())).thenReturn(Optional.empty());
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(entityRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        Livro result = livroRepository.criarLivro(domain);

        // Assert
        assertNotNull(result);
        verify(entityRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Criar Livro: Deve lançar exceção se ISBN já existe")
    void criarLivro_ErroDuplicidade() {
        // Arrange
        Livro domain = createValidLivroDomain();
        when(entityRepository.findByIsbn(domain.getIsbn())).thenReturn(Optional.of(new LivroEntity()));

        // Act & Assert
        assertThrows(LivroJaCadastradoException.class, () -> livroRepository.criarLivro(domain));
        verify(entityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Decrementar Estoque: Deve reduzir quantidade se houver disponibilidade")
    void decrementarEstoque_Sucesso() {
        // Arrange
        Long id = 1L;
        LivroEntity entity = new LivroEntity();
        entity.setQuantidadeDisponivel(5);
        Livro domain = createValidLivroDomain();

        when(entityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        livroRepository.decrementarEstoque(id);

        // Assert
        assertEquals(4, entity.getQuantidadeDisponivel()); // Verifica alteração no objeto
        // Nota: O método original não chama explicitamente o .save(), confia no @Transactional.
        // Se quiser testar persistência, o método precisaria retornar o save.
    }

    @Test
    @DisplayName("Decrementar Estoque: Deve falhar se estoque for zero ou negativo")
    void decrementarEstoque_ErroInsuficiente() {
        // Arrange
        Long id = 1L;
        LivroEntity entity = new LivroEntity();
        entity.setQuantidadeDisponivel(0); // Sem estoque

        when(entityRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(EstoqueInsuficienteException.class, () -> livroRepository.decrementarEstoque(id));
    }

    @Test
    @DisplayName("Incrementar Estoque: Deve aumentar quantidade")
    void incrementarEstoque_Sucesso() {
        // Arrange
        Long id = 1L;
        LivroEntity entity = new LivroEntity();
        entity.setQuantidadeDisponivel(5);
        Livro domain = createValidLivroDomain();

        when(entityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        livroRepository.incrementarEstoque(id);

        // Assert
        assertEquals(6, entity.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Buscar por ID: Deve lançar exceção se não encontrado")
    void buscarPorId_NaoEncontrado() {
        when(entityRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LivroNaoEncontradoException.class, () -> livroRepository.buscarPorId(1L));
    }

    private Livro createValidLivroDomain() {
        return LivroBuilder.builder()
                .id(1L)
                .titulo("Teste")
                .isbn("123456789")
                .quantidadeTotal(10)
                .quantidadeDisponivel(10)
                .build();
    }
}