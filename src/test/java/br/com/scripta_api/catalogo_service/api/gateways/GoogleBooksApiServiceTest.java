package br.com.scripta_api.catalogo_service.api.gateways;


import br.com.scripta_api.catalogo_service.api.ApiLivroClient;
import br.com.scripta_api.catalogo_service.api.dto.GoogleBookItem;
import br.com.scripta_api.catalogo_service.api.dto.GoogleBooksResponse;
import br.com.scripta_api.catalogo_service.api.dto.VolumeInfo;
import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.exception.LivroNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleBooksApiServiceTest {

    @Mock
    private ApiLivroClient apiLivroClient;

    @Mock
    private LivroService livroService;

    @InjectMocks
    private GoogleBooksApiService googleBooksApiService;

    @Test
    @DisplayName("Importar: Deve mapear corretamente e priorizar ISBN_13")
    void importarLivro_SucessoMapeamento() {
        // Arrange
        String isbnBusca = "9781234567890";
        GoogleBooksResponse mockResponse = criarResponseMock(isbnBusca);

        when(apiLivroClient.buscarPorIsbn(isbnBusca)).thenReturn(mockResponse);
        when(livroService.criarLivro(any(Livro.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Livro result = googleBooksApiService.importarLivroPorIsbn(isbnBusca);

        // Assert (Regras de Mapeamento)
        assertNotNull(result);
        assertEquals("Livro Mock", result.getTitulo());
        assertEquals("Autor Mock", result.getAutor());
        assertEquals(2023, result.getAnoPublicacao());
        assertEquals(isbnBusca, result.getIsbn()); // Garante que pegou o ISBN_13
        assertEquals(1, result.getQuantidadeTotal()); // Regra de estoque inicial = 1

        verify(livroService).criarLivro(any(Livro.class));
    }

    @Test
    @DisplayName("Importar: Deve lançar exceção se API retornar vazio")
    void importarLivro_NaoEncontrado() {
        when(apiLivroClient.buscarPorIsbn("111")).thenReturn(new GoogleBooksResponse()); // Sem itens

        assertThrows(LivroNaoEncontradoException.class, () ->
                googleBooksApiService.importarLivroPorIsbn("111")
        );

        verify(livroService, never()).criarLivro(any());
    }

    private GoogleBooksResponse criarResponseMock(String isbn13) {
        VolumeInfo.IndustryIdentifier id10 = new VolumeInfo.IndustryIdentifier();
        id10.setType("ISBN_10");
        id10.setIdentifier("1234567890");

        VolumeInfo.IndustryIdentifier id13 = new VolumeInfo.IndustryIdentifier();
        id13.setType("ISBN_13");
        id13.setIdentifier(isbn13); // O que esperamos

        VolumeInfo info = new VolumeInfo();
        info.setTitle("Livro Mock");
        info.setAuthors(List.of("Autor Mock"));
        info.setPublishedDate("2023-05-20"); // Teste de parsing de data
        info.setIndustryIdentifiers(List.of(id10, id13));

        GoogleBookItem item = new GoogleBookItem();
        item.setVolumeInfo(info);

        GoogleBooksResponse response = new GoogleBooksResponse();
        response.setItems(List.of(item));
        return response;
    }
}
