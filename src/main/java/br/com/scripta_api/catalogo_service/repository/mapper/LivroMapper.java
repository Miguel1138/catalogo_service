package br.com.scripta_api.catalogo_service.repository.mapper;


import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import org.springframework.stereotype.Component;


@Component
public class LivroMapper {

    public Livro toDomain(LivroEntity savedEntity) {
        if (savedEntity == null) {
            return null;
        }

        return LivroBuilder.builder()
                .id(savedEntity.getId())
                .titulo(savedEntity.getTitulo())
                .autor(savedEntity.getAutor())
                .isbn(savedEntity.getIsbn())
                .anoPublicacao(savedEntity.getAnoPublicacao())
                .quantidadeTotal(savedEntity.getQuantidadeTotal())
                .quantidadeDisponivel(savedEntity.getQuantidadeDisponivel())
                .build();
    }


    public LivroEntity toEntity(Livro domain) {
        if (domain == null) {
            return null;
        }

        return LivroEntity.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .autor(domain.getAutor())
                .isbn(domain.getIsbn())
                .anoPublicacao(domain.getAnoPublicacao())
                .quantidadeTotal(domain.getQuantidadeTotal())
                .quantidadeDisponivel(domain.getQuantidadeDsiponivel())
                .build();
    }

}

