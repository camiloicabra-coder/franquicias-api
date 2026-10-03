package com.nequi.franquicias.repository;

import com.nequi.franquicias.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByIdAndSucursalId(
            Long id,
            Long sucursalId
    );

    Optional<Producto> findByIdAndSucursalId(
            Long id,
            Long sucursalId
    );

    @Query("""
        SELECT p
        FROM Producto p
        WHERE p.sucursal.franquicia.id = :franquiciaId
        AND p.stock = (
            SELECT MAX(p2.stock)
            FROM Producto p2
            WHERE p2.sucursal.id = p.sucursal.id
        )
        """)
    List<Producto> findProductosConMayorStockPorSucursal(
            @Param("franquiciaId") Long franquiciaId
    );
}