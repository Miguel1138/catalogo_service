package br.com.scripta_api.catalogo_service.api.gateways;

/*
TODO: Anotar com @Service, @RequiredArgsConstructor.

TODO: Injetar private final ApiLivroClient apiLivroClient;.

TODO: Injetar private final LivroService livroService; (a interface do application).

TODO: Implementar public Livro importarLivroPorIsbn(String isbn):

TODO: Chamar apiLivroClient.buscarPorIsbn(isbn).

TODO: Extrair os dados da resposta (título, autor, etc.).

TODO:
  Criar um Livro (modelo de domínio) usando new Livro.builder()... com os dados importados.

TODO: Chamar livroService.criarLivro(livroDomain) e retornar o resultado.
 */
public interface GoogleBooksApiService {
}
