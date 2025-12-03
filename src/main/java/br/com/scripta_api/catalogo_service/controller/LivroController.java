package br.com.scripta_api.catalogo_service.controller;

// --- OS IMPORTS QUE ESTAVAM FALTANDO ---
import br.com.scripta_api.catalogo_service.dtos.AtualizarLivroRequest;
import br.com.scripta_api.catalogo_service.dtos.CriarLivroRequest;
import br.com.scripta_api.catalogo_service.dtos.LivroResponse;
// ---------------------------------------

import br.com.scripta_api.catalogo_service.infra.data.LivroEntity;
import br.com.scripta_api.catalogo_service.repository.LivroRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroRepository livroRepository;

    @PostMapping
    public ResponseEntity<LivroResponse> criarLivro(@RequestBody CriarLivroRequest request) {
        LivroEntity entity = new LivroEntity();
        entity.setTitulo(request.getTitulo());
        entity.setAutor(request.getAutor());
        entity.setIsbn(request.getIsbn());
        entity.setAnoPublicacao(request.getAnoPublicacao());
        entity.setQuantidadeTotal(request.getQuantidadeTotal() != null ? request.getQuantidadeTotal() : 1);
        entity.setQuantidadeDisponivel(request.getQuantidadeDisponivel() != null ? request.getQuantidadeDisponivel() : 1);

        LivroEntity salvo = livroRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(LivroResponse.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarLivros() {
        List<LivroResponse> response = livroRepository.findAll().stream()
                .map(LivroResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
        LivroEntity livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return ResponseEntity.ok(LivroResponse.fromEntity(livro));
    }

    @PostMapping("/buscar")
    public ResponseEntity<List<LivroResponse>> buscarLivrosPorPalavra(@RequestBody String palavra) {
        String termo = palavra.replace("\"", "");
        List<LivroResponse> response = livroRepository.buscarPorTituloOuAutor(termo.toLowerCase()).stream()
                .map(LivroResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizarLivro(@PathVariable Long id, @RequestBody AtualizarLivroRequest request) {
        LivroEntity entity = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (request.getTitulo() != null) entity.setTitulo(request.getTitulo());
        if (request.getAutor() != null) entity.setAutor(request.getAutor());
        if (request.getIsbn() != null) entity.setIsbn(request.getIsbn());
        if (request.getAnoPublicacao() != null) entity.setAnoPublicacao(request.getAnoPublicacao());
        if (request.getQuantidadeTotal() != null) entity.setQuantidadeTotal(request.getQuantidadeTotal());
        if (request.getQuantidadeDisponivel() != null) entity.setQuantidadeDisponivel(request.getQuantidadeDisponivel());

        LivroEntity atualizado = livroRepository.save(entity);
        return ResponseEntity.ok(LivroResponse.fromEntity(atualizado));
    }

    @PutMapping("/{id}/estoque/incrementar")
    public ResponseEntity<LivroResponse> incrementarEstoque(@PathVariable Long id) {
        LivroEntity entity = livroRepository.findById(id).orElseThrow();
        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() + 1);
        entity.setQuantidadeTotal(entity.getQuantidadeTotal() + 1);
        return ResponseEntity.ok(LivroResponse.fromEntity(livroRepository.save(entity)));
    }

    @PutMapping("/{id}/estoque/decrementar")
    public ResponseEntity<LivroResponse> decrementarEstoque(@PathVariable Long id) {
        LivroEntity entity = livroRepository.findById(id).orElseThrow();
        if (entity.getQuantidadeDisponivel() > 0) {
            entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() - 1);
            entity.setQuantidadeTotal(entity.getQuantidadeTotal() - 1);
        }
        return ResponseEntity.ok(LivroResponse.fromEntity(livroRepository.save(entity)));
    }

    @PostMapping("/importar/{isbn}")
    public ResponseEntity<LivroResponse> importaLivro(@PathVariable String isbn) {
        LivroEntity dummy = new LivroEntity();
        dummy.setTitulo("Livro Importado (" + isbn + ")");
        dummy.setAutor("Autor Desconhecido");
        dummy.setIsbn(isbn);
        dummy.setAnoPublicacao(2023);
        dummy.setQuantidadeTotal(1);
        dummy.setQuantidadeDisponivel(1);
        LivroEntity salvo = livroRepository.save(dummy);
        return ResponseEntity.ok(LivroResponse.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        livroRepository.deleteById(id);
    }
}