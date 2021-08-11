package com.mx.tsmo.envios.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "Entregas")
public class Entrega implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id_entrega")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recibio;
    private String parentesco;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    private String longitud;
    private String latitud;
    private String identificacion;

}
