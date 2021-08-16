package com.mx.tsmo.envios.service;

import com.google.gson.Gson;
import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.cotizacion.model.domain.Detalle;
import com.mx.tsmo.cotizacion.model.dto.DetalleDto;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.envios.model.dao.EnvioCustomDao;
import com.mx.tsmo.envios.model.dao.EnvioDaoImpl;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dao.EnvioDao;
import com.mx.tsmo.envios.model.dto.EnvioDto;
import com.mx.tsmo.envios.model.dto.PostCancelacion;
import com.mx.tsmo.envios.model.dto.ResponseCancelacion;
import com.mx.tsmo.envios.model.enums.EstadoEnvio;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
@Slf4j
public class EnvioServiceImpl implements EnvioService {

    private static final String TSM = "TSM";
    private static final String TSMO = "O";
    private static final String ENVIA = "E";

    private static final String CANCELACION = "Cancelacion/Cancelar";
    private static final String COMENTARIO_CANCELACION = "Error al realizar pago para proveedor";

    // int contador;

    @Autowired
    private EnvioDao envioDao;

    private EnvioDaoImpl envioDaoImpl = new EnvioDaoImpl();

    // private EnvioCustomDao envioCustomDao;

    @Override
    public Envio guardar(Envio envio) {
        // envio.setGuiaProveedor();
        envio.setCreateAt(new Date());
        return envioDao.save(envio);

    }

    @Override
    public String generarGuia(String proveedor, String tipoPaquete) {
        log.info("Entra a servicio para generear guia");
        log.info("Proveedor: "+proveedor);
        String busquedaProveedor = "";
        String paquete = "";
        switch (proveedor) {
            case "TSMO":
                busquedaProveedor = TSM + TSMO;
                break;
            case "ENVIA":
                busquedaProveedor = TSM + ENVIA;
                break;
        }
        log.info("busquedaProveedor: "+busquedaProveedor);
        //int contador = envioDao.findAll().size();
        int contador = envioDao.findByGuiaTsmoContaining(busquedaProveedor).size();
        log.info(""+contador);
        contador++;
        log.info(""+contador);
        String guia = busquedaProveedor + tipoPaquete + this.agregarCeros(contador);

        log.info("GUIA: " + guia);

        return guia;
    }

    @Override
    public Envio actualizarEstadoPago(Envio envio, String estadoPago) {
        log.info("Entra a servicio para actualizar estado de pago");
        Envio envioBD = envioDao.findById(envio.getId()).orElse(null);
        if (envioBD == null) {
            log.error("ERROR: No existe envio con ese id");
            return null;
        }
        envioBD.setEstadoPago(estadoPago);
        envioBD.setPago(envio.getPago());
        return envioDao.save(envioBD);
    }

    @Override
    public Envio actualizarEstadoEnvio(Envio envio) {
        log.info("Entra a servicio para actualizar estado de envio");
        Envio envioBD = envioDao.findById(envio.getId()).orElse(null);
        if (envioBD == null) {
            log.error("ERROR: No existe envio con ese id");
            return null;
        }
        if (envio.getGuiaProveedor() != null) {
            log.info("Entra a colocar guia proveedor");
            envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        }
        envioBD.setEstadoEnvio(envio.getEstadoEnvio());
        log.info("Entra a guardar, desde servicio");
        return envioDao.save(envioBD);
    }

    @Override
    public ResponseCancelacion cancelarEnvio(Envio envio) {
        if (envio.getDocumentacion().getCotizacion().getRealiza().equalsIgnoreCase("ENVIA")) {
            return this.cancelacionEnvia(PostCancelacion.builder().guia(envio.getGuiaProveedor()).comentario(COMENTARIO_CANCELACION).build());
        }
        return null;
    }

    @Override
    public Envio buscarPorId(Long id) {
        return envioDao.findById(id).orElse(null);
    }

    @Override
    public Envio buscarPorGuiaTsmo(String guiaTsmo) {
        return envioDao.findByGuiaTsmo(guiaTsmo);
    }

    @Override
    public Envio actualizarEtapa(Envio envio) {
        log.info("Entra a servicio para actualizar etapa");
        Envio envioBD = envioDao.findById(envio.getId()).orElse(null);
        if (envioBD == null) {
            return null;
        }
        envioBD.setEtapa(envio.getEtapa());
        return envioDao.save(envioBD);
    }

