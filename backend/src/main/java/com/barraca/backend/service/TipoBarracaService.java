package com.barraca.backend.service;

import com.barraca.backend.domain.TipoBarraca;
import com.barraca.backend.dto.TipoBarracaDTO;
import com.barraca.backend.exception.RegraNegocioException;
import com.barraca.backend.repository.TipoBarracaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoBarracaService {

    private final TipoBarracaRepository repository;

    public TipoBarracaService(TipoBarracaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TipoBarracaDTO criar(TipoBarracaDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do tipo de barraca não pode ser vazio.");
        }
        if (repository.existsByNome(dto.getNome())) {
            throw new RegraNegocioException("Já existe um Tipo de Barraca com este nome.");
        }
        TipoBarraca tipoBarraca = new TipoBarraca();
        tipoBarraca.setNome(dto.getNome());
        tipoBarraca.setDescricao(dto.getDescricao());
        tipoBarraca = repository.save(tipoBarraca);
        return converterParaDTO(tipoBarraca);
    }

    public TipoBarracaDTO buscarPorId(Long id) {
        TipoBarraca tipo = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Tipo de Barraca não encontrado com ID: " + id));
        return converterParaDTO(tipo);
    }

    public List<TipoBarracaDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RegraNegocioException("Tipo de Barraca não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private TipoBarracaDTO converterParaDTO(TipoBarraca tipo) {
        return new TipoBarracaDTO(tipo.getId(), tipo.getNome(), tipo.getDescricao());
    }
}
