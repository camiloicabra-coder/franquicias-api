package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.FranquiciaRequest;
import com.nequi.franquicias.dto.FranquiciaResponse;
import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.entity.Franquicia;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;

@Service
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    public FranquiciaResponse crearFranquicia(
            FranquiciaRequest request) {

        Franquicia franquicia = new Franquicia(
                request.getNombre()
        );

        Franquicia guardada = franquiciaRepository.save(franquicia);

        return convertirAResponse(guardada);
    }

    public FranquiciaResponse actualizarNombre(
            Long franquiciaId,
            NombreRequest request) {

        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Franquicia no encontrada"));

        franquicia.setNombre(request.getNombre());

        Franquicia actualizada = franquiciaRepository.save(franquicia);

        return convertirAResponse(actualizada);
    }

    private FranquiciaResponse convertirAResponse(
            Franquicia franquicia) {

        return new FranquiciaResponse(
                franquicia.getId(),
                franquicia.getNombre()
        );
    }
}