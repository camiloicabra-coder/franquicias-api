package com.nequi.franquicias.dto;

public class SucursalResponse {

    private Long id;
    private String nombre;
    private Long franquiciaId;

    public SucursalResponse(
            Long id,
            String nombre,
            Long franquiciaId) {

        this.id = id;
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getFranquiciaId() {
        return franquiciaId;
    }
}