package feira.graspcrud.domain;

public class Barraca {
    private int id; 
    private String nome; 
    private int tipoId; 
    private boolean ativo;

    public Barraca(int id, String nome, int tipoId, boolean ativo) {
        this.id = id; 
        this.nome = nome; 
        this.tipoId = tipoId; 
        this.ativo = ativo;
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getTipoId() { return tipoId; }
    public boolean isAtivo() { return ativo; }
}