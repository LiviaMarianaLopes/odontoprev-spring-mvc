package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_CIDADE")
@Data
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_CIDADE")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "id")
    private Estado estado;
}
