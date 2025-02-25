package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_UNIDADE")
@Data
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_UNIDADE")
    private String nome;

    @Column(name = "telefone")
    private int telefone;

    @OneToOne
    @JoinColumn(name = "ID_ENDERECO", referencedColumnName = "id")
    private Endereco endereco;

}