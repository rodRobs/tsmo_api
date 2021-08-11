package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.cotizacion.model.dto.CargaDto;
import com.mx.tsmo.cotizacion.model.dto.PostCotizacionDto;
import com.mx.tsmo.domain.dtos.CotizacionDto;
import com.mx.tsmo.envios.model.dto.PostCancelacion;
import com.mx.tsmo.envios.model.dto.ResponseCancelacion;

import javax.ws.rs.core.Response;

public interface EnviaService {

    // public String cotizacion(Cotizacion cotizacion);
    // public Costo[] cotizacion(Cotizacion cotizacion);
    public Response cotizacion(Cotizacion cotizacion, int tipoCarga);
    // public Costo calcularEnvia(Cotizacion cotizacion);
    public Response calcularEnvia(Cotizacion cotizacion, int tipoCarga);
    public CargaDto crearCargaDTODeResponseCotizacion(Costo costo);
    ResponseCancelacion cancelacion(PostCancelacion postCancelacion);
    Cotizacion cotizacionEnvia(Cotizacion cotizacion);

}
