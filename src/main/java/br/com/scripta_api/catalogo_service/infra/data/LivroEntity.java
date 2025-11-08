package br.com.scripta_api.catalogo_service.infra.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "livros")
@Table(name = "livros")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    private String autor;
    private Integer anoPublicacao;
    @Column(nullable = false)
    private Integer quantidadeTotal;
    @Column(nullable = false)
    private Integer quantidadeDisponivel;
    @Column(nullable = false, unique = true)
    private String isbn;
}
