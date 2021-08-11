package com.mx.tsmo.cotizacion.model.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ltl_local")
@Data
public class LtlLocal extends Carga {

    @Column(name = "distancia_minima")
    private double distanciaMin;
    @Column(name = "distancia_maxima")
    private double distanciaMax;

}
