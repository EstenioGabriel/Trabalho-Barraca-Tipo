package com.barraca.backend.service;

import com.barraca.backend.domain.Barraca;
import com.barraca.backend.domain.TipoBarraca;
import com.barraca.backend.dto.BarracaRequestDTO;
import com.barraca.backend.dto.BarracaResponseDTO;
import com.barraca.backend.exception.RegraNegocioException;
import com.barraca.backend.repository.BarracaRepository;
import com.barraca.backend.repository.TipoBarracaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BarracaService {

    private final BarracaRepository barracaRepository;
    private final TipoBarracaRepository tipoBarracaRepository;

    public BarracaService(BarracaRepository barracaRepository, TipoBarracaRepository tipoBarracaRepository) {
        this.barracaRepository = barracaRepository;
        this.tipoBarracaRepository = tipoBarracaRepository;
    }

    public List<BarracaResponseDTO> listarTodas() {
        return barracaRepository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public BarracaResponseDTO buscarPorId(Long id) {
        return converterParaResponse(encontrarPorIdOuLancarErro(id));
    }

    @Transactional
    public BarracaResponseDTO criar(BarracaRequestDTO dto) {
        validarNome(dto.getNome());
        if (barracaRepository.existsByNome(dto.getNome())) {
            throw new RegraNegocioException("Já existe uma Barraca com o nome: " + dto.getNome());
        }
        TipoBarraca tipo = encontrarTipoOuLancarErro(dto.getTipoBarracaId());
        Barraca barraca = new Barraca();
        preencherBarraca(barraca, dto, tipo);
        return converterParaResponse(barracaRepository.save(barraca));
    }

    @Transactional
    public BarracaResponseDTO atualizar(Long id, BarracaRequestDTO dto) {
        Barraca barraca = encontrarPorIdOuLancarErro(id);
        validarNome(dto.getNome());
        Optional<Barraca> comMesmoNome = barracaRepository.findByNome(dto.getNome());
        if (comMesmoNome.isPresent() && !comMesmoNome.get().getId().equals(id)) {
            throw new RegraNegocioException("Já existe outra Barraca com o nome: " + dto.getNome());
        }
        TipoBarraca tipo = encontrarTipoOuLancarErro(dto.getTipoBarracaId());
        preencherBarraca(barraca, dto, tipo);
        return converterParaResponse(barracaRepository.save(barraca));
    }

    @Transactional
    public void remover(Long id) {
        encontrarPorIdOuLancarErro(id);
        barracaRepository.deleteById(id);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraNegocioException("O nome da Barraca é obrigatório e deve ter ao menos 3 caracteres.");
        }
    }

    private Barraca encontrarPorIdOuLancarErro(Long id) {
        return barracaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Barraca não encontrada com ID: " + id));
    }

    private TipoBarraca encontrarTipoOuLancarErro(Long tipoId) {
        if (tipoId == null) {
            throw new RegraNegocioException("O tipo de barraca é obrigatório.");
        }
        return tipoBarracaRepository.findById(tipoId)
                .orElseThrow(() -> new RegraNegocioException("Tipo de Barraca não encontrado com ID: " + tipoId));
    }

    private void preencherBarraca(Barraca barraca, BarracaRequestDTO dto, TipoBarraca tipo) {
        barraca.setNome(dto.getNome().trim());
        barraca.setDescricao(dto.getDescricao());
        barraca.setLocalizacao(dto.getLocalizacao());
        barraca.setAtivo(dto.isAtivo());
        barraca.setTipoBarraca(tipo);
    }

    private BarracaResponseDTO converterParaResponse(Barraca barraca) {
        return new BarracaResponseDTO(
                barraca.getId(),
                barraca.getNome(),
                barraca.getDescricao(),
                barraca.getLocalizacao(),
                barraca.isAtivo(),
                barraca.getTipoBarraca().getId(),
                barraca.getTipoBarraca().getNome()
        );
    }
}
