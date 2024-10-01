package br.com.fiap.library_api.service;

import br.com.fiap.library_api.dto.PrateleiraRequest;
import br.com.fiap.library_api.dto.PrateleiraResponse;
import br.com.fiap.library_api.model.Prateleira;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PrateleiraMapper {
    public Prateleira requestToPrateleira(PrateleiraRequest prateleiraRequest) {
        Prateleira prateleira = new Prateleira();
        prateleira.setNome(prateleiraRequest.nome());
        prateleira.setDescricao(prateleiraRequest.descricao());
        return prateleira;

    }

    public PrateleiraResponse prateleiraToResponse(Prateleira prateleira) {
        return new PrateleiraResponse(
                prateleira.getId(),
                prateleira.getNome(),
                prateleira.getDescricao(),
                prateleira.getLivros().stream().map(livro -> new LivroMapper().livroToResponse(livro)).collect(
                        Collectors.toSet())
        );

    }
}
