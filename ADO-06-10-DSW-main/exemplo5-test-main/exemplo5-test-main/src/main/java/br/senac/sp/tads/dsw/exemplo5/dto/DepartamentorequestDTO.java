package br.senac.sp.tads.dsw.exemplo5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DepartamentorequestDTO(
    @NotBlank String nome,
    @Positive Double orcamento
) {
    
}
