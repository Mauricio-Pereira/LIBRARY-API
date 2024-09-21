package br.com.fiap.primeira_api.service;
import br.com.fiap.primeira_api.controller.LivroController;
import br.com.fiap.primeira_api.dto.LivroRequest;
import br.com.fiap.primeira_api.dto.LivroResponse;
import br.com.fiap.primeira_api.model.Livro;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LivroMapper {
    public Livro requestToLivro(LivroRequest livroRequest) {
        Livro livro = new Livro();
        livro.setTitulo(livroRequest.titulo());
        livro.setAutor(livroRequest.autor());
        livro.setCategoria(livroRequest.categoria());
        livro.setEditora(livroRequest.editora());
        livro.setIsbn(livroRequest.isbn());
        return livro;

    }
    public LivroResponse livroToResponse(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getCategoria().toString(),
                livro.getEditora(),
                livro.getIsbn(),
                linkTo(
                        methodOn(LivroController.class)
                                .readLivroById(livro.getId())
                ).withSelfRel()
        );

    }
}