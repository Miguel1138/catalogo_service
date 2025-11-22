package br.com.scripta_api.catalogo_service.controller;

import br.com.scripta_api.catalogo_service.api.gateways.GoogleBooksApiService;
import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.config.SecurityConfig;
import br.com.scripta_api.catalogo_service.dtos.CriarLivroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
@Import(SecurityConfig.class) // Importa config de segurança para testar rotas
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LivroService livroService;

    @MockitoBean
    private GoogleBooksApiService googleBooksApiService;

    // Mocks necessários para o SecurityConfig funcionar no teste
    @MockitoBean
    private org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;
    @MockitoBean
    private org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter;

    @Test
    @DisplayName("POST /livros: Deve retornar 200 OK quando BIBLIOTECARIO cria")
    @WithMockUser(roles = "BIBLIOTECARIO")
    void criarLivro_Sucesso() throws Exception {
        CriarLivroRequest request = CriarLivroRequest.builder()
                .titulo("Teste")
                .autor("Autor")
                .isbn("123")
                .anoPublicacao(2020)
                .quantidadeTotal(5)
                .quantidadeDisponivel(5)
                .build();

        Livro livroDomain = LivroBuilder.builder()
                .id(1L).titulo("Teste").autor("Autor").isbn("123")
                .quantidadeTotal(5).quantidadeDisponivel(5)
                .build();

        when(livroService.criarLivro(any())).thenReturn(livroDomain);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /livros: Deve retornar 403 Forbidden quando ALUNO tenta criar")
    @WithMockUser(roles = "ALUNO")
    void criarLivro_AcessoNegado() throws Exception {
        CriarLivroRequest request = CriarLivroRequest.builder().titulo("Teste").build();

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /livros/importar/{isbn}: Deve chamar serviço de integração")
    @WithMockUser(roles = "BIBLIOTECARIO")
    void importarLivro_Sucesso() throws Exception {
        String isbn = "123456";
        Livro livroDomain = LivroBuilder.builder()
                .id(1L).titulo("Importado").isbn(isbn)
                .quantidadeTotal(1).quantidadeDisponivel(1)
                .build();

        when(googleBooksApiService.importarLivroPorIsbn(isbn)).thenReturn(livroDomain);

        mockMvc.perform(post("/livros/importar/{isbn}", isbn))
                .andExpect(status().isOk());
    }
}