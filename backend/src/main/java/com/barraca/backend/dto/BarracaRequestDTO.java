package com.barraca.backend.dto;

public class BarracaRequestDTO {
    private String nome;
    private String descricao;
    private String localizacao;
    private boolean ativo;
    private Long tipoBarracaId;

    public BarracaRequestDTO() {}

    public BarracaRequestDTO(String nome, String descricao, String localizacao, boolean ativo, Long tipoBarracaId) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.ativo = ativo;
        this.tipoBarracaId = tipoBarracaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getTipoBarracaId() {
        return tipoBarracaId;
    }

    public void setTipoBarracaId(Long tipoBarracaId) {
        this.tipoBarracaId = tipoBarracaId;
    }
}
