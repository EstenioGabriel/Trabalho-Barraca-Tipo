package com.barraca.backend.repository;

import com.barraca.backend.domain.Barraca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarracaRepository extends JpaRepository<Barraca, Long> {
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
    boolean existsByTipoBarracaId(Long tipoBarracaId);
}
