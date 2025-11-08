package br.com.scripta_api.catalogo_service.controller;

/*
TODO: Anotar com @RestController,
 @RequestMapping("/livros"),
 @RequiredArgsConstructor.

TODO: Injetar private final LivroService livroService; (a interface).
TODO: Injetar private final IntegracaoService integracaoService;.

TODO: Implementar POST / (protegido):
  Recebe CriarLivroRequest, converte para Livro (domain) usando o builder,
  chama livroService.criarLivro() e retorna LivroResponse.fromDomain().

TODO: Implementar
 GET /,
 GET /{id},
 GET /buscar (públicos).

TODO: Implementar
 POST /importar/{isbn} (protegido):
 Chama integracaoService.importarLivroPorIsbn() e retorna LivroResponse.fromDomain().

TODO: Implementar
 PUT /{id}/estoque/decrementar e
 .../incrementar (protegidos).
 */
public class LivroController {
}
