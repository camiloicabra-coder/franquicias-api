package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.NombreRequest;
import com.nequi.franquicias.dto.ProductoMayorStockResponse;
import com.nequi.franquicias.dto.ProductoRequest;
import com.nequi.franquicias.dto.ProductoResponse;
import com.nequi.franquicias.dto.StockRequest;
import com.nequi.franquicias.entity.Producto;
import com.nequi.franquicias.entity.Sucursal;
import com.nequi.franquicias.exception.ResourceNotFoundException;
import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.ProductoRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository,
            FranquiciaRepository franquiciaRepository) {

        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public ProductoResponse crearProducto(
            Long sucursalId,
            ProductoRequest request) {

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada"));

        Producto producto = new Producto(
                request.getNombre(),
                request.getStock(),
                sucursal
        );

        Producto guardado = productoRepository.save(producto);

        return convertirAResponse(guardado);
    }

    public void eliminarProducto(
            Long sucursalId,
            Long productoId) {

        Producto producto = productoRepository
                .findByIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado en la sucursal indicada"));

        productoRepository.delete(producto);
    }

    public ProductoResponse actualizarStock(
            Long sucursalId,
            Long productoId,
            StockRequest request) {

        Producto producto = productoRepository
                .findByIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado en la sucursal indicada"));

        producto.setStock(request.getStock());

        Producto actualizado = productoRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    public ProductoResponse actualizarNombre(
            Long sucursalId,
            Long productoId,
            NombreRequest request) {

        Producto producto = productoRepository
                .findByIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado en la sucursal indicada"));

        producto.setNombre(request.getNombre());

        Producto actualizado = productoRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    public List<ProductoMayorStockResponse> obtenerProductosMayorStock(
            Long franquiciaId) {

        franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Franquicia no encontrada"));

        return productoRepository
                .findProductosConMayorStockPorSucursal(franquiciaId)
                .stream()
                .map(producto -> new ProductoMayorStockResponse(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        producto.getSucursal().getId(),
                        producto.getSucursal().getNombre()
                ))
                .toList();
    }

    private ProductoResponse convertirAResponse(
            Producto producto) {

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                producto.getSucursal().getId()
        );
    }
}