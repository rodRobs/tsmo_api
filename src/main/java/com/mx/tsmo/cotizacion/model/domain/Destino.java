package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "DESTINOS")
public class Destino implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_destino")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String destinatario;
    @Column(length = 100)
    private String destinatario2;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_destino")
    private List<Telefono> telefonos;
    @Column(length = 100)
    private String email;
    @Column(length = 200)
    private String referencia;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @PrePersist
    public void setCreate_At() {
        createAt = new Date();
    }

}
