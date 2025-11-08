package br.com.scripta_api.catalogo_service.infra.gateways;

import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroEntityRepository extends JpaRepository<LivroEntity, Long> {

    Optional<LivroEntity> findByIsbn(String isbn);

    @Query("select e " +
            "from livros e " +
            "where upper(e.titulo) like concat('%', upper(:titulo), '%')" +
            "or upper(e.autor) like concat('%', upper(:autor), '%')")
    Optional<List<LivroEntity>> findLivroByTituloOrAutor(String titulo, String autor);

}
