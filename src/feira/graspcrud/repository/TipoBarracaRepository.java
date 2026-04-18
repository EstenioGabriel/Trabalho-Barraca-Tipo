package feira.graspcrud.repository;

import feira.graspcrud.domain.TipoBarraca;
import java.util.List;

public interface TipoBarracaRepository {
    void salvar(TipoBarraca tipo);
    List<TipoBarraca> listarTodos();
    TipoBarraca buscarPorId(int id);
    void remover(int id);
    void atualizar(TipoBarraca tipo);
    boolean existeNome(String nome, Integer ignorarId);
}