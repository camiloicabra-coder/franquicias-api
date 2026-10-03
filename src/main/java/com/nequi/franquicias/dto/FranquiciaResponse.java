package com.nequi.franquicias.dto;

public class FranquiciaResponse {

    private Long id;
    private String nombre;

    public FranquiciaResponse(
            Long id,
            String nombre) {

        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
