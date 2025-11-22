package br.com.scripta_api.catalogo_service.api.gateways;

import br.com.scripta_api.catalogo_service.api.ApiLivroClient;
import br.com.scripta_api.catalogo_service.api.dto.GoogleBookItem;
import br.com.scripta_api.catalogo_service.api.dto.GoogleBooksResponse;
import br.com.scripta_api.catalogo_service.api.dto.VolumeInfo;
import br.com.scripta_api.catalogo_service.application.domain.Livro;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.exception.LivroNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleBooksApiService {
    private final ApiLivroClient apiLivroClient;
    private final LivroService livroService; // A "Porta" de entrada do domínio

    public Livro importarLivroPorIsbn(String isbn) {
        // 1. Buscar na API Externa
        GoogleBooksResponse response = apiLivroClient.buscarPorIsbn(isbn);

        // 2. Validar se encontrou
        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            throw new LivroNaoEncontradoException("Livro não encontrado na base externa para o ISBN: " + isbn);
        }

        // 3. Mapear DTO Externo -> Domínio Interno
        GoogleBookItem item = response.getItems().get(0);
        Livro livroParaSalvar = converterParaDominio(item.getVolumeInfo(), isbn);

        // 4. Salvar usando o Serviço de Domínio (que já valida duplicidade)
        return livroService.criarLivro(livroParaSalvar);
    }

    private Livro converterParaDominio(VolumeInfo info, String isbnBusca) {
        // Tenta achar o ISBN_13 na resposta, se não usa o que foi buscado
        String isbnFinal = isbnBusca;
        if (info.getIndustryIdentifiers() != null) {
            isbnFinal = info.getIndustryIdentifiers().stream()
                    .filter(id -> "ISBN_13".equals(id.getType()))
                    .map(VolumeInfo.IndustryIdentifier::getIdentifier)
                    .findFirst()
                    .orElse(isbnBusca);
        }

        // Trata lista de autores
        String autor = "Desconhecido";
        if (info.getAuthors() != null && !info.getAuthors().isEmpty()) {
            autor = String.join(", ", info.getAuthors());
        }

        // Trata ano de publicação (geralmente vem "YYYY-MM-DD" ou só "YYYY")
        int ano = 0;
        if (info.getPublishedDate() != null && info.getPublishedDate().length() >= 4) {
            try {
                ano = Integer.parseInt(info.getPublishedDate().substring(0, 4));
            } catch (NumberFormatException ignored) {
            }
        }

        return LivroBuilder.builder()
                .titulo(info.getTitle() != null ? info.getTitle() : "Sem Título")
                .autor(autor)
                .isbn(isbnFinal)
                .anoPublicacao(ano)
                .quantidadeTotal(1) // Regra: Importação inicia com 1 exemplar por padrão
                .quantidadeDisponivel(1)
                .build();
    }
}
