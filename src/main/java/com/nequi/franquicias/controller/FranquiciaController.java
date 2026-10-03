package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.FranquiciaRequest;
import com.nequi.franquicias.dto.FranquiciaResponse;
import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.service.FranquiciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    public FranquiciaController(
            FranquiciaService franquiciaService) {

        this.franquiciaService = franquiciaService;
    }

    @Operation(
            summary = "Crear una franquicia",
            description = "Crea una nueva franquicia en el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Franquicia creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<FranquiciaResponse> crearFranquicia(
            @Valid @RequestBody FranquiciaRequest request) {

        FranquiciaResponse response =
                franquiciaService.crearFranquicia(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Actualizar nombre de franquicia",
            description = "Actualiza el nombre de una franquicia existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Franquicia actualizada correctamente"
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
    @PutMapping("/{franquiciaId}")
    public ResponseEntity<FranquiciaResponse> actualizarNombre(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody NombreRequest request) {

        FranquiciaResponse response =
                franquiciaService.actualizarNombre(
                        franquiciaId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}