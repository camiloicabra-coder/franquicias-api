package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.ProductoMayorStockResponse;
import com.nequi.franquicias.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaProductoController {

    private final ProductoService productoService;

    public FranquiciaProductoController(
            ProductoService productoService) {

        this.productoService = productoService;
    }

    @Operation(
            summary = "Obtener productos con mayor stock",
            description = "Obtiene el producto con mayor stock de cada sucursal de una franquicia"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos obtenidos correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada"
            )
    })
    @GetMapping("/{franquiciaId}/productos/mayor-stock")
    public ResponseEntity<List<ProductoMayorStockResponse>> obtenerProductosMayorStock(
            @PathVariable Long franquiciaId) {

        List<ProductoMayorStockResponse> productos =
                productoService.obtenerProductosMayorStock(franquiciaId);

        return ResponseEntity.ok(productos);
    }
}