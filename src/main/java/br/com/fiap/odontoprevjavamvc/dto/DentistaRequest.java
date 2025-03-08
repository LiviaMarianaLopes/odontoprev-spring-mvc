package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class DentistaRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @Pattern(regexp = "^[A-Z]{2}\\d{4,6}$", message = "O CRO deve estar no formato correto (UF + número).")
    @NotBlank(message = "O CRO é obrigatório.")
    private String cro;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private Date dataNascimento;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotNull(message = "O login é obrigatório.")
    private LoginRequest login;

    @NotNull(message = "O endereço é obrigatório.")
    private EnderecoRequest endereco;

    @NotNull(message = "O gênero é obrigatório.")
    private GeneroRequest genero;
}
