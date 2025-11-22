package br.com.scripta_api.catalogo_service.exception;

public class EstoqueInsuficienteException  extends RuntimeException{
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
