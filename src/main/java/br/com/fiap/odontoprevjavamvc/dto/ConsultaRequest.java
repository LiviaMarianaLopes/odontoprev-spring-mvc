package br.com.fiap.odontoprevjavamvc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.sql.Timestamp;

public record ConsultaRequest(
        @NotNull(message = "A data da consulta é obrigatória")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
        Timestamp data,

        @NotNull(message = "O id do paciente é obrigatório")
        @Positive(message = "Id do paciente inválido")
        long idPaciente,

        @NotNull(message = "O id do dentista é obrigatório")
        @Positive(message = "Id do dentista inválido")
        long idDentista,

        @NotNull(message = "O id da unidade é obrigatório")
        @Positive(message = "Id da unidade inválido")
        long idUnidade,

        @NotBlank(message = "O motivo da consulta é obrigatório")
        String motivo
){
}
