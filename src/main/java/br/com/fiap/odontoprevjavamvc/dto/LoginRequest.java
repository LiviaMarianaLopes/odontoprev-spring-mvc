package br.com.fiap.odontoprevjavamvc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "A senha é obrigatória.")
    private String senha;
    @NotBlank(message = "O email é obrigatório.")
    private String email;
}
