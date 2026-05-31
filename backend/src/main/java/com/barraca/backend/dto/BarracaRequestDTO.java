package com.barraca.backend.dto;

public class BarracaRequestDTO {

    private String nome;
    private String descricao;
    private boolean ativo;
    private Long tipoBarracaId;

    public BarracaRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Long getTipoBarracaId() { return tipoBarracaId; }
    public void setTipoBarracaId(Long tipoBarracaId) { this.tipoBarracaId = tipoBarracaId; }
}
