package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carga")
    private Long id;
    @Column(name = "carga_kg")
    private double carga;
    private Float flete;
    private Float recoleccion;
    @Column(name = "entrega_domicilio")
    private Float domicilio;
    @Column(name = "cargo_por_combustible")
    private Float cxc;
    @Column(name = "utilidad_tsmo")
    private Float precio;
    @Column(name = "precio_final")
    private double precioFinal;



}
