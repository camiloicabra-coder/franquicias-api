package com.nequi.franquicias.entity;

import jakarta.persistence.*;

@Entity
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franquicia_id", nullable = false)
    private Franquicia franquicia;

    public Sucursal() {
    }

    public Sucursal(String nombre, Franquicia franquicia) {
        this.nombre = nombre;
        this.franquicia = franquicia;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Franquicia getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(Franquicia franquicia) {
        this.franquicia = franquicia;
    }
}