package feira.graspcrud.dto;

public class TipoBarracaRequest {
    public int id;
    public String nome;
    public String descricao;

    public TipoBarracaRequest(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}