package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.cotizacion.model.domain.Detalle;
import com.mx.tsmo.cotizacion.model.domain.Servicio;
import com.mx.tsmo.cotizacion.model.dto.CargaDto;
import com.mx.tsmo.domain.dtos.CotizacionDto;

import java.util.List;

public interface CotizacionService {

    Cotizacion guardar(Cotizacion cotizacion);
    List<Cotizacion> listar();
    Cotizacion buscarPorId(Long id);
    //Costo cotizacion(Cotizacion cotizacion);

    public double getPeso(Detalle detalle);
    public String URL_Google(Cotizacion cotizacion);
    public String URL_Google_Cotizacion(Cotizacion cotizacion);
    public Double getDistancia(String url);
    public int getTipoDistancia(double distancia, Cotizacion cotizacion);
    public int getTipoDistancia(Double distancia, CotizacionDto cotizacion);
    public int getTipoCarga(Double peso);
    int getClassCarga(int peso);

    public int getClaseCarga(int peso, int distancia);
    public Costo seleccionarServicioCosto(int tipoCarga, int tipoDistancia, int peso, int distancia);
    public Costo seleccionarServicioCosto(int tipoCarga, int peso);
    public Costo getCosto(CargaDto carga);
    boolean seguro(Servicio servicio);
    boolean recoleccion(Servicio servicio);
    double calculoCostoFinal(Cotizacion cotizacion, double costo);
    double calculoCostoFinalTSMO(Cotizacion cotizacion, double costo);
    double getPesoVolumetrico(Detalle detalle);

}

