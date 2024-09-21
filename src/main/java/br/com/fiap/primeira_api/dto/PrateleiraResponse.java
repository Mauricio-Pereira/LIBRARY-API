package br.com.fiap.primeira_api.dto;

import java.util.Set;

public record PrateleiraResponse(
        Long id,
        String nome,
        String descricao,
        Set<LivroResponse> livros
) {
}
