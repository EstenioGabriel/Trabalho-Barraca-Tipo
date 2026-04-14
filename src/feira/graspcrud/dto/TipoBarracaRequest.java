package feira.graspcrud.dto;

public class TipoBarracaRequest {
    public int id; 
    public String nome;

    public TipoBarracaRequest(int id, String nome) { 
        this.id = id; 
        this.nome = nome; 
    }
}