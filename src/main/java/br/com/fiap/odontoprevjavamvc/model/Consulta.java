package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "OP_CONSULTA")
@Data
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_consulta")
    private Timestamp data;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", referencedColumnName = "id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_DENTISTA", referencedColumnName = "id")
    private Dentista dentista;

    @ManyToOne
    @JoinColumn(name = "ID_UNIDADE", referencedColumnName = "id")
    private Unidade unidade;

    @Column(name = "MOTIVO_CONSULTA")
    private String motivo;
}
