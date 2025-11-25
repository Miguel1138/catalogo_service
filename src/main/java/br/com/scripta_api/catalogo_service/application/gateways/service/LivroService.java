package br.com.scripta_api.catalogo_service.application.gateways.service;

import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.dtos.AtualizarLivroRequest;

import java.util.List;

public interface LivroService {

    Livro criarLivro(Livro domain);

    Livro buscarPorId(Long id);

    // Corrigi o nome da variável 'reques' para 'request' para ficar mais padronizado
    Livro atualizarLivro(Long id, AtualizarLivroRequest request);

    List<Livro> listarLivros();

    List<Livro> buscar(String nomeOrAutor);

    // --- DEIXE APENAS ESTE ---
    void deletar(Long id);

    Livro decrementarEstoque(Long id);

    Livro incrementarEstoque(Long id);
}