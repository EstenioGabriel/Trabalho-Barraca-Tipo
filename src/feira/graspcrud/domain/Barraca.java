package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

public class Barraca {

    private int id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private int tipoId;

    public Barraca(int id, String nome, String descricao, boolean ativo, int tipoId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao == null ? "" : descricao;
        this.ativo = ativo;
        this.tipoId = tipoId;
    }

    public void validar() {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraNegocioException("Nome da Barraca e obrigatorio e deve ter ao menos 3 caracteres.");
        }
        if (tipoId <= 0) {
            throw new RegraNegocioException("Barraca deve estar associada a um TipoBarraca valido.");
        }
    }

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getDescricao() { return descricao; }

    public boolean isAtivo() { return ativo; }

    public int getTipoId() { return tipoId; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %s", id, nome, descricao, ativo ? "ATIVA" : "INATIVA");
    }
}