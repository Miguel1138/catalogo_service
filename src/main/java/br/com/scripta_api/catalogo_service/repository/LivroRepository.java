package br.com.scripta_api.catalogo_service.repository;

/*
TODO: Anotar com @Repository, @RequiredArgsConstructor.

TODO: Fazer a classe implements LivroService (a interface do application).
TODO: Injetar private final LivroMapper mapper;.
TODO: Injetar private final LivroEntityRepository repository; (a interface do infra).

TODO: Implementar @Transactional public Livro criarLivro(Livro livroDomain):

TODO: Validar se repository.findByIsbn(livroDomain.getIsbn()) já existe. Se sim, lançar LivroJaCadastradoException.

TODO: LivroEntity entity = mapper.toEntity(livroDomain);

TODO: LivroEntity savedEntity = repository.save(entity);

TODO: return mapper.toDomain(savedEntity);

TODO: Implementar os outros métodos da interface LivroService (listar, buscar, etc.) seguindo o padrão (chamar repository, mapear resultado com mapper).

TODO: Implementar @Transactional public Livro decrementarEstoque(Long id):

TODO: LivroEntity entity = repository.findById(id).orElseThrow(LivroNaoEncontradoException::new);

TODO: Validar se entity.getQuantidadeDisponivel() > 0. Se não, lançar EstoqueInsuficienteException.

TODO: entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() - 1);

TODO: return mapper.toDomain(repository.save(entity));

TODO: Implementar incrementarEstoque (lógica similar).
 */
public class LivroRepository {
}
