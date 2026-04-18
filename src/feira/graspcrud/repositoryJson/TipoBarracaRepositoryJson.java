package feira.graspcrud.repositoryJson;

import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.repository.TipoBarracaRepository;
import feira.graspcrud.util.JsonMini;
import java.util.ArrayList;
import java.util.List;

public class TipoBarracaRepositoryJson implements TipoBarracaRepository {

    private final String CAMINHO = "data/tipos-barraca.json";
    private List<TipoBarraca> cache = new ArrayList<>();

    public TipoBarracaRepositoryJson() {
        carregarDados();
    }

    private void carregarDados() {
        List<String> objetosRaw = JsonMini.ler(CAMINHO);
        for (String json : objetosRaw) {
            try {
                if (json.trim().isEmpty() || !json.contains("{")) continue;
                int id = Integer.parseInt(extrair(json, "id"));
                String nome = extrair(json, "nome");
                String descricao = extrair(json, "descricao");
                cache.add(new TipoBarraca(id, nome, descricao));
            } catch (Exception e) {
                continue;
            }
        }
    }

    private String extrair(String json, String chave) {
        String alvo = "\"" + chave + "\":";
        int inicio = json.indexOf(alvo);
        if (inicio == -1) return "";
        inicio += alvo.length();
        int fim = json.indexOf(",", inicio);
        if (fim == -1) fim = json.indexOf("}", inicio);
        if (fim == -1) return "";
        return json.substring(inicio, fim).replace("\"", "").trim();
    }

    @Override
    public void salvar(TipoBarraca tipo) {
        cache.removeIf(t -> t.getId() == tipo.getId());
        cache.add(tipo);
        salvarNoArquivo();
    }

    @Override
    public List<TipoBarraca> listarTodos() {
        return new ArrayList<>(cache);
    }

    @Override
    public TipoBarraca buscarPorId(int id) {
        for (TipoBarraca t : cache) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    @Override
    public void remover(int id) {
        cache.removeIf(t -> t.getId() == id);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(TipoBarraca tipo) {
        cache.removeIf(t -> t.getId() == tipo.getId());
        cache.add(tipo);
        salvarNoArquivo();
    }

    @Override
    public boolean existeNome(String nome, Integer ignorarId) {
        for (TipoBarraca t : cache) {
            if (t.getNome().equalsIgnoreCase(nome)) {
                if (ignorarId == null || t.getId() != ignorarId) return true;
            }
        }
        return false;
    }

    private void salvarNoArquivo() {
        List<String> linhas = new ArrayList<>();
        for (TipoBarraca t : cache) {
            linhas.add(String.format(
                "{\"id\": %d, \"nome\": \"%s\", \"descricao\": \"%s\"}",
                t.getId(), t.getNome(), t.getDescricao()
            ));
        }
        JsonMini.salvar(CAMINHO, linhas);
    }
}