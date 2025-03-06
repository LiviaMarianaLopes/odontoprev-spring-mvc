package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CidadeRequest {

    @NotBlank(message = "O nome da cidade é obrigatório.")
    private String nome;

    @NotNull(message = "O estado é obrigatório.")
    private EstadoRequest estado;
}
