package br.com.scripta_api.catalogo_service.controller;

/*

TODO: Implementar
 POST /importar/{isbn} (protegido):
 Chama integracaoService.importarLivroPorIsbn() e retorna LivroResponse.fromDomain().
 */

import br.com.scripta_api.catalogo_service.api.gateways.GoogleBooksApiService;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.dtos.CriarLivroRequest;
import br.com.scripta_api.catalogo_service.dtos.LivroResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;
    private final GoogleBooksApiService apiService;

    @PostMapping
    public LivroResponse criarLivro(@RequestBody CriarLivroRequest request) {
        return LivroResponse.fromDomain(livroService.criarLivro(
                        LivroBuilder.builder()
                                .titulo(request.getTitulo())
                                .autor(request.getAutor())
                                .isbn(request.getIsbn())
                                .anoPublicacao(request.getAnoPublicacao())
                                .quantidadeTotal(request.getQuantidadeTotal())
                                .quantidadeDisponivel(request.getQuantidadeDisponivel())
                                .build()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarLivros() {
        return ResponseEntity.ok(livroService.listarLivros().stream()
                .map(LivroResponse::fromDomain)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(LivroResponse.fromDomain(livroService.buscarPorId(id)));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LivroResponse>> buscarLivrosPorPalavra(@RequestBody String palavra) {
        return ResponseEntity.ok(
                livroService.buscar(palavra).stream()
                        .map(LivroResponse::fromDomain)
                        .toList()
        );
    }

    @PutMapping("/{id}/estoque/incrementar")
    public ResponseEntity<LivroResponse> incrementarEstoque(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                LivroResponse.fromDomain(livroService.incrementarEstoque(id))
        );
    }

    @PutMapping("/{id}/estoque/decrementar")
    public ResponseEntity<LivroResponse> decrementarEstoque(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                LivroResponse.fromDomain(livroService.decrementarEstoque(id))
        );
    }

    @PostMapping("/importar/{isbn}")
    public ResponseEntity<LivroResponse> importaLivro(@PathVariable String isbn) {
        return ResponseEntity.ok(LivroResponse.fromDomain(apiService.importarLivroPorIsbn(isbn)));
    }

}
