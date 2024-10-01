package br.com.fiap.library_api.dto;

import br.com.fiap.library_api.model.UserRole;

public record RegisterDTO(String login, String senha, UserRole role) {}
