package br.com.fiap.library_api.repository;

import br.com.fiap.library_api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
