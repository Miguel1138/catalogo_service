package br.com.scripta_api.catalogo_service.repository;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.dtos.AtualizarLivroRequest;
import br.com.scripta_api.catalogo_service.exception.EstoqueInsuficienteException;
import br.com.scripta_api.catalogo_service.exception.LivroJaCadastradoException;
import br.com.scripta_api.catalogo_service.exception.LivroNaoEncontradoException;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import br.com.scripta_api.catalogo_service.infra.gateways.LivroEntityRepository;
import br.com.scripta_api.catalogo_service.repository.mapper.LivroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LivroRepository implements LivroService {

    private final LivroMapper mapper;
    private final LivroEntityRepository repository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Livro criarLivro(Livro livroDomain) {
        if (repository.findByIsbn(livroDomain.getIsbn()).isPresent()) {
            throw new LivroJaCadastradoException("Livro com ISBN " + livroDomain.getIsbn() + " já cadastrado.");
        }

        LivroEntity entity = mapper.toEntity(livroDomain);
        LivroEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> listarLivros() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        return mapper.toDomain(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscar(String termo) {
        Optional<List<LivroEntity>> entities = repository.findLivroByTituloOrAutor(termo, termo);

        return entities.orElse(Collections.emptyList())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Livro atualizarLivro(Long id, AtualizarLivroRequest request) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        // --- DEBUG: Ver o que está chegando ---
        System.out.println("DEBUG ATUALIZAR:");
        System.out.println("ID: " + id);
        System.out.println("ISBN Banco: [" + entity.getIsbn() + "]");
        System.out.println("ISBN Request: [" + request.getIsbn() + "]");
        // --------------------------------------

        // Normaliza as strings (remove espaços extras e garante comparação segura)
        String isbnBanco = entity.getIsbn() != null ? entity.getIsbn().trim() : "";
        String isbnRequest = request.getIsbn() != null ? request.getIsbn().trim() : "";

        // SÓ VERIFICA SE OS ISBNS FOREM REALMENTE DIFERENTES
        if (!isbnBanco.equalsIgnoreCase(isbnRequest)) {
            System.out.println("DEBUG: ISBNs são diferentes. Verificando duplicidade...");

            if (repository.findByIsbn(isbnRequest).isPresent()) {
                throw new LivroJaCadastradoException("O ISBN " + isbnRequest + " já pertence a outro livro.");
            }
        } else {
            System.out.println("DEBUG: ISBNs iguais. Pulando verificação.");
        }

        entity.setTitulo(request.getTitulo());
        entity.setAutor(request.getAutor());
        entity.setIsbn(isbnRequest); // Salva o ISBN limpo (sem espaços)
        entity.setAnoPublicacao(request.getAnoPublicacao());
        entity.setQuantidadeTotal(request.getQuantidadeTotal());

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Livro decrementarEstoque(Long id) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        if (entity.getQuantidadeDisponivel() <= 0) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o livro ID: " + id);
        }

        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() - 1);

        return mapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Livro incrementarEstoque(Long id) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() + 1);

        return mapper.toDomain(entity);
    }

    // --- CORREÇÃO AQUI ---
    // Renomeado de deletarLivro para deletar, para coincidir com a interface e o controller
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new LivroNaoEncontradoException("Livro não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}