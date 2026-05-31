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

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "tipo_barraca_id", nullable = false)
    private TipoBarraca tipoBarraca;

    public Barraca() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public TipoBarraca getTipoBarraca() { return tipoBarraca; }
    public void setTipoBarraca(TipoBarraca tipoBarraca) { this.tipoBarraca = tipoBarraca; }
}
