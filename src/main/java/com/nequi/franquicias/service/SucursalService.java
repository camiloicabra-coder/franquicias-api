package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.SucursalRequest;
import com.nequi.franquicias.dto.SucursalResponse;
import com.nequi.franquicias.entity.Franquicia;
import com.nequi.franquicias.entity.Sucursal;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public SucursalService(
            SucursalRepository sucursalRepository,
            FranquiciaRepository franquiciaRepository) {

        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public SucursalResponse crearSucursal(
            Long franquiciaId,
            SucursalRequest request) {

        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Franquicia no encontrada"));

        Sucursal sucursal = new Sucursal(
                request.getNombre(),
                franquicia
        );

        Sucursal guardada = sucursalRepository.save(sucursal);

        return convertirAResponse(guardada);
    }

    public SucursalResponse actualizarNombre(
            Long franquiciaId,
            Long sucursalId,
            NombreRequest request) {

        if (!sucursalRepository.existsByIdAndFranquiciaId(
                sucursalId,
                franquiciaId)) {

            throw new ResourceNotFoundException(
                    "Sucursal no encontrada en la franquicia indicada");
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada"));

        sucursal.setNombre(request.getNombre());

        Sucursal actualizada = sucursalRepository.save(sucursal);

        return convertirAResponse(actualizada);
    }

    private SucursalResponse convertirAResponse(
            Sucursal sucursal) {

        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getFranquicia().getId()
        );
    }
}