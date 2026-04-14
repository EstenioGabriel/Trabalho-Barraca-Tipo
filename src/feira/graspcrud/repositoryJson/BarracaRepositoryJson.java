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
                int tipoId = Integer.parseInt(extrairValor(json, "tipoId"));
                boolean ativo = extrairValor(json, "ativo").equalsIgnoreCase("true");
                
                cache.add(new Barraca(id, nome, tipoId, ativo));
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public void salvar(Barraca barraca) {
        Barraca existente = buscarPorId(barraca.getId());
        if (existente != null) {
            cache.remove(existente);
        }
        
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
        this.salvar(barraca);
    }

    @Override
    public void remover(int id) {
        cache.removeIf(b -> b.getId() == id);
        salvarNoArquivo();
    }

    private void salvarNoArquivo() {
        List<String> linhas = new ArrayList<>();
        for (Barraca b : cache) {
            linhas.add(String.format(
                "{\"id\": %d, \"nome\": \"%s\", \"tipoId\": %d, \"ativo\": %b}",
                b.getId(), b.getNome(), b.getTipoId(), b.isAtivo()
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