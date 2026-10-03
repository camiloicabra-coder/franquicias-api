package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.SucursalRequest;
import com.nequi.franquicias.dto.SucursalResponse;
import com.nequi.franquicias.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(
            SucursalService sucursalService) {

        this.sucursalService = sucursalService;
    }

    @Operation(
            summary = "Crear una sucursal",
            description = "Crea una nueva sucursal asociada a una franquicia"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Sucursal creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada"
            )
    })
    @PostMapping
    public ResponseEntity<SucursalResponse> crearSucursal(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalRequest request) {

        SucursalResponse response =
                sucursalService.crearSucursal(
                        franquiciaId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Actualizar nombre de sucursal",
            description = "Actualiza el nombre de una sucursal perteneciente a una franquicia"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursal actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada en la franquicia indicada"
            )
    })
    @PutMapping("/{sucursalId}")
    public ResponseEntity<SucursalResponse> actualizarNombre(
            @PathVariable Long franquiciaId,
            @PathVariable Long sucursalId,
            @Valid @RequestBody NombreRequest request) {

        SucursalResponse response =
                sucursalService.actualizarNombre(
                        franquiciaId,
                        sucursalId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}