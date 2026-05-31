package com.barraca.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BarracaRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome deve ter ao menos 3 caracteres")
    private String nome;

    private String descricao;

    private String localizacao;

    private boolean ativo;

    @NotNull(message = "O tipo de barraca é obrigatório")
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
