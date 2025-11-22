package br.com.scripta_api.catalogo_service.api;

/*
TODO: Anotar com @Component, @RequiredArgsConstructor.

TODO: Injetar o WebClient.

TODO: Implementar public
  ApiExternaResponseDto buscarPorIsbn(String isbn) (usando webClient.get()...).
 */

import br.com.scripta_api.catalogo_service.api.dto.GoogleBooksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ApiLivroClient {
    private final WebClient webClient;

    public GoogleBooksResponse buscarPorIsbn(String isbn) {
        // A query padrão do Google Books para ISBN é "isbn:NUMERO"
        String query = "isbn:" + isbn;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", query)
                        .queryParam("maxResults", 1) // Queremos apenas o match exato
                        .build())
                .retrieve()
                .bodyToMono(GoogleBooksResponse.class)
                .block(); // Chamada síncrona para simplificar o fluxo transacional do serviço
    }
}
