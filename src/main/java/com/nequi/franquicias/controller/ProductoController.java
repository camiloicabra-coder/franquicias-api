package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.ProductoRequest;
import com.nequi.franquicias.dto.ProductoResponse;
import com.nequi.franquicias.dto.StockRequest;
import com.nequi.franquicias.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sucursales/{sucursalId}/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(
            summary = "Crear un producto",
            description = "Crea un producto y lo asocia a una sucursal"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada"
            )
    })
    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoRequest request) {

        ProductoResponse response =
                productoService.crearProducto(
                        sucursalId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto de una sucursal"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado en la sucursal indicada"
            )
    })
    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId) {

        productoService.eliminarProducto(
                sucursalId,
                productoId
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar stock de producto",
            description = "Actualiza la cantidad disponible de un producto"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Stock inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado en la sucursal indicada"
            )
    })
    @PatchMapping("/{productoId}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId,
            @Valid @RequestBody StockRequest request) {

        ProductoResponse response =
                productoService.actualizarStock(
                        sucursalId,
                        productoId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar nombre de producto",
            description = "Actualiza el nombre de un producto existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado en la sucursal indicada"
            )
    })
    @PutMapping("/{productoId}")
    public ResponseEntity<ProductoResponse> actualizarNombre(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId,
            @Valid @RequestBody NombreRequest request) {

        ProductoResponse response =
                productoService.actualizarNombre(
                        sucursalId,
                        productoId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}