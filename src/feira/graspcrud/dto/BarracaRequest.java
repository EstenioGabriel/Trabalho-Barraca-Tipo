package feira.graspcrud.dto;

public class BarracaRequest {
    public int id;
    public String nome;
    public String descricao;
    public int tipoId;
    public boolean ativo;

    public BarracaRequest(int id, String nome, String descricao, int tipoId, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipoId = tipoId;
        this.ativo = ativo;
    }
}