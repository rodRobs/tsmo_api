package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "COTIZACIONES")
public class Cotizacion  {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_cotizacion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String cuenta;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_opciones")
    private Opciones opciones;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_origen")
    private Origen origen;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_destino")
    private Destino destino;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_cotizacion")
    private List<Detalle> detalle;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;
    private String realiza;
    private String tipoServicio;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_cotizacion")
    private List<Servicio> servicios;

    @OneToOne(mappedBy = "cotizacion", cascade = {CascadeType.ALL})
    private Costo costo;

    public String toString() {
        return "id: " + id + "\n"
                + " cuenta: " + cuenta + "\n"
                + " opciones: " + opciones + "\n"
                + " origen: " + origen + "\n"
                + " destino: " + destino + "\n"
                + " detalle: " + detalle + "\n"
                + " realiza: " + realiza + "\n"
                + " servicios: " + servicios ;
    }

    @PrePersist
    public void setCreate_At() {
        createAt = new Date();
    }
}
