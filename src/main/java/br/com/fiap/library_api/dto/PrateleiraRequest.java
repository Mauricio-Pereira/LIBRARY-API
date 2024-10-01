package br.com.fiap.library_api.dto;
import jakarta.validation.constraints.NotBlank;

public record PrateleiraRequest(
        @NotBlank(message = "O nome da prateleira é obrigatório")
        String nome,
        @NotBlank(message = "A descrição da prateleira é obrigatória")
        String descricao

) {
}
