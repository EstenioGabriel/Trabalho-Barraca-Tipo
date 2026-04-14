package feira.graspcrud.repository;

import feira.graspcrud.domain.TipoBarraca;
import java.util.List;

public interface TipoBarracaRepository{
    void salvar(TipoBarraca tipo);
    List<TipoBarraca> listarTodos();
    TipoBarraca buscarPorId(int id);
    void remover (int id);
}