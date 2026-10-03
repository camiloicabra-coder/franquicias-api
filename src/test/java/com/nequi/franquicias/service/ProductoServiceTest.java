package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.ProductoMayorStockResponse;
import com.nequi.franquicias.dto.ProductoRequest;
import com.nequi.franquicias.dto.ProductoResponse;
import com.nequi.franquicias.dto.StockRequest;
import com.nequi.franquicias.entity.Franquicia;
import com.nequi.franquicias.entity.Producto;
import com.nequi.franquicias.entity.Sucursal;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.ProductoRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Franquicia franquicia;
    private Sucursal sucursal;
    private Producto producto;

    @BeforeEach
    void setUp() {

        franquicia = new Franquicia("McDonald's");

        sucursal = new Sucursal(
                "Sucursal Centro",
                franquicia
        );

        producto = new Producto(
                "Arroz",
                100,
                sucursal
        );
    }

    @Test
    void debeCrearProducto() {

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        ProductoRequest request = new ProductoRequest();
        request.setNombre("Arroz");
        request.setStock(100);

        ProductoResponse response =
                productoService.crearProducto(
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals("Arroz", response.getNombre());
        assertEquals(100, response.getStock());

        verify(sucursalRepository)
                .findById(1L);

        verify(productoRepository)
                .save(any(Producto.class));
    }

    @Test
    void noDebeCrearProductoSiSucursalNoExiste() {

        when(sucursalRepository.findById(999L))
                .thenReturn(Optional.empty());

        ProductoRequest request = new ProductoRequest();
        request.setNombre("Arroz");
        request.setStock(100);

        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.crearProducto(
                        999L,
                        request
                )
        );

        verify(sucursalRepository)
                .findById(999L);

        verify(productoRepository, never())
                .save(any(Producto.class));
    }

    @Test
    void debeActualizarStock() {

        when(productoRepository.findByIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        StockRequest request = new StockRequest();
        request.setStock(250);

        ProductoResponse response =
                productoService.actualizarStock(
                        1L,
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals(250, response.getStock());

        verify(productoRepository)
                .findByIdAndSucursalId(1L, 1L);

        verify(productoRepository)
                .save(producto);
    }

    @Test
    void noDebeActualizarStockSiProductoNoPerteneceALaSucursal() {

        when(productoRepository.findByIdAndSucursalId(1L, 2L))
                .thenReturn(Optional.empty());

        StockRequest request = new StockRequest();
        request.setStock(250);

        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.actualizarStock(
                        2L,
                        1L,
                        request
                )
        );

        verify(productoRepository)
                .findByIdAndSucursalId(1L, 2L);

        verify(productoRepository, never())
                .save(any(Producto.class));
    }

    @Test
    void debeActualizarNombre() {

        when(productoRepository.findByIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        NombreRequest request = new NombreRequest();
        request.setNombre("Arroz Integral");

        ProductoResponse response =
                productoService.actualizarNombre(
                        1L,
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals(
                "Arroz Integral",
                response.getNombre()
        );

        verify(productoRepository)
                .findByIdAndSucursalId(1L, 1L);

        verify(productoRepository)
                .save(producto);
    }

    @Test
    void debeEliminarProducto() {

        when(productoRepository.findByIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(producto));

        productoService.eliminarProducto(
                1L,
                1L
        );

        verify(productoRepository)
                .findByIdAndSucursalId(1L, 1L);

        verify(productoRepository)
                .delete(producto);
    }

    @Test
    void noDebeEliminarProductoSiNoPerteneceALaSucursal() {

        when(productoRepository.findByIdAndSucursalId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.eliminarProducto(
                        2L,
                        1L
                )
        );

        verify(productoRepository)
                .findByIdAndSucursalId(1L, 2L);

        verify(productoRepository, never())
                .delete(any(Producto.class));
    }

    @Test
    void debeObtenerProductosConMayorStockPorSucursal() {

        when(franquiciaRepository.findById(1L))
                .thenReturn(Optional.of(franquicia));

        when(productoRepository
                .findProductosConMayorStockPorSucursal(1L))
                .thenReturn(List.of(producto));

        List<ProductoMayorStockResponse> response =
                productoService.obtenerProductosMayorStock(1L);

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(
                "Arroz",
                response.get(0).getProducto()
        );

        assertEquals(
                100,
                response.get(0).getStock()
        );

        assertEquals(
                "Sucursal Centro",
                response.get(0).getSucursal()
        );

        verify(franquiciaRepository)
                .findById(1L);

        verify(productoRepository)
                .findProductosConMayorStockPorSucursal(1L);
    }

    @Test
    void noDebeObtenerMayorStockSiFranquiciaNoExiste() {

        when(franquiciaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.obtenerProductosMayorStock(999L)
        );

        verify(franquiciaRepository)
                .findById(999L);

        verify(productoRepository, never())
                .findProductosConMayorStockPorSucursal(anyLong());
    }
}