package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.FranquiciaRequest;
import com.nequi.franquicias.dto.FranquiciaResponse;
import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.entity.Franquicia;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    private Franquicia franquicia;

    @BeforeEach
    void setUp() {

        franquicia = new Franquicia("McDonald's");
    }

    @Test
    void debeCrearFranquicia() {

        when(franquiciaRepository.save(any(Franquicia.class)))
                .thenReturn(franquicia);

        FranquiciaRequest request = new FranquiciaRequest();
        request.setNombre("McDonald's");

        FranquiciaResponse response =
                franquiciaService.crearFranquicia(request);

        assertNotNull(response);
        assertEquals("McDonald's", response.getNombre());

        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void debeActualizarNombreFranquicia() {

        when(franquiciaRepository.findById(1L))
                .thenReturn(Optional.of(franquicia));

        when(franquiciaRepository.save(any(Franquicia.class)))
                .thenReturn(franquicia);

        NombreRequest request = new NombreRequest();
        request.setNombre("Burger King");

        FranquiciaResponse response =
                franquiciaService.actualizarNombre(1L, request);

        assertNotNull(response);
        assertEquals("Burger King", response.getNombre());

        verify(franquiciaRepository).findById(1L);
        verify(franquiciaRepository).save(franquicia);
    }

    @Test
    void debeLanzarExcepcionSiFranquiciaNoExiste() {

        when(franquiciaRepository.findById(999L))
                .thenReturn(Optional.empty());

        NombreRequest request = new NombreRequest();
        request.setNombre("Burger King");

        assertThrows(
                ResourceNotFoundException.class,
                () -> franquiciaService.actualizarNombre(
                        999L,
                        request
                )
        );

        verify(franquiciaRepository).findById(999L);
        verify(franquiciaRepository, never())
                .save(any(Franquicia.class));
    }
}