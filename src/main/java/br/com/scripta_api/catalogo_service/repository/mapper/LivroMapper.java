package br.com.scripta_api.catalogo_service.repository.mapper;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import org.springframework.stereotype.Component;


@Component
public class LivroMapper {

    public static Livro toDomain(LivroEntity entity) {
        return LivroBuilder.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .quantidadeTotal(entity.getQuantidadeTotal())
                .quantidadeDisponivel(entity.getQuantidadeDisponivel())
                .isbn(entity.getIsbn())
                .anoPublicacao(entity.getAnoPublicacao())
                .autor(entity.getAutor())
                .build();
    }

    public static LivroEntity toEntity(Livro domain) {
        return LivroEntity.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .anoPublicacao(domain.getAnoPublicacao())
                .autor(domain.getAutor())
                .isbn(domain.getIsbn())
                .quantidadeDisponivel(domain.getQuantidadeDsiponivel())
                .quantidadeTotal(domain.getQuantidadeTotal())
                .build();
    }

}
