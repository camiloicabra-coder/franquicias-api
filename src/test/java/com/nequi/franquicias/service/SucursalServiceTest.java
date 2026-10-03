package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.SucursalRequest;
import com.nequi.franquicias.dto.SucursalResponse;
import com.nequi.franquicias.entity.Franquicia;
import com.nequi.franquicias.entity.Sucursal;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.SucursalRepository;
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
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Franquicia franquicia;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {

        franquicia = new Franquicia("McDonald's");

        sucursal = new Sucursal(
                "Sucursal Centro",
                franquicia
        );
    }

    @Test
    void debeCrearSucursal() {

        when(franquiciaRepository.findById(1L))
                .thenReturn(Optional.of(franquicia));

        when(sucursalRepository.save(any(Sucursal.class)))
                .thenReturn(sucursal);

        SucursalRequest request = new SucursalRequest();
        request.setNombre("Sucursal Centro");

        SucursalResponse response =
                sucursalService.crearSucursal(
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals(
                "Sucursal Centro",
                response.getNombre()
        );

        verify(franquiciaRepository).findById(1L);
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void debeActualizarSucursalSiPerteneceALaFranquicia() {

        when(sucursalRepository.existsByIdAndFranquiciaId(1L, 1L))
                .thenReturn(true);

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        when(sucursalRepository.save(any(Sucursal.class)))
                .thenReturn(sucursal);

        NombreRequest request = new NombreRequest();
        request.setNombre("Sucursal Norte");

        SucursalResponse response =
                sucursalService.actualizarNombre(
                        1L,
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals(
                "Sucursal Norte",
                response.getNombre()
        );

        verify(sucursalRepository)
                .existsByIdAndFranquiciaId(1L, 1L);

        verify(sucursalRepository)
                .findById(1L);

        verify(sucursalRepository)
                .save(sucursal);
    }

    @Test
    void noDebeActualizarSucursalSiNoPerteneceALaFranquicia() {

        when(sucursalRepository.existsByIdAndFranquiciaId(1L, 2L))
                .thenReturn(false);

        NombreRequest request = new NombreRequest();
        request.setNombre("Sucursal Norte");

        assertThrows(
                ResourceNotFoundException.class,
                () -> sucursalService.actualizarNombre(
                        2L,
                        1L,
                        request
                )
        );

        verify(sucursalRepository)
                .existsByIdAndFranquiciaId(1L, 2L);

        verify(sucursalRepository, never())
                .findById(anyLong());

        verify(sucursalRepository, never())
                .save(any(Sucursal.class));
    }

    @Test
    void debeLanzarExcepcionSiFranquiciaNoExisteAlCrearSucursal() {

        when(franquiciaRepository.findById(999L))
                .thenReturn(Optional.empty());

        SucursalRequest request = new SucursalRequest();
        request.setNombre("Sucursal Centro");

        assertThrows(
                ResourceNotFoundException.class,
                () -> sucursalService.crearSucursal(
                        999L,
                        request
                )
        );

        verify(franquiciaRepository)
                .findById(999L);

        verify(sucursalRepository, never())
                .save(any(Sucursal.class));
    }
}