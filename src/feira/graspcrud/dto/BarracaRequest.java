package feira.graspcrud.dto;

public class BarracaRequest {
    public int id; 
    public String nome; 
    public int tipoId; 
    public boolean ativo;

    public BarracaRequest(int id, String nome, int tipoId, boolean ativo) {
        this.id = id; 
        this.nome = nome; 
        this.tipoId = tipoId; 
        this.ativo = ativo;
    }
}