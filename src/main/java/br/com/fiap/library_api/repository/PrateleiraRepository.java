package br.com.fiap.library_api.repository;

import br.com.fiap.library_api.model.Prateleira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrateleiraRepository extends JpaRepository<Prateleira, Long> {
}
