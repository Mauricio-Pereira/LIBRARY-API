package br.com.fiap.primeira_api.dto;

import br.com.fiap.primeira_api.model.Categoria;
import jakarta.validation.constraints.*;

public record LivroRequest(
        @NotBlank(message = "O título do livro é obrigatório")
        String titulo,
        @NotBlank(message = "O autor do livro é obrigatório")
        String autor,
        @NotNull(message = "A categoria do livro é obrigatória")
        Categoria categoria,
        @Size(min=3, max=50, message = "O nome da editora deve ter entre 3 e 50 caracteres")
        @NotBlank(message = "A editora do livro é obrigatória")
        String editora,
        @Pattern(regexp = "^\\d{10}(\\d{3})?$", message = "O ISBN deve ter 10 ou 13 dígitos")
        @NotNull(message = "O ISBN do livro é obrigatório")
        String isbn
) {
}