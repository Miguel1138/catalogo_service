package br.com.scripta_api.catalogo_service.controller;

import br.com.scripta_api.catalogo_service.api.gateways.GoogleBooksApiService;
import br.com.scripta_api.catalogo_service.application.domain.LivroBuilder;
import br.com.scripta_api.catalogo_service.application.gateways.service.LivroService;
import br.com.scripta_api.catalogo_service.dtos.AtualizarLivroRequest; // <--- O IMPORT QUE FALTAVA
import br.com.scripta_api.catalogo_service.dtos.CriarLivroRequest;
import br.com.scripta_api.catalogo_service.dtos.LivroResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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

    // --- AQUI ESTÁ A CORREÇÃO DO MÉTODO DE ATUALIZAR ---
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizarLivro(@PathVariable Long id, @RequestBody @Valid AtualizarLivroRequest request) {
        return ResponseEntity.ok(
                LivroResponse.fromDomain(livroService.atualizarLivro(id, request))
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        livroService.deletar(id);
    }
}