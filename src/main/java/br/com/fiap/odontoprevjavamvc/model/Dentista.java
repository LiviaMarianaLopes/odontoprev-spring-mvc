package br.com.fiap.odontoprevjavamvc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "OP_DENTISTA")
@Data
public class Dentista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME_DENTISTA")
    private String nome;

    @Column(name = "cro")
    private String cro;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "data_de_nascimento")
    private Date dataNascimento;
    @Column(name = "EMAIL_DENTISTA")
    private String email;
    @Column(name = "CPF_DENTISTA")
    private Long cpf;
    @OneToOne
    @JoinColumn(name = "id_genero", referencedColumnName = "id")
    private Genero genero;

    @Column(name = "telefone_dentista")
    private Long telefone;

    @OneToOne
    @JoinColumn(name = "ID_ENDERECO", referencedColumnName = "id")
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    private Login login;
    @Column(name = "DENTISTA_SUSPEITO")
    private String suspeito;

}
