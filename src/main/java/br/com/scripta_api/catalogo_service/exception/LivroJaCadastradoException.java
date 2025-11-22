package br.com.scripta_api.catalogo_service.exception;

public class LivroJaCadastradoException extends RuntimeException {
    public LivroJaCadastradoException(String message) {
        super(message);
    }
}
