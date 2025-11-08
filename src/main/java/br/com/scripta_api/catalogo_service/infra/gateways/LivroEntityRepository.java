package br.com.scripta_api.catalogo_service.infra.gateways;

import br.com.scripta_api.catalogo_service.infra.data.LIvroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/*
TODO: Adicionar Optional<LivroEntity> findByIsbn(String isbn);.

TODO: Adicionar List<LivroEntity> findByTituloContainingOrAutorContaining(String titulo, String autor);.
 */
public interface LivroEntityRepository extends JpaRepository<LIvroEntity, Long> {
}