    public int obtenerLongitud(int contador) {
        return Integer.toString(contador).length();
    }

    public String agregarCeros(int contador) {
        String valor = "";
        switch(this.obtenerLongitud(contador)) {
            case 1:
                valor = "00000" + contador;
                break;
            case 2:
                valor = "0000" + contador;
                break;
            case 3:
                valor = "000" + contador;
                break;
            case 4:
                valor = "00" + contador;
                break;
            case 5:
                valor = "0" + contador;
                break;
        }
        return valor;
    }


    public ResponseCancelacion cancelacionEnvia(PostCancelacion postCancelacion) {
        log.info("Entra para cancelar guia en envia");
        ResponseCancelacion res = new ResponseCancelacion();
        try {

            ResteasyClient client = new ResteasyClientBuilder().build();

            WebTarget target = client.target(EnviaAuth.URL.toString()+CANCELACION);

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());

            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonString = gson.toJson(postCancelacion);

            log.info("JSON Cancelacion: " + jsonString);
            Response post = solicitud.post((Entity.json(jsonString)));

            ResponseCancelacion responseJson = post.readEntity(ResponseCancelacion.class);

            res = responseJson;

            log.info("Estatus: " + post.getStatus());

            switch (post.getStatus()) {
                case 200:
                    res = responseJson;
                    break;
                default:
                    res.setMsg("Error");
                    // log.info(responseJson);
                    break;
            }
        } catch (Exception e) {
            log.info("Hubo error");
            log.info(e.getMessage());
            //res.setMsg(e.getMessage);
        }

        // log.info(res);


        //Gson respuestaJson = new Gson();
        //ResponseCancelacion cancelacionRes = respuestaJson.fromJson(res, ResponseCancelacion.class);

