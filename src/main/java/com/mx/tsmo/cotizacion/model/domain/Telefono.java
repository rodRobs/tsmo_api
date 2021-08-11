package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "TELEFONOS")
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Long id;
    @Column(length = 20)
    private String numeroTelefono;
    /*
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_origen")
    private Origen origen;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_destino")
    private Destino destino;
     */

    public String toString() {
        return "Numero Telefono: " + numeroTelefono +
                "id: " + id;
    }

}
