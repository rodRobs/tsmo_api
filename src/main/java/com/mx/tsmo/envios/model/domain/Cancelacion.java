package com.mx.tsmo.envios.model.domain;

import com.mx.tsmo.cotizacion.model.domain.Opciones;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CANCELACIONES")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cancelacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_cancelacion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    private String mensaje;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_envio")
    private Envio envio;

}
