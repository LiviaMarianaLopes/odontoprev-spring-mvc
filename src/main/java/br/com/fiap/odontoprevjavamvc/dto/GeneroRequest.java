package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GeneroRequest {

    @NotBlank(message = "O gênero é obrigatório.")
    private String titulo;
}

