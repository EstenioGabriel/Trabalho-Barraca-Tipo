package com.barraca.backend.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "barraca")
public class Barraca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    private String localizacao;

    private boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_barraca_id", nullable = false)
    private TipoBarraca tipoBarraca;

    public Barraca() {}

    public Barraca(Long id, String nome, String descricao, String localizacao, boolean ativo, TipoBarraca tipoBarraca) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.ativo = ativo;
        this.tipoBarraca = tipoBarraca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TipoBarraca getTipoBarraca() {
        return tipoBarraca;
    }

    public void setTipoBarraca(TipoBarraca tipoBarraca) {
        this.tipoBarraca = tipoBarraca;
    }
}
