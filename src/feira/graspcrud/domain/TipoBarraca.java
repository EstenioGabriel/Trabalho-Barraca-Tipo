package feira.graspcrud.domain;

public class TipoBarraca {
    private int id; private String nome;

    public TipoBarraca(int id, String nome) { this.id = id; this.nome = nome; }

    public int getId() { return id; }

    public String getNome() { return nome; }
}