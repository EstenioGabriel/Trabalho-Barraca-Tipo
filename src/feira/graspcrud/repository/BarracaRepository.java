package feira.graspcrud.repository;

import feira.graspcrud.domain.Barraca;
import java.util.List;

public interface BarracaRepository {
    void salvar(Barraca barraca);
    List<Barraca> listarTodas();
    Barraca buscarPorId(int id);
    void atualizar(Barraca barraca);
    void remover(int id);
    boolean existeNome(String nome, Integer ignorarId);
    boolean existePorTipo(int tipoId);
}