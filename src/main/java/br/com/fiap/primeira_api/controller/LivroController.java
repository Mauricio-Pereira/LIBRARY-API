package br.com.fiap.primeira_api.controller;

import br.com.fiap.primeira_api.dto.LivroRequest;
import br.com.fiap.primeira_api.dto.LivroResponse;
import br.com.fiap.primeira_api.model.Livro;
import br.com.fiap.primeira_api.repository.LivroRepository;
import br.com.fiap.primeira_api.service.LivroMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page; // Certifique-se de que esta importação está presente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/livros", produces = {"application/json"})
@Tag(name = "Livros", description = "API de Livros")
public class LivroController {
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private LivroMapper livroMapper;
    private Pageable pageable = PageRequest.of(0, 2, Sort.by("titulo").descending());

    @Operation(summary = "Cria um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos inválidos", content = @Content(schema = @Schema()))    })
    @PostMapping
    public ResponseEntity<LivroResponse> createLivro (@Valid @RequestBody LivroRequest livroRequest) {
        Livro livroConvertido = livroMapper.requestToLivro(livroRequest);
        Livro livroCriado = livroRepository.save(livroConvertido);



        LivroResponse livroResponse = livroMapper.livroToResponse(livroCriado);
//        livroResponse.setLink(
//                linkTo(
//                        methodOn(LivroController.class)
//                                .readLivroById(livroCriado.getId())
//                ).withSelfRel()
//        );
        return new ResponseEntity<>(livroResponse, HttpStatus.CREATED);
    }



    @Operation(summary = "Cria vários livros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livros criados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos inválidos", content = @Content(schema = @Schema()))    })
    @PostMapping("/lista")
    public ResponseEntity<List<LivroResponse>> createLivros (@Valid @RequestBody List<LivroRequest> listaLivrosRequest) {
        List<Livro> listaLivros = new ArrayList<>();
        for (LivroRequest livroRequest : listaLivrosRequest){
            Livro livroConvertido = livroMapper.requestToLivro(livroRequest);
            Livro livroCriado = livroRepository.save(livroConvertido);
            listaLivros.add(livroConvertido);
        }
        List<LivroResponse> listaLivrosResponse = new ArrayList<>();
        for (Livro livro : listaLivros){
            LivroResponse livroResponse = livroMapper.livroToResponse(livro);
//            livroResponse.setLink(
//                    linkTo(
//                            methodOn(LivroController.class)
//                                    .readLivroById(livro.getId())
//                    ).withSelfRel()
//            );
            listaLivrosResponse.add(livroResponse);
        }
        return new ResponseEntity<>(listaLivrosResponse, HttpStatus.CREATED);
    }



    @Operation(summary = "Lista todos os livros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livros listados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum livro encontrado", content = @Content(schema = @Schema()))    })
    @GetMapping("/page/{page}")
    public ResponseEntity<List<LivroResponse>> readAllLivros(@PathVariable int page){
        Pageable pageable = PageRequest.of(page, 2, Sort.by("titulo").descending());
        Page<Livro> listaLivros = livroRepository.findAll(pageable);
        if (listaLivros.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<LivroResponse> listaLivrosResponse = new ArrayList<>();
        for (Livro livro : listaLivros){
            LivroResponse livroResponse = livroMapper.livroToResponse(livro);
            listaLivrosResponse.add(livroResponse);
        }
        return new ResponseEntity<>(listaLivrosResponse, HttpStatus.OK);

    }


    @Operation(summary = "Lista livro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro com ID não encontrado", content = @Content(schema = @Schema()))    })
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> readLivroById(@PathVariable Long id){
        Optional<Livro> livroSalvo = livroRepository.findById(id);
        if (livroSalvo.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LivroResponse livroResponse = livroMapper.livroToResponse(livroSalvo.get());
//        livroResponse.setLink(
//                linkTo(
//                        methodOn(LivroController.class)
//                                .readLivroById(id)
//                ).withRel("Lista de Livros")
//        );
            return new ResponseEntity<>(livroResponse, HttpStatus.OK);

    }


    @Operation(summary = "Atualiza um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Livro com ID não encontrado ou atributos inválidos", content = @Content(schema = @Schema()))    })
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> updateLivro(@Valid @PathVariable Long id, @RequestBody LivroRequest livroRequest){
        Optional<Livro> livroSalvo = livroRepository.findById(id);
        if (livroSalvo.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Livro livroConvertido = livroMapper.requestToLivro(livroRequest);
        livroConvertido.setId(id);
        Livro livroAtualizado = livroRepository.save(livroConvertido);

        LivroResponse livroResponse = livroMapper.livroToResponse(livroAtualizado);
//        livroResponse.setLink(
//                linkTo(
//                        methodOn(LivroController.class)
//                                .readLivroById(livroAtualizado.getId())
//                ).withSelfRel()
//        );
        return new ResponseEntity<>(livroResponse, HttpStatus.OK);
    }



    @Operation(summary = "Deleta um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Livro com ID não encontrado", content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id){
        Optional<Livro> livroSalvo = livroRepository.findById(id);
        if (livroSalvo.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        livroRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
