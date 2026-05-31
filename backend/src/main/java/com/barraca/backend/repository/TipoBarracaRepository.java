package com.barraca.backend.repository;

import com.barraca.backend.domain.TipoBarraca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoBarracaRepository extends JpaRepository<TipoBarraca, Long> {
    Optional<TipoBarraca> findByNome(String nome);
    boolean existsByNome(String nome);
}
