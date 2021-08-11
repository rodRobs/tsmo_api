package com.mx.tsmo.clientes.model.domain;

import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.security.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Slf4j
@Entity
@Table(name = "CLIENTES")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;

    @Column(nullable = true)
    private int descuento;

    /*
    @OneToMany(mappedBy="cliente", cascade = CascadeType.ALL)
    private Set<Usuario> usuarios;
    */

    public String toString() {
        return " [id]: " + id
                + " [nombre]: " + nombre
                + " [telefono]: " + telefono
                + " [descuento]: " + descuento;
    }

}
