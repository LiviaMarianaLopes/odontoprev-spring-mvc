package br.com.fiap.odontoprevjavamvc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "OP_PACIENTE")
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_paciente")
    private String nome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "data_de_nascimento")
    private Date dataNascimento;
    @Column(name = "email_paciente")
    private String email;
    @Column(name = "CPF_PACIENTE")
    private Long cpf;
    @Column(name = "telefone_paciente")
    private Long telefone;

    @OneToOne
    @JoinColumn(name = "id_genero", referencedColumnName = "id")
    private Genero genero;

    @OneToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    private Login login;
    @Column(name = "CLIENTE_SUSPEITO")
    private String suspeito;

}
