package com.mx.tsmo.cotizacion.service;

import com.google.gson.Gson;
import com.mx.tsmo.cotizacion.model.dao.CotizacionRepository;
import com.mx.tsmo.cotizacion.model.domain.*;
import com.mx.tsmo.cotizacion.model.dto.DistanciaEntreCiudades;
import com.mx.tsmo.documentacion.model.dto.Servicios;
import com.mx.tsmo.enums.TipoCarga;
import com.mx.tsmo.enums.TipoClase;
import com.mx.tsmo.enums.TipoTraslado;
import com.mx.tsmo.domain.dtos.CajaDto;
import com.mx.tsmo.cotizacion.model.dto.CargaDto;
import com.mx.tsmo.domain.dtos.CotizacionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CotizacionServiceImpl implements CotizacionService {

    private static final String URL_CP = "https://api-sepomex.hckdrk.mx/query/info_cp/07010";
    private static final String URL_GOOGLE = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String KEY = "AIzaSyCSoaSUrUk2c_MDB_kP4bCVwG3WK6zyLTo";
    private static final String MODE = "driving";
    private static final String LANGUAGE = "es-419";

    private static final String CDMX = "Ciudad de México";
    private static final String EDOMX = "México";

    private static final double RECOLECCION = 25.056;
    private static final double SEGURO = 23.2;
    private static final double UTILIDAD = 1.3;

    //int contador = 0;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    private CargaServiceImpl cargaService;

    @Autowired
    private CargaNormalLocalService cargaNormalLocalService;
    @Autowired
    private CargaPesadaLocalService cargaPesadaLocalService;
    @Autowired
    private LtlLocalService ltlLocalService;

    @Autowired
    private EnviaService enviaService;

    @Override
    public Cotizacion guardar(Cotizacion cotizacion) {
        log.info("Entra a guardar cotizacion");
        //cotizacion.setCreateAt(new Date());
        cotizacion.setCreateAt(new Date());
        cotizacion.getDetalle().get(0).setCreateAt(new Date());
        cotizacion.getOpciones().setCreateAt(new Date());
        cotizacion.getOrigen().getDomicilio().setCreateAt(new Date());
        cotizacion.getDestino().getDomicilio().setCreateAt(new Date());
        cotizacion.getDetalle().get(0).getDimensiones().setCreateAt(new Date());
        if (cotizacion.getServicios().size() > 0) {
            for (Servicio servicio : cotizacion.getServicios()) {
                log.info("Servicio: " + servicio.getServicio());
                servicio.setCreateAt(new Date());
            }
        }
        // log.info(""+cotizacion.getOrigen().getTelefonos().get(0).toString());
        return cotizacionRepository.save(cotizacion);
    }
    @Override
    public List<Cotizacion> listar() {
        return cotizacionRepository.findAll();
    }
    @Override
    public Cotizacion buscarPorId(Long id) {
        return cotizacionRepository.findById(id).orElse(null);
    }

    /*
    @Override
    public Costo cotizacion(Cotizacion cotizacion) {
        log.info("Entramos a peticion para calcular cotizacion");
        Costo costo;
        double distancia = this.getDistancia(this.URL_Google(cotizacion));
        int tipoDistancia = this.getTipoDistancia(distancia, cotizacion);
        if (tipoDistancia == TipoTraslado.LOCAL.getValue()) {
            log.info("Cotizacion Local");
            double peso = this.getPeso(cotizacion.getDetalle().get(0));
            int tipoCarga = this.getTipoCarga(peso);
            costo = this.seleccionarServicioCosto(tipoCarga, tipoDistancia,(int) peso,(int) distancia);
            costo.setTipoServicio("1 a 4 días hábiles");
            costo.setRealiza("TSMO");
        } else {
            log.info("Cotizacion Foranea");
            costo = enviaService.calcularEnvia(cotizacion);
            costo.setCostoTotal(costo.getTotal()*1.3);
            costo.setRealiza("ENVIA");

        }
        if (costo == null) {
            return null;
        }
        return costo;
    }
    */
    /*
    public CargaDto calcular(CotizacionDto cotizacion) {
        log.info("Entra a negocio para calcular cotizacion");
        CajaDto caja = CajaDto.builder()
                .largo(cotizacion.getLargo())
                .ancho(cotizacion.getAncho())
                .alto(cotizacion.getAlto())
                .peso(cotizacion.getPeso())
                .build();
        double peso = this.getPeso(caja);
        log.info("Regresa Peso: "+peso);
        double distancia = this.getDistancia(this.URL_Google(cotizacion));
        int tipoCarga = this.getTipoCarga(peso);
        int tipoDistancia = this.getTipoDistancia(distancia, cotizacion);
        CargaDto carga = null;
        switch (this.getClaseCarga(tipoCarga, tipoDistancia)) {
            case 4: // LTL - Local
                List<LtlLocal> ltlsLocal = ltlLocalService.listar();
                cargaService = new CargaServiceImpl<LtlLocal>(new LtlLocal());
                carga = cargaService.getCarga(peso, distancia, ltlsLocal);
                break;
            case 5: // Pesado - Local
                List<CargaPesadaLocal> cargasPesadaLocal = cargaPesadaLocalService.listar();
                cargaService = new CargaServiceImpl<CargaPesadaLocal>(new CargaPesadaLocal());
                carga = cargaService.getCarga(peso, distancia, cargasPesadaLocal);
                //carga = cargaPesadaLocalService.getCarga(peso);
                break;
            case 6: // Normal - Local
                List<CargaNormalLocal> cargasNormalLocal = cargaNormalLocalService.listar();
                cargaService = new CargaServiceImpl<CargaNormalLocal>(new CargaNormalLocal());
                carga = cargaService.getCarga(peso, distancia, cargasNormalLocal);
                break;
        }
        return carga;
    }
     */

    public double getPeso(Detalle detalle) {
        log.info("Ingresa a calcular volumen");
        double pv = (detalle.getDimensiones().getAlto()*detalle.getDimensiones().getAncho()*detalle.getDimensiones().getLargo());
        pv = pv/5000;
        log.info("PV: "+pv);
        log.info("PB: "+detalle.getDimensiones().getPeso());
        return (pv >= detalle.getDimensiones().getPeso() ? pv : detalle.getDimensiones().getPeso());
    }



    public Double getPeso(CajaDto caja) {
        log.info("Ingresa a calcula peso volumetrico");
        double pv = (caja.getAlto()*caja.getAncho()*caja.getLargo());
        pv = pv/5000;
        log.info("PV: "+pv);
        log.info("PB: "+caja.getPeso());
        return (pv >= caja.getPeso()) ? pv : caja.getPeso();
    }
/*
    public void webServicePrueba() throws IOException {
        log.info("Entra a prueba de web service");
        Http peticion = new Http();
        String response = peticion.GET(URL_CP);
        Gson gson = new Gson();
        ResponseCpDto[] cpArray = gson.fromJson(response, ResponseCpDto[].class);
        for (ResponseCpDto cp : cpArray) {
            log.info(cp.getResponse().getAsentamiento());
        }

    }
 */

    public String URL_Google(Cotizacion cotizacion) {
        log.info("Entra a negocio para armar url de api distance Google");
        return URL_GOOGLE +
                "?origins=" + cotizacion.getOrigen().getDomicilio().getCiudad().replace(" ", "+") + "," + cotizacion.getOrigen().getDomicilio().getEstado().replace(" ","+") + "," + cotizacion.getOrigen().getDomicilio().getPais().replace(" ","+")+
                "&destinations=" + cotizacion.getDestino().getDomicilio().getCiudad().replace(" ", "+") + "," + cotizacion.getDestino().getDomicilio().getEstado().replace(" ", "+") + "," + cotizacion.getDestino().getDomicilio().getPais().replace(" ", "+")+
                "&mode="+MODE+
                "&language="+LANGUAGE+
                "&key="+KEY;
    }

    public String URL_Google_Cotizacion(Cotizacion cotizacion) {
        log.info("Entra a negocio armar url para la cotizacion");
        return URL_GOOGLE +
                "?origins=" + cotizacion.getOrigen().getDomicilio().getCodigoPostal().replace(" ", "+") + "+Mexico" +
                "&destinations=" + cotizacion.getDestino().getDomicilio().getCodigoPostal().replace(" ", "+") + "+Mexico" +
                "&mode="+MODE+
                "&language="+LANGUAGE+
                "&key="+KEY;
    }

    /*
    public String URL_Google(CotizacionDto cotizacion) {
        log.info("Entra a negocio armar url de las ciudades: "
                + cotizacion.getColoniaOrigen() + " "
                + cotizacion.getDelegacionOrigen() + " "
                + cotizacion.getEstadoOrigen() +" & "
                + cotizacion.getColoniaDestino() + " "
                + cotizacion.getDelegacionDestino() + " "
                + cotizacion.getEstadoDestino());

        return URL_GOOGLE +
                "?origins=" + cotizacion.getColoniaOrigen().replace(" ", "+") + "," + cotizacion.getDelegacionOrigen().replace(" ","+") + "," + cotizacion.getEstadoOrigen().replace(" ","+")+
                "&destinations=" + cotizacion.getColoniaDestino().replace(" ", "+") + "," + cotizacion.getDelegacionDestino().replace(" ", "+") + "," + cotizacion.getEstadoDestino().replace(" ", "+")+
                "&mode="+MODE+
                "&language="+LANGUAGE+
                "&key="+KEY;
    }
*/
    public Double getDistancia(String url) {
        //log.info(String.valueOf(contador++));
        log.info("Entra a servicio para calcular distancia");
        log.info(url);
        GoogleDistanceHttp peticion = new GoogleDistanceHttp();
        String response = peticion.GET(url);
        Gson gson = new Gson();
        DistanciaEntreCiudades distanciaEntreCiudades = gson.fromJson(response, DistanciaEntreCiudades.class);
        System.out.println(gson.toString());
        return (distanciaEntreCiudades.getRows().get(0).getElements().get(0).getDistance().getValue())/1000;
    }

    public int getTipoDistancia(double distancia, Cotizacion cotizacion) {
        if (distancia < 60) {
            System.out.println("Ciudad de México: "+cotizacion.getOrigen().getDomicilio().getEstado() +" CDMX: "+ CDMX+ " == "+(cotizacion.getOrigen().getDomicilio().getEstado().equalsIgnoreCase(CDMX)));
            if (cotizacion.getOrigen().getDomicilio().getEstado().equalsIgnoreCase(CDMX) || cotizacion.getOrigen().getDomicilio().getEstado().equalsIgnoreCase(EDOMX)) {
                if (cotizacion.getDestino().getDomicilio().getEstado().equalsIgnoreCase(CDMX) || cotizacion.getDestino().getDomicilio().getEstado().equalsIgnoreCase(EDOMX)) {
                    return TipoTraslado.LOCAL.getValue();
                }
            }
        }
        return TipoTraslado.FORANEO.getValue();
    }

    public int getTipoDistancia(Double distancia, CotizacionDto cotizacion) {
        if (distancia < 60) {
            System.out.println("Ciudad de México: "+cotizacion.getEstadoOrigen() +" CDMX: "+ CDMX+ " == "+(cotizacion.getEstadoOrigen().equalsIgnoreCase(CDMX)));
            if (cotizacion.getEstadoOrigen().equalsIgnoreCase(CDMX) || cotizacion.getEstadoOrigen().equalsIgnoreCase(EDOMX)) {
                if (cotizacion.getEstadoDestino().equalsIgnoreCase(CDMX) || cotizacion.getEstadoDestino().equalsIgnoreCase(EDOMX)) {
                    return TipoTraslado.LOCAL.getValue();
                }
            }
        }
        return TipoTraslado.FORANEO.getValue();
    }

    public int getTipoCarga(Double peso) {
        System.out.println("Peso: "+peso);
        System.out.println("Peso >= 200: "+(peso>=200));
        System.out.println("Peso >= 60: "+(peso>=60));
        if (peso >= 200.00) {
            log.info("Carga LTL");
            return TipoCarga.LTL.getValue();
        } else if (peso >= 60.00) {
            log.info("Carga PESADO");
            return TipoCarga.PESADO.getValue();
        } else {
            log.info("Carga NORMAL");
            return TipoCarga.NORMAL.getValue();
        }
    }

    public int getClaseCarga(int peso, int distancia) {
        log.info("Peso: "+peso+" Distancia: "+distancia);
        /*if (peso == TipoCarga.LTL.getValue() && distancia == TipoTraslado.FORANEO.getValue()) {
            return TipoClase.CARGALTLFORANEO.getValue();
        } else if (peso == TipoCarga.PESADO.getValue() && distancia == TipoTraslado.FORANEO.getValue()) {
            return TipoClase.CARGAPESADAFORANEO.getValue();
        } else if (peso == TipoCarga.NORMAL.getValue() && distancia == TipoTraslado.FORANEO.getValue()) {
            return TipoClase.CARGANORMALFORANEO.getValue();
        } else*/ if (peso == TipoCarga.LTL.getValue() && distancia == TipoTraslado.LOCAL.getValue()) {
            return TipoClase.CARGALTLLOCAL.getValue();
        } else if (peso == TipoCarga.PESADO.getValue() && distancia == TipoTraslado.LOCAL.getValue()) {
            return TipoClase.CARGAPESADALOCAL.getValue();
        } else if (peso == TipoCarga.NORMAL.getValue() && distancia == TipoTraslado.LOCAL.getValue()) {
            return TipoClase.CARGANORMALLOCAL.getValue();
        }
        return 0;
    }

    public int getClassCarga(int peso) {
        if (peso == TipoCarga.LTL.getValue()) {
            return TipoClase.CARGALTLLOCAL.getValue();
        } else if (peso == TipoCarga.PESADO.getValue()) {
            return TipoClase.CARGAPESADALOCAL.getValue();
        } else if (peso == TipoCarga.NORMAL.getValue()) {
            return TipoClase.CARGANORMALLOCAL.getValue();
        }
        return 0;
    }

    public CargaDto cargaDtoCliente(CargaDto carga) {
        return CargaDto.builder()
                .peso(carga.getPeso())
                .precio(carga.getPrecio())
                .build();
    }

    public Costo seleccionarServicioCosto(int tipoCarga, int tipoDistancia, int peso, int distancia) {
        CargaDto carga = null;
        switch (this.getClaseCarga(tipoCarga, tipoDistancia)) {
            case 4: // LTL - Local
                List<LtlLocal> ltlsLocal = ltlLocalService.listar();
                cargaService = new CargaServiceImpl<LtlLocal>(new LtlLocal());
                carga = cargaService.getCarga(peso, distancia, ltlsLocal);
                break;
            case 5: // Pesado - Local
                List<CargaPesadaLocal> cargasPesadaLocal = cargaPesadaLocalService.listar();
                cargaService = new CargaServiceImpl<CargaPesadaLocal>(new CargaPesadaLocal());
                carga = cargaService.getCarga(peso, distancia, cargasPesadaLocal);
                //carga = cargaPesadaLocalService.getCarga(peso);
                break;
            case 6: // Normal - Local
                List<CargaNormalLocal> cargasNormalLocal = cargaNormalLocalService.listar();
                cargaService = new CargaServiceImpl<CargaNormalLocal>(new CargaNormalLocal());
                carga = cargaService.getCarga(peso, distancia, cargasNormalLocal);
                break;
        }
        return getCosto(carga);
    }

    public Costo seleccionarServicioCosto(int tipoCarga, int peso) {
        CargaDto carga = null;
        switch (this.getClassCarga(tipoCarga)) {
            case 4: // LTL - Local
                List<LtlLocal> ltlsLocal = ltlLocalService.listar();
                cargaService = new CargaServiceImpl<LtlLocal>(new LtlLocal());

                carga = cargaService.getCarga(peso, 0, ltlsLocal);
                break;
            case 5: // Pesado - Local
                List<CargaPesadaLocal> cargasPesadaLocal = cargaPesadaLocalService.listar();
                cargaService = new CargaServiceImpl<CargaPesadaLocal>(new CargaPesadaLocal());
                carga = cargaService.getCarga(peso, 0, cargasPesadaLocal);
                //carga = cargaPesadaLocalService.getCarga(peso);
                break;
            case 6: // Normal - Local
                List<CargaNormalLocal> cargasNormalLocal = cargaNormalLocalService.listar();
                cargaService = new CargaServiceImpl<CargaNormalLocal>(new CargaNormalLocal());
                carga = cargaService.getCarga(peso, 0, cargasNormalLocal);
                break;
        }
        return getCosto(carga);
    }

    /*
    public CargaDto seleccionarServicio(int tipoCarga, int tipoDistancia, int peso, int distancia) {
        //log.info("Class Carga: "+classCarga);
        CargaDto carga = null;
        switch (cotizacionBusiness.getClaseCarga(tipoCarga, tipoDistancia)) {
            /*case 1: // LTL - Foraneo
                List<LtlForaneo> ltlsForaneo = ltlForaneoService.listar();
                cargaService = new CargaServiceImpl<LtlForaneo>(new LtlForaneo());
                carga = cargaService.getCarga(peso, distancia, ltlsForaneo);
                break;
            case 2: // Pesado - Foraneo
                List<CargaPesadaForaneo> cargasPesadaForaneo = cargaPesadaForaneoService.listar();
                cargaService = new CargaServiceImpl<CargaPesadaForaneo>(new CargaPesadaForaneo());
                carga = cargaService.getCarga(peso, distancia, cargasPesadaForaneo);
                break;
            case 3: // Normal - Foraneo
                List<CargaNormalForaneo> cargasNormalForaneo = cargaNormalForaneoService.listar();
                cargaService = new CargaServiceImpl<CargaNormalForaneo>(new CargaNormalForaneo());
                carga = cargaService.getCarga(peso, distancia, cargasNormalForaneo);
                break;

            case 4: // LTL - Local
                List<LtlLocal> ltlsLocal = ltlLocalService.listar();
                cargaService = new CargaServiceImpl<LtlLocal>(new LtlLocal());
                carga = cargaService.getCarga(peso, distancia, ltlsLocal);
                break;
            case 5: // Pesado - Local
                List<CargaPesadaLocal> cargasPesadaLocal = cargaPesadaLocalService.listar();
                cargaService = new CargaServiceImpl<CargaPesadaLocal>(new CargaPesadaLocal());
                carga = cargaService.getCarga(peso, distancia, cargasPesadaLocal);
                //carga = cargaPesadaLocalService.getCarga(peso);
                break;
            case 6: // Normal - Local
                List<CargaNormalLocal> cargasNormalLocal = cargaNormalLocalService.listar();
                cargaService = new CargaServiceImpl<CargaNormalLocal>(new CargaNormalLocal());
                carga = cargaService.getCarga(peso, distancia, cargasNormalLocal);
                break;
        }
        return carga;
    }
    */
    public Costo getCosto(CargaDto carga) {
        return
                Costo.builder()
                .flete((carga.getFlete() == null) ? null : carga.getFlete())
                .peso(carga.getPeso())
                .combustible(carga.getCxc())
                .costoTotal(carga.getUtilidad())
                .build();
    }

    @Override
    public boolean seguro(Servicio servicio) {
        return servicio.getServicio().equalsIgnoreCase("SEG");
    }

    @Override
    public boolean recoleccion(Servicio servicio) {
        return servicio.getServicio().equalsIgnoreCase("RDO");
    }

    @Override
    public double calculoCostoFinal(Cotizacion cotizacion, double costo) {
        boolean seguro = false;
        boolean recoleccion = false;
        if (cotizacion.getServicios().size() > 0) {
            for (Servicio servicio : cotizacion.getServicios()) {
               switch(servicio.getServicio()) {
                   case "SEG":
                       seguro = true;
                       costo = costo - SEGURO;
                       break;
                   case "RDO":
                       recoleccion = true;
                       costo = costo - RECOLECCION;
                       break;
               }
            }
        }
        costo = costo * UTILIDAD;
        if (seguro)
            costo = costo + SEGURO;
        if (recoleccion)
            costo = costo + RECOLECCION;
        return costo;
    }

}
