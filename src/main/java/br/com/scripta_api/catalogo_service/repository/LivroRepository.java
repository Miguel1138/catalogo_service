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
        if (!repository.existsById(id)) {
            throw new LivroNaoEncontradoException("Livro não encontrado com ID: " + id);
        }

        repository.findByIsbn(request.getIsbn()).ifPresent(entity -> {
            if (!entity.getId().equals(id)) {
                throw new LivroJaCadastradoException("ISBN " + request.getIsbn() + " já pertence a outro livro.");
            }
        });

        LivroEntity updateEntity = repository.findById(id).orElseThrow();

        updateEntity.setId(id);
        updateEntity.setTitulo(request.getTitulo());
        updateEntity.setAutor(request.getAutor());
        updateEntity.setIsbn(request.getIsbn());
        updateEntity.setAnoPublicacao(request.getAnoPublicacao());
        updateEntity.setQuantidadeTotal(request.getQuantidadeTotal());

        return mapper.toDomain(updateEntity);
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

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deletarLivro(Long id) {
        if (!repository.existsById(id)) {
            throw new LivroNaoEncontradoException("Livro não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}

