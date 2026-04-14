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
                
                cache.add(new TipoBarraca(id, nome));
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
        return json.substring(inicio, fim).replace("\"", "").trim();
    }

    @Override
    public void salvar(TipoBarraca tipo) {
        cache.add(tipo);
        salvarNoArquivo();
    }

    @Override
    public List<TipoBarraca> listarTodos() {
        return new ArrayList<>(cache);
    }

    @Override
    public TipoBarraca buscarPorId(int id) {
        return cache.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remover(int id) {
        cache.removeIf(t -> t.getId() == id);
        salvarNoArquivo();
    }

    private void salvarNoArquivo() {
        List<String> linhas = new ArrayList<>();
        for (TipoBarraca t : cache) {
            linhas.add(String.format("{\"id\": %d, \"nome\": \"%s\"}", t.getId(), t.getNome()));
        }
        JsonMini.salvar(CAMINHO, linhas);
    }
}