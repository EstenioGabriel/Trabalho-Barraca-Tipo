package com.barraca.backend.repository;

import com.barraca.backend.domain.Barraca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarracaRepository extends JpaRepository<Barraca, Long> {
    Optional<Barraca> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByTipoBarracaId(Long tipoBarracaId);
}
