package com.nequi.franquicias.dto;

public class ProductoMayorStockResponse {

    private Long productoId;
    private String producto;
    private Integer stock;
    private Long sucursalId;
    private String sucursal;

    public ProductoMayorStockResponse(
            Long productoId,
            String producto,
            Integer stock,
            Long sucursalId,
            String sucursal) {

        this.productoId = productoId;
        this.producto = producto;
        this.stock = stock;
        this.sucursalId = sucursalId;
        this.sucursal = sucursal;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getProducto() {
        return producto;
    }

    public Integer getStock() {
        return stock;
    }

    public Long getSucursalId() {
        return sucursalId;
    }

    public String getSucursal() {
        return sucursal;
    }
}