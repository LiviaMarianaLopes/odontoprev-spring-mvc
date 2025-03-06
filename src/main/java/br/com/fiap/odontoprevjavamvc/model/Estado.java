package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_ESTADO")
@Data
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_ESTADO")
    private String nome;

    @Column(name = "SIGLA_ESTADO")
    private String sigla;

    public Estado(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Estado() {
    }
}
