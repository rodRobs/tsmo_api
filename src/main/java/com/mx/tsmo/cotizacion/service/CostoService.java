package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;

import java.util.List;
import java.util.Optional;


public interface CostoService {

    Costo guardar(Costo costo);
    Costo listarPorCotizacion(Cotizacion cotizacion);
    Costo buscarPorCotizacion(Cotizacion cotizacion);

}
