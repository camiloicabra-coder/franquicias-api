package com.nequi.franquicias.dto;

public class ProductoResponse {

    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;

    public ProductoResponse(
            Long id,
            String nombre,
            Integer stock,
            Long sucursalId) {

        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.sucursalId = sucursalId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public Long getSucursalId() {
        return sucursalId;
    }
}