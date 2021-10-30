package com.mx.tsmo.envios.service;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dto.ResponseCancelacion;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface EnvioService {

    Envio guardar(Envio envio);
    String generarGuia(String Proveedor, String tipoPaquete);
    Envio actualizarEstadoPago(Envio envio, String estadoPago);
    Envio actualizarEstadoEnvio(Envio envio);
    ResponseCancelacion cancelarEnvio(Envio envio);
    Envio buscarPorId(Long id);
    Envio buscarPorGuiaTsmo(String guiaTsmo);
    Envio actualizarEtapa(Envio envio);
    List<Envio> buscarFiltro();
    List<Envio> buscarEnvioYPago(String envio, String pago);
    List<Envio> buscarEnvioParams(Map<String, String> allParams);
    long contarPorProveedor(String proveedor);
    long contarPorProveedorYFechaCraacion(String proveedor, Date fecha);
    long contarPorProveedorFechaInicioFechaFin(String proveedor, Date inicio, Date fin);
    // Facturas
    long contarPorFacturaPago(String estadoPago);
    long contarPorFacturaFechaIncioFechaFin(String estadoPago, Date inicio, Date fin);
    // Proveedor y estado envio
    long contarPorProveedorEstadoEnvio(String proveedor, String estadoEnvio);
    long contarPorProveedorEstadoEnvioFechaIncioFechaFin(String proveedor, String estadoEnvio, Date inicio, Date fin);
    // Clientes
    List<Object> buscarTodosClientes();
    long contarPorClientes(Cliente cliente);
    long contarPorClienteFechaInicioFechaFin(Cliente cliente, Date inicio, Date fin);
    // Rastreo
    long contarPorClienteEstadoEnvioFechaInicioFechaFin(Cliente cliente, String estadoEnvio, Date inicio, Date fin);
    // Factura
    long contarPorClienteEstadoPagoFechaInicioFechaFin(Cliente cliente, String estadoPago, Date inicio, Date fin);
    Envio actualizarGuiaProveedor(Envio envio);
    List<Envio> listar();
    List<Envio> listarPorFechaInicioFechaFin(Date inicio, Date fin);
    List<Envio> buscarPorOrigenYDestino(String origen, String destino);
    List<Envio> buscarPorOrigen(String origen);
    List<Envio> buscarPorDestino(String destino);
    List<Integer> listaConteoEnviosAnuales();
    Envio buscarPorGuiaProveedor(String guia);
    String generaGuia(int sumarle);
    boolean existeEnvio(String guia);

}
