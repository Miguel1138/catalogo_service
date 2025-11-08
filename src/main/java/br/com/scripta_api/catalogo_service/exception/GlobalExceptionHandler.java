package br.com.scripta_api.catalogo_service.exception;

/*
TODO: Anotar a classe com @RestControllerAdvice (para que ela "ouça" todos os controllers).

TODO: (Opcional) Criar um DTO simples, como ErroResponse(String mensagem,
 int status), para padronizar a resposta.

TODO: Criar um método handleLivroNaoEncontrado.

TODO: Anotar com @ExceptionHandler(LivroNaoEncontradoException.class).

TODO: Anotar com @ResponseStatus(HttpStatus.NOT_FOUND).

TODO: Fazer o método retornar um ResponseEntity<ErroResponse>
 (ou similar) com a mensagem da exceção e o status 404.

TODO: Criar um método handleLivroJaCadastrado.

TODO: Anotar com @ExceptionHandler(LivroJaCadastradoException.class).

TODO: Anotar com @ResponseStatus(HttpStatus.CONFLICT).

TODO: Fazer o método retornar o ErroResponse com a mensagem e o status 409 (Conflito).

TODO: Criar um método handleEstoqueInsuficiente.

TODO: Anotar com @ExceptionHandler(EstoqueInsuficienteException.class).

TODO: Anotar com @ResponseStatus(HttpStatus.BAD_REQUEST).

TODO: Fazer o método retornar o ErroResponse com a mensagem e o status 400 (Requisição Ruim).

TODO: Criar um método handleValidationExceptions (para DTOs inválidos).

TODO: Anotar com @ExceptionHandler(MethodArgumentNotValidException.class).

TODO: Anotar com @ResponseStatus(HttpStatus.BAD_REQUEST).

TODO: Implementar a lógica para extrair os erros de validação
 (ex: ex.getBindingResult().getFieldErrors())
 e retorná-los em um formato claro (ex: Map<String, String>).
 */
public class GlobalExceptionHandler {
}
