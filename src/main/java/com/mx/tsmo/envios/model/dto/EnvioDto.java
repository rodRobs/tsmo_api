package com.mx.tsmo.envios.model.dto;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EnvioDto {

    private Long id;
    private String guiaTsmo;
    private String guiaProveedor;
    private Documentacion documentacion;
    private Cliente cliente;
    private Date createAt;
    private String pago;

}
