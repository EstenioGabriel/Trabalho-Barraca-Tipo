package feira.graspcrud.repositoryJson;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.util.JsonMini;
import java.util.ArrayList;
import java.util.List;

public class BarracaRepositoryJson implements BarracaRepository {

    private final String CAMINHO = "data/barracas.json";
    private List<Barraca> cache = new ArrayList<>();

    public BarracaRepositoryJson() {
        carregarDados();
    }

    private void carregarDados() {
        List<String> objetosRaw = JsonMini.ler(CAMINHO);
        for (String json : objetosRaw) {
            try {
                if (json.trim().isEmpty() || !json.contains("{")) continue;
                int id = Integer.parseInt(extrairValor(json, "id"));
                String nome = extrairValor(json, "nome");
                String descricao = extrairValor(json, "descricao");
                int tipoId = Integer.parseInt(extrairValor(json, "tipoId"));
                boolean ativo = extrairValor(json, "ativo").equalsIgnoreCase("true");
                cache.add(new Barraca(id, nome, descricao, ativo, tipoId));
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public void salvar(Barraca barraca) {
        cache.removeIf(b -> b.getId() == barraca.getId());
        cache.add(barraca);
        salvarNoArquivo();
    }

    @Override
    public List<Barraca> listarTodas() {
        return new ArrayList<>(cache);
    }

    @Override
    public Barraca buscarPorId(int id) {
        for (Barraca b : cache) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    @Override
    public void atualizar(Barraca barraca) {
        cache.removeIf(b -> b.getId() == barraca.getId());
        cache.add(barraca);
        salvarNoArquivo();
    }

    @Override
    public void remover(int id) {
        cache.removeIf(b -> b.getId() == id);
        salvarNoArquivo();
    }

    @Override
    public boolean existeNome(String nome, Integer ignorarId) {
        for (Barraca b : cache) {
            if (b.getNome().equalsIgnoreCase(nome)) {
                if (ignorarId == null || b.getId() != ignorarId) return true;
            }
        }
        return false;
    }

    @Override
    public boolean existePorTipo(int tipoId) {
        for (Barraca b : cache) {
            if (b.getTipoId() == tipoId) return true;
        }
        return false;
    }

    private void salvarNoArquivo() {
        List<String> linhas = new ArrayList<>();
        for (Barraca b : cache) {
            linhas.add(String.format(
                "{\"id\": %d, \"nome\": \"%s\", \"descricao\": \"%s\", \"tipoId\": %d, \"ativo\": %b}",
                b.getId(), b.getNome(), b.getDescricao(), b.getTipoId(), b.isAtivo()
            ));
        }
        JsonMini.salvar(CAMINHO, linhas);
    }

    private String extrairValor(String json, String chave) {
        String alvo = "\"" + chave + "\":";
        int inicio = json.indexOf(alvo);
        if (inicio == -1) return "";
        inicio += alvo.length();
        int fim = json.indexOf(",", inicio);
        if (fim == -1) fim = json.indexOf("}", inicio);
        if (fim == -1) return "";
        return json.substring(inicio, fim).replace("\"", "").trim();
    }
}