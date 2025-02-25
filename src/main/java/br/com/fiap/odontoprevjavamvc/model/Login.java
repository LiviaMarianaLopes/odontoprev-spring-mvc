package br.com.fiap.odontoprevjavamvc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OP_LOGIN")
@Data
public class Login{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    public Login() {
    }

    public Login(Long id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }

    public Login(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

}