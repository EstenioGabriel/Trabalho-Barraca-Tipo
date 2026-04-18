package feira.graspcrud.service;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.dto.BarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.repository.TipoBarracaRepository;
import java.util.List;

public class BarracaService {

    private final BarracaRepository barracaRepository;
    private final TipoBarracaRepository tipoRepository;

    public BarracaService(BarracaRepository barracaRepository, TipoBarracaRepository tipoRepository) {
        this.barracaRepository = barracaRepository;
        this.tipoRepository = tipoRepository;
    }

    public Barraca criar(BarracaRequest request) {
        if (barracaRepository.existeNome(request.nome, null)) {
            throw new RegraNegocioException("Ja existe uma Barraca com este nome.");
        }
        if (tipoRepository.buscarPorId(request.tipoId) == null) {
            throw new RegraNegocioException("TipoBarraca nao encontrado com ID: " + request.tipoId);
        }
        int novoId = gerarProximoId();
        Barraca barraca = new Barraca(novoId, request.nome, request.descricao, request.ativo, request.tipoId);
        barraca.validar();
        barracaRepository.salvar(barraca);
        return barraca;
    }

    public Barraca atualizar(int id, BarracaRequest request) {
        buscarPorId(id);
        if (barracaRepository.existeNome(request.nome, id)) {
            throw new RegraNegocioException("Ja existe outra Barraca com este nome.");
        }
        if (tipoRepository.buscarPorId(request.tipoId) == null) {
            throw new RegraNegocioException("TipoBarraca nao encontrado com ID: " + request.tipoId);
        }
        Barraca barraca = new Barraca(id, request.nome, request.descricao, request.ativo, request.tipoId);
        barraca.validar();
        barracaRepository.atualizar(barraca);
        return barraca;
    }

    public Barraca buscarPorId(int id) {
        Barraca barraca = barracaRepository.buscarPorId(id);
        if (barraca == null) {
            throw new RegraNegocioException("Barraca nao encontrada com ID: " + id);
        }
        return barraca;
    }

    public List<Barraca> listarTodas() {
        return barracaRepository.listarTodas();
    }

    public void remover(int id) {
        buscarPorId(id);
        barracaRepository.remover(id);
    }

    private int gerarProximoId() {
        List<Barraca> todas = barracaRepository.listarTodas();
        int max = 0;
        for (Barraca b : todas) {
            if (b.getId() > max) max = b.getId();
        }
        return max + 1;
    }
}
