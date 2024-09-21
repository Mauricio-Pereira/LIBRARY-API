package br.com.fiap.primeira_api.controller;

import br.com.fiap.primeira_api.dto.PrateleiraRequest;
import br.com.fiap.primeira_api.dto.PrateleiraResponse;
import br.com.fiap.primeira_api.model.Livro;
import br.com.fiap.primeira_api.model.Prateleira;
import br.com.fiap.primeira_api.repository.LivroRepository;
import br.com.fiap.primeira_api.repository.PrateleiraRepository;
import br.com.fiap.primeira_api.service.PrateleiraMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/prateleiras", produces = {"application/json"})
@Tag(name = "Prateleiras", description = "API de Prateleiras")
public class PrateleiraController {
    float aFloat = 100;
    @Autowired
    private PrateleiraRepository prateleiraRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private PrateleiraMapper prateleiraMapper;

    private Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").descending());


    @Operation(summary = "Cria uma prateleira")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prateleira criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Atributos inválidos", content = @Content(schema = @Schema()))    })
    @PostMapping
    public ResponseEntity<PrateleiraResponse> createPrateleira (@Valid @RequestBody PrateleiraRequest prateleiraRequest) {
        Prateleira prateleiraConvertida = prateleiraMapper.requestToPrateleira(prateleiraRequest);
        Prateleira prateleiraCriada = prateleiraRepository.save(prateleiraConvertida);

        PrateleiraResponse prateleiraResponse = prateleiraMapper.prateleiraToResponse(prateleiraCriada);
        return new ResponseEntity<>(prateleiraResponse, HttpStatus.CREATED);
    }


    @Operation(summary="Cria várias prateleiras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Várias prateleiras criadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos inválidos", content = @Content(schema = @Schema()))    })
    @PostMapping("/lista")
    public ResponseEntity<List<PrateleiraResponse>> createListaPrateleira(@Valid @RequestBody List<PrateleiraRequest> prateleiraRequest) {
        List<Prateleira> listaPrateleira = new ArrayList<>();
        List<PrateleiraResponse> listaPrateleiraResponse = new ArrayList<>();
        for (PrateleiraRequest prateleira : prateleiraRequest) {
            //Faz a conversão de PrateleiraRequest para Prateleira
            Prateleira prateleiraConvertida = prateleiraMapper.requestToPrateleira(prateleira);
            //Salva a Prateleira no banco de dados
            Prateleira prateleiraCriada = prateleiraRepository.save(prateleiraConvertida);
            //Adiciona a Prateleira criada na lista de Prateleiras
            listaPrateleira.add(prateleiraCriada);
            //Adiciona a Prateleira criada na lista de PrateleirasResponse para retornar
            listaPrateleiraResponse.add(prateleiraMapper.prateleiraToResponse(prateleiraCriada));
        }
        return new ResponseEntity<>(listaPrateleiraResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todas as prateleiras cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prateleiras encontradas"),
        @ApiResponse(responseCode = "404", description = "Prateleiras não encontradas", content = @Content(schema = @Schema()))    })
    @GetMapping
    public ResponseEntity<List<PrateleiraResponse>> readPrateleira() {
        Page<Prateleira> prateleiras = prateleiraRepository.findAll(pageable);
        if (prateleiras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PrateleiraResponse> prateleiraResponse = new ArrayList<>();
        for (Prateleira prateleira : prateleiras) {
            //Converte a Prateleira para PrateleiraResponse e adiciona na lista de PrateleiraResponse
            prateleiraResponse.add(prateleiraMapper.prateleiraToResponse(prateleira));
        }
        return new ResponseEntity<>(prateleiraResponse, HttpStatus.OK);
    }

    @Operation(summary = "Busca prateleira por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prateleira encontrada"),
        @ApiResponse(responseCode = "404", description = "Prateleira não encontrada", content = @Content(schema = @Schema()))    })
    @GetMapping("/{id}")
    public ResponseEntity<PrateleiraResponse> readPrateleiraById(@PathVariable Long id) {
        Optional<Prateleira> prateleira = prateleiraRepository.findById(id);
        if (prateleira == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PrateleiraResponse prateleiraResponse = prateleiraMapper.prateleiraToResponse(prateleira.get());
        return new ResponseEntity<>(prateleiraResponse, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza prateleira por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prateleira atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Prateleira não encontrada", content = @Content(schema = @Schema()))    })
    @PutMapping("/{id}")
    public ResponseEntity<PrateleiraResponse> updatePrateleira(@PathVariable Long id, @Valid @RequestBody PrateleiraRequest prateleiraRequest) {
        Optional<Prateleira> prateleira = prateleiraRepository.findById(id);
        if (prateleira.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Prateleira prateleiraConvertida = prateleiraMapper.requestToPrateleira(prateleiraRequest);
        prateleiraConvertida.setId(id);
        Prateleira prateleiraAtualizada = prateleiraRepository.save(prateleiraConvertida);
        PrateleiraResponse prateleiraResponse = prateleiraMapper.prateleiraToResponse(prateleiraAtualizada);
        return new ResponseEntity<>(prateleiraResponse, HttpStatus.OK);
    }

    @Operation(summary = "Deleta prateleira por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prateleira deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Prateleira não encontrada", content = @Content(schema = @Schema()))    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PrateleiraResponse> deletePrateleira(@PathVariable Long id) {
        Optional<Prateleira> prateleira = prateleiraRepository.findById(id);
        if (prateleira.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        prateleiraRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //Método para adicionar um livro a uma prateleira
    @Operation(summary = "Adiciona um livro existente a uma prateleira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Prateleira ou Livro não encontrado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema()))
    })
    @PutMapping("/{prateleiraId}/livros/{livroId}")
    public ResponseEntity<PrateleiraResponse> addExistingLivroToPrateleira(@PathVariable Long prateleiraId, @PathVariable Long livroId) {
        Optional<Prateleira> prateleiraOptional = prateleiraRepository.findById(prateleiraId);
        if (prateleiraOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Livro> livroOptional = livroRepository.findById(livroId);
        if (livroOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Livro livro = livroOptional.get();
        Prateleira prateleira = prateleiraOptional.get();
        prateleira.getLivros().add(livro);
        prateleiraRepository.save(prateleira);
        PrateleiraResponse prateleiraResponse = prateleiraMapper.prateleiraToResponse(prateleira);
        return new ResponseEntity<>(prateleiraResponse, HttpStatus.OK);


    }
}
