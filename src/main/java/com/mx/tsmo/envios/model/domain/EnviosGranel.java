package com.mx.tsmo.envios.model.domain;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.envios.model.enums.EstadoEnvio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ENVIOS_GRANEL")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EnviosGranel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    private String nombre;
    @Column(name = "TOTAL_PAQUETES")
    private long totalPaquetes;
    private long entregados;
    private String estado;
    private Date createAt;

    @OneToMany(mappedBy = "granel", cascade = CascadeType.PERSIST)
    private List<Envio> envios;

    @PrePersist
    public void setFecha() {
        createAt = new Date();
        estado = EstadoEnvio.PENDIENTE.toString();
    }

}
