package com.barraca.backend.dto;

public class BarracaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private Long tipoBarracaId;
    private String tipoBarracaNome;

    public BarracaResponseDTO() {}

    public BarracaResponseDTO(Long id, String nome, String descricao, boolean ativo,
                               Long tipoBarracaId, String tipoBarracaNome) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.tipoBarracaId = tipoBarracaId;
        this.tipoBarracaNome = tipoBarracaNome;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Long getTipoBarracaId() { return tipoBarracaId; }
    public void setTipoBarracaId(Long tipoBarracaId) { this.tipoBarracaId = tipoBarracaId; }

    public String getTipoBarracaNome() { return tipoBarracaNome; }
    public void setTipoBarracaNome(String tipoBarracaNome) { this.tipoBarracaNome = tipoBarracaNome; }
}
