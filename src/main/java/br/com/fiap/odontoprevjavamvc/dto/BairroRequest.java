package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BairroRequest {

    @NotBlank(message = "O nome do bairro é obrigatório.")
    private String nome;

    @NotNull(message = "A cidade é obrigatória.")
    private CidadeRequest cidade;
}