package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

public class TipoBarraca {

    private int id;
    private String nome;
    private String descricao;

    public TipoBarraca(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao == null ? "" : descricao;
    }

    public void validar() {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraNegocioException("Nome do TipoBarraca e obrigatorio e deve ter ao menos 3 caracteres.");
        }
    }

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s", id, nome, descricao);
    }
}