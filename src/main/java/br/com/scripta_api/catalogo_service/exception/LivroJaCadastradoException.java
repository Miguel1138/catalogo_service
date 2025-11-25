package br.com.scripta_api.catalogo_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Define o status 409 para o Front-end
public class LivroJaCadastradoException extends RuntimeException {
    public LivroJaCadastradoException(String message) {
        super(message);
    }
}