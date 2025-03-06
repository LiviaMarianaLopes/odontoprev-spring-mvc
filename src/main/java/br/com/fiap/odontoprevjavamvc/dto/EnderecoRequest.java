package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EnderecoRequest {

    @NotBlank(message = "O logradouro é obrigatório.")
    private String logradouro;

    @NotNull(message = "O número é obrigatório.")
    private int numero;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos numéricos.")
    private int cep;

    private String complemento;

    @NotNull(message = "O bairro é obrigatório.")
    private BairroRequest bairro;
}