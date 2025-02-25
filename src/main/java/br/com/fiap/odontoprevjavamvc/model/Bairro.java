package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_BAIRRO")
@Data
public class Bairro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_BAIRRO")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "id")
    private Cidade cidade;
}