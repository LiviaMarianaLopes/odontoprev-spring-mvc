package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstadoRequest {

    @NotBlank(message = "O nome do estado é obrigatório.")
    private String nome;

    @NotBlank(message = "A sigla do estado é obrigatória.")
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter 2 caracteres.")
    private String sigla;
}