        return res;
    }

    public List<Envio> buscarFiltro() {
        log.info("Entra a servicio para buscar por filtro");
        //return envioDaoImpl.findByCreateAtAndEstadoEnvioAndEstadoPagoAndClienteAndDocumentacion(null, null, null, null, null);
        return null;
    }

    public List<Envio> buscarEnvioYPago(String envio, String pago) {
        log.info("Entra a servicio para buscar por envio y pago");
        return envioDao.findByEstadoEnvioAndEstadoPago(envio, pago);
        // return null;
    }

    @Override
    public List<Envio> buscarEnvioParams(Map<String, String> allParams) {
        log.info("Entra a servicios para buscar por parametros");
        try {
            return envioDao.findByParams(allParams);
        } catch (ParseException e) {
            log.error(""+e);
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public long contarPorProveedor(String proveedor) {
        log.info("Entra a servicio para contar envios por proveedor");
        return envioDao.countByGuiaTsmoContaining(proveedor);
    }

    @Override
    public long contarPorProveedorYFechaCraacion(String proveedor, Date fecha) {
        log.info("Entra a servicio para contar envios por proveedor");
        return envioDao.countByGuiaTsmoContainingAndCreateAtAfter(proveedor, fecha);
    }

    @Override
    public long contarPorProveedorFechaInicioFechaFin(String proveedor, Date inicio, Date fin) {
        log.info("Entra a servicio para contar envios por proveedor y fecha inicio y fecha fin");
        return envioDao.countByGuiaTsmoContainingAndCreateAtAfterAndCreateAtBefore(proveedor, inicio, fin);
    }

    @Override
    public long contarPorFacturaPago(String estadoPago) {
        return envioDao.countByEstadoPago(estadoPago);
    }

    @Override
    public long contarPorFacturaFechaIncioFechaFin(String estadoPago, Date inicio, Date fin) {
        return envioDao.countByEstadoPagoAndCreateAtAfterAndCreateAtBefore(estadoPago, inicio, fin);
    }

    @Override
    public long contarPorProveedorEstadoEnvio(String proveedor, String estadoEnvio) {
        return envioDao.countByGuiaTsmoContainingAndEstadoEnvio(proveedor, estadoEnvio);
    }

    @Override
    public long contarPorProveedorEstadoEnvioFechaIncioFechaFin(String proveedor, String estadoEnvio, Date inicio, Date fin) {
        return envioDao.countByGuiaTsmoContainingAndEstadoEnvioAndCreateAtAfterAndCreateAtBefore(proveedor, estadoEnvio, inicio, fin);
    }

    @Override
    public List<Object> buscarTodosClientes() {
        return envioDao.findDistinctCliente();
    }

    @Override
    public long contarPorClientes(Cliente cliente) {
        return envioDao.countByCliente(cliente);
    }

    @Override
    public long contarPorClienteFechaInicioFechaFin(Cliente cliente, Date inicio, Date fin) {
        return envioDao.countByClienteAndCreateAtAfterAndCreateAtBefore(cliente, inicio, fin);
    }

    @Override
    public long contarPorClienteEstadoEnvioFechaInicioFechaFin(Cliente cliente, String estadoEnvio, Date inicio, Date fin) {
        return envioDao.countByClienteAndEstadoEnvioAndCreateAtAfterAndCreateAtBefore(cliente, estadoEnvio, inicio, fin);
    }

    @Override
    public long contarPorClienteEstadoPagoFechaInicioFechaFin(Cliente cliente, String estadoPago, Date inicio, Date fin) {
        return envioDao.countByClienteAndEstadoPagoAndCreateAtAfterAndCreateAtBefore(cliente, estadoPago, inicio, fin);
    }

    @Override
    public Envio actualizarGuiaProveedor(Envio envio) {
        Envio envioBD = envioDao.findById(envio.getId()).orElse(null);
        if (envioBD == null) {
            return null;
        }
        envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        envioBD.setEstadoEnvio(EstadoEnvio.PENDIENTE.toString());
        return envioDao.save(envioBD);
    }

    @Override
    public List<Envio> listar() {
        return envioDao.findAll();
    }

    @Override
    public List<Envio> listarPorFechaInicioFechaFin(Date inicio, Date fin) {
        return envioDao.findByCreateAtAfterAndCreateAtBefore(inicio, fin);
    }

    @Override
    public List<Envio> buscarPorOrigenYDestino(String origen, String destino) {
        return envioDao.findByOrigenAndDestino(origen, destino);
    }

    @Override
    public List<Envio> buscarPorOrigen(String origen) {
        return envioDao.findByOrigen(origen);
    }

    @Override
    public List<Envio> buscarPorDestino(String destino) {
        return envioDao.findByDestino(destino);
    }

    @Override
    public List<Integer> listaConteoEnviosAnuales() {
        Map<String, String> fechas = this.fechas();
        fechas.forEach((inicio, fin) -> log.info("inicio: "+inicio+ " :: fin: " + fin)

        );
        return null;
    }

    @Override
    public Envio buscarPorGuiaProveedor(String guia) {
        return envioDao.findByGuiaProveedor(guia);
    }

    public Map<String, String> fechas() {
        log.info("Prueba fechas");
        Date date = new Date();

        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getLocalDate = date.toInstant().atZone(timeZone).toLocalDate();
        System.out.println(getLocalDate.getYear());
        log.info("Mes: "+getLocalDate.getMonthValue());
        int anio = getLocalDate.getYear();
        String[] mes = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        String[] dia = {"31","28","31","30","31","30","30","31","30","31","30","31","29"};
        // int anio = this.getAnio();
        Map<String, String> fechas =  new LinkedHashMap<String, String>();
        for (int i = 0; i < getLocalDate.getMonthValue(); i++) {
            if (i == 1 && this.getBisiesto(anio)) {
                //if (envioService.getBisiesto(anio)) {
                fechas.put(Integer.toString(anio) + "-" + mes[i] + "-01", Integer.toString(anio) + "-" + mes[i] + "-" + dia[12]);
                //}
            }   else {
                fechas.put(Integer.toString(anio) + "-" + mes[i] + "-01", Integer.toString(anio) + "-" + mes[i] + "-" + dia[i]);
            }
        }
        fechas.forEach((inicio, fin) -> log.info("inicio: "+inicio+ " :: fin: " + fin));
        return fechas;
    }

    public boolean getBisiesto(int anio) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.isLeapYear(anio)) {
            log.info("El año es bisiesto");
            return true;
        } else {
            log.info("El año no es bisiesto");
            return false;
        }
    }


}