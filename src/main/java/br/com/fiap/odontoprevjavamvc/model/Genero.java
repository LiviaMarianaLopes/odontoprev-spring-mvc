package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_GENERO")
@Data
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo")
    private String titulo;

}
