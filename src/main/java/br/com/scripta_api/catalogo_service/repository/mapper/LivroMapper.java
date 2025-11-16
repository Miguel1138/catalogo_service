package br.com.scripta_api.catalogo_service.repository.mapper;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import org.springframework.stereotype.Component;


@Component
public class LivroMapper {

    public Livro toDomain(LivroEntity savedEntity) {
        if (savedEntity == null) {
            return null;
        }

        Livro domain = new Livro();
        domain.setId(savedEntity.getId());
        domain.setTitulo(savedEntity.getTitulo());
        domain.setAutor(savedEntity.getAutor());
        domain.setIsbn(savedEntity.getIsbn());
        domain.setAnoPublicacao(savedEntity.getAnoPublicacao());
        domain.setQuantidadeTotal(savedEntity.getQuantidadeTotal());
        domain.setQuantidadeDisponivel(savedEntity.getQuantidadeDisponivel());

        return domain;
    }

    public LivroEntity toEntity(Livro domain) {
        if (domain == null) {
            return null;
        }

        LivroEntity entity = new LivroEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setAutor(domain.getAutor());
        entity.setIsbn(domain.getIsbn());
        entity.setAnoPublicacao(domain.getAnoPublicacao());
        entity.setQuantidadeTotal(domain.getQuantidadeTotal());
        entity.setQuantidadeDisponivel(domain.getQuantidadeDsiponivel());

        return  entity;
    }

    public void updateEntityFromDomain(Livro domain, LivroEntity entity) {
        if (domain == null || entity == null) {
            return;
        }




        entity.setTitulo(domain.getTitulo());
        entity.setAutor(domain.getAutor());
        entity.setIsbn(domain.getIsbn());
        entity.setAnoPublicacao(domain.getAnoPublicacao());
        entity.setQuantidadeTotal(domain.getQuantidadeTotal());


        entity.setQuantidadeDisponivel(domain.getQuantidadeDsiponivel());
    }
}
