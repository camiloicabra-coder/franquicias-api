package com.nequi.franquicias.dto;

import jakarta.validation.constraints.NotBlank;

public class FranquiciaRequest {

    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}