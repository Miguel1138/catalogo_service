package br.com.scripta_api.catalogo_service.application.gateways.service;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.dtos.AtualizarLivroRequest;

import java.util.List;

public interface LivroService {
    Livro criarLivro(Livro domain);

    Livro buscarPorId(Long id);

    Livro atualizarLivro(Long id, AtualizarLivroRequest reques);

    List<Livro> listarLivros();

    List<Livro> buscar(String nomeOrAutor);

    void deletarLivro(Long id);

    Livro decrementarEstoque(Long id);

    Livro incrementarEstoque(Long id);
}
