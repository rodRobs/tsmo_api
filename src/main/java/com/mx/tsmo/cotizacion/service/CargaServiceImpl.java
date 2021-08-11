package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.Carga;
import com.mx.tsmo.cotizacion.model.dto.CargaDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CargaServiceImpl<T> {

    private static final double UTILIDAD = 0.3;
    private static final String URL_CP = "https://api-sepomex.hckdrk.mx/query/info_cp/07010";
    private static final String URL_GOOGLE = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String KEY = "AIzaSyC1ccYV9yg5fC_2nuT5Sr45dJvtPQxZ78Q";
    private static final String MODE = "driving";
    private static final String LANGUAGE = "es-419";
    private static final String CDMX = "Ciudad de México";
    private static final String EDOMX = "México";

    T object;

    public CargaServiceImpl(T objeto) {
        this.object = objeto;
    }

    public void obtenerTipo() {
        System.out.println("El tipo T es: "+ object.getClass().getSimpleName());
    }

    public CargaDto getCarga(double peso, double distancia, List<T> cargas) {
        log.info("Entra a servicio para regresar objeto de "+object.getClass().getSimpleName()+" por un peso: "+peso+" kg");

        if (cargas.isEmpty()) {
            log.info("Regresa lista vacia");
            return null;
        }

        log.info("Lista de " + cargas.size() + " cargas");
        /*if (object instanceof LtlForaneo) {
            for (T ltl : cargas) {
                LtlForaneo carga = (LtlForaneo) ltl;
                if (distancia > carga.getDistanciaMin() && distancia < carga.getDistanciaMax()) {
                    return getCargaDto(ltl, peso);
                }
            }
        } else {
         */
        if (cargas.size() == 1) {
            log.info("Carga: "+cargas.get(0));
            return getCargaDto(cargas.get(0), peso);
        } else {

            for (int i = 0; i < cargas.size(); i++) {
                //log.info("peso: "+peso+" cargasLocalesCarga ("+i+"): "+cargasLocales.get(i).getCarga()+ " cargaSiguiente: "+cargasLocales.get(i+1).getCarga());
                // System.out.println("i: " + i + " size():" + cargas.size() + " peso: " + peso + " cargaActiva: " + (int) ((Carga) cargas.get(i)).getCarga());
                int cargaActiva = (int) ((Carga) cargas.get(i)).getCarga();

                if (i == 0) {
                    //log.info("Entra a primer valor");
                    if (peso < cargaActiva) {
                        //log.info("Regresa este valor");
                        return getCargaDto(cargas.get(i), peso);
                    } else if (peso >= cargaActiva) {
                        if (peso < (int) ((Carga) cargas.get(i)).getCarga()) {
                            return getCargaDto(cargas.get(i + 1), peso);
                        }
                    }
                } else if (i == cargas.size() - 1) {
                    //log.info("Entra a ultimo valor");
                    return getCargaDto(cargas.get(i), peso);
                } else if (peso > cargaActiva) {
                    if (peso <= (int) ((Carga) cargas.get(i+1)).getCarga()) {
                        //log.info("Regresar este valor");
                        return getCargaDto(cargas.get(i + 1), peso);
                    }
                }
            }
        }
        return getCargaDto(cargas.get(cargas.size()-1), peso);
    }

    public CargaDto getCargaDto(T carga, double peso) {
        log.info("carga instanceof Carga: "+(carga instanceof Carga));
        if (carga instanceof Carga) {
            log.info("Carga: "+((Carga) carga).getCarga());
            log.info("cxc: "+((Carga) carga).getCxc());
            log.info("flete: "+((Carga) carga).getFlete());
            log.info("domicilio: "+((Carga) carga).getDomicilio());
            log.info("precio: "+((Carga) carga).getPrecio());
            log.info("recoleccion: "+((Carga) carga).getRecoleccion());
            log.info("utilidad: "+((Carga) carga).getPrecioFinal());
            log.info("peso: "+peso);
            return CargaDto.builder()
                    .carga(((Carga) carga).getCarga())
                    .cxc(((Carga) carga).getCxc())
                    .flete(((Carga) carga).getFlete())
                    .id(((Carga) carga).getId())
                    .domicilio(((Carga) carga).getDomicilio())
                    .precio(((Carga) carga).getPrecio())
                    .recoleccion(((Carga) carga).getRecoleccion())
                    .utilidad(((Carga) carga).getPrecioFinal())
                    .peso((float) peso)
                    .build();
        } else {
            return null;
        }
    }

    public double getUtilidad(T carga) {
        if (carga instanceof Carga)

            return (((Carga) carga).getCarga()+((Carga) carga).getFlete()+((Carga) carga).getDomicilio()+((Carga) carga).getRecoleccion())*(float) UTILIDAD;
        return 0;
    }

}
