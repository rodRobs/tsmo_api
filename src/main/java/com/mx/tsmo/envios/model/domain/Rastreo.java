package com.mx.tsmo.envios.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RASTREOS")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Rastreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_rastreo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int etapa;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String latitud;
    private String longitud;
    private String estado;
    private String municipio;
    private String pais;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @JsonIgnore // Soluciona ERROR: java.lang.IllegalStateException: Cannot call sendError() after the response has been committed
    @ManyToOne
    @JoinColumn(name="id_envio")
    private Envio envio;

    public Rastreo(int etapa, String nombre, String descripcion, String municipio, String estado, String pais, String latitud, String longitud, Envio envio){
        this.etapa = etapa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.municipio = municipio;
        this.pais = pais;
        this.envio = envio;
        this.ubicacion = municipio + ", " + estado + ", " + pais;
    }

}
