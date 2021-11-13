package com.mx.tsmo.envios.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.cotizacion.model.domain.Opciones;
import com.mx.tsmo.cotizacion.model.domain.Telefono;
import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.security.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ENVIOS")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Envio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Long id;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "numero_guia_tsmo", unique = true)
    private String guiaTsmo;
    @Column(name = "numero_guia_proveedor", unique = true)
    private String guiaProveedor;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_documentacion")
    private Documentacion documentacion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "estado_pago")
    private String estadoPago;
    @Column(name = "estado_envio")
    private String estadoEnvio;
    private String mensaje;
    @Column(name = "id_pago_stripe")
    private String pago;
    @OneToOne
    @JoinColumn(name = "id_entrega")
    private Entrega entrega;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL)
    private List<Rastreo> rastreos;

    private int etapa;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_granel", nullable = true)
    private EnviosGranel granel;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public String toString() {
        return "[id]: " + id
                + " [guiaTsmo]: " + guiaTsmo
                + " [guiaProveedor]: " + guiaProveedor
                + " [cliente]: " + cliente.toString()
                + " [estadoPago]: " + estadoPago
                + " [estadoEnvio]: " + estadoEnvio;
    }

    @PrePersist
    public void setCreate_At() {
        createAt = new Date();
    }
 }
