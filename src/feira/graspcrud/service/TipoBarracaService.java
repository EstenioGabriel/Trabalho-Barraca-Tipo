package feira.graspcrud.service;

import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.dto.TipoBarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.repository.TipoBarracaRepository;
import java.util.List;

public class TipoBarracaService {

    private final TipoBarracaRepository tipoRepository;
    private final BarracaRepository barracaRepository;

    public TipoBarracaService(TipoBarracaRepository tipoRepository, BarracaRepository barracaRepository) {
        this.tipoRepository = tipoRepository;
        this.barracaRepository = barracaRepository;
    }

    public TipoBarraca criar(TipoBarracaRequest request) {
        if (tipoRepository.existeNome(request.nome, null)) {
            throw new RegraNegocioException("Ja existe um TipoBarraca com este nome.");
        }
        int novoId = gerarProximoId();
        TipoBarraca tipo = new TipoBarraca(novoId, request.nome, request.descricao);
        tipo.validar();
        tipoRepository.salvar(tipo);
        return tipo;
    }

    public List<TipoBarraca> listarTodos() {
        return tipoRepository.listarTodos();
    }

    public TipoBarraca buscarPorId(int id) {
        TipoBarraca tipo = tipoRepository.buscarPorId(id);
        if (tipo == null) {
            throw new RegraNegocioException("TipoBarraca nao encontrado com ID: " + id);
        }
        return tipo;
    }

    public void remover(int id) {
        if (barracaRepository.existePorTipo(id)) {
            throw new RegraNegocioException("Nao e permitido remover TipoBarraca em uso por barracas.");
        }
        buscarPorId(id);
        tipoRepository.remover(id);
    }

    private int gerarProximoId() {
        List<TipoBarraca> todos = tipoRepository.listarTodos();
        int max = 0;
        for (TipoBarraca t : todos) {
            if (t.getId() > max) max = t.getId();
        }
        return max + 1;
    }
}
