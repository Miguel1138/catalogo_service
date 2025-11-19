package br.com.scripta_api.catalogo_service.repository;


import org.springframework.transaction.annotation.Transactional;
import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.exception.EstoqueInsuficienteException;
import br.com.scripta_api.catalogo_service.exception.LivroJaCadastradoException;
import br.com.scripta_api.catalogo_service.exception.LivroNaoEncontradoException;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import br.com.scripta_api.catalogo_service.infra.gateways.LivroEntityRepository;
import br.com.scripta_api.catalogo_service.repository.mapper.LivroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LivroRepository implements LivroService {
    private final LivroMapper mapper;
    private final LivroEntityRepository repository;


    @Override
    @Transactional
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
    public List<Livro> listarTodos() {
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
    @Transactional
    public Livro atualizarLivro(Long id, Livro livroDomain) {
        if (!repository.existsById(id)) {
            throw new LivroNaoEncontradoException("Livro não encontrado com ID: " + id);
        }

        repository.findByIsbn(livroDomain.getIsbn()).ifPresent(entity -> {
            if (!entity.getId().equals(id)) {
                throw new LivroJaCadastradoException("ISBN " + livroDomain.getIsbn() + " já pertence a outro livro.");
            }
        });

        LivroEntity entityToUpdate = mapper.toEntity(livroDomain);
        entityToUpdate.setId(id);

        LivroEntity updated = repository.save(entityToUpdate);

        return mapper.toDomain(updated);
    }

    @Override
    @Transactional
    public Livro decrementarEstoque(Long id) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        if (entity.getQuantidadeDisponivel() <= 0) {

            throw new EstoqueInsuficienteException("Estoque insuficiente para o livro ID: " + id);
        }

        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() - 1);
        LivroEntity updated = repository.save(entity);

        return mapper.toDomain(updated);

    }

    @Override
    @Transactional
    public Livro incrementarEstoque(Long id) {
        LivroEntity entity = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ID: " + id));

        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() + 1);
        LivroEntity updated = repository.save(entity);

        return mapper.toDomain(updated);
    }

    @Override
    @Transactional
    public void deletarLivro(Long id) {
        if (!repository.existsById(id)) {
            throw new LivroNaoEncontradoException("Livro não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}

