package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "COSTOS")
public class Costo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_costo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tipo_guia", length = 30)
    private String tipoGuia;
    @Column(length = 10)
    private String zona;
    @Column(length = 20)
    private String tipoServicio;
    @Column(nullable = true)
    private int paquete;
    @Column(nullable = true)
    private double peso;
    @Column(name = "peso_volumetrico", nullable = true)
    private double pesoVolumetrico;
    @Column(nullable = true)
    private double volumen;
    @Column(nullable = true)
    private double flete;
    @Column(nullable = true)
    private double combustible;
    @Column(nullable = true)
    private double servicio;
    @Column(nullable = true)
    private double subTotal;
    @Column(nullable = true)
    private double iva;
    @Column(nullable = true)
    private double total;
    @Column(nullable = true)
    private double costoTotal;
    @Column(length = 30)
    private String moneda;
    @OneToOne
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;
    private String realiza;
    private String fCompromisoEntrega;

    @PrePersist
    public void setCreate_At() {
        createAt = new Date();
    }
}
