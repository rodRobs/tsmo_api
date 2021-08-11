package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.envios.model.domain.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EnvioDao extends JpaRepository<Envio, Long>, EnvioCustomDao {

    @Query("SELECT e FROM Envio e WHERE e.guiaTsmo LIKE %:guiaTsmo%")
    List<Envio> findByGuiaTsmoContaining(@Param("guiaTsmo") String guiaTsmo);
    Envio findByGuiaTsmo(String guiaTsmo);
    // Proveedor
    long countByGuiaTsmoContaining(String guiaTsmo);
    long countByGuiaTsmoContainingAndCreateAtAfter(String guiaTsmo, Date createAt);
    long countByGuiaTsmoContainingAndCreateAtAfterAndCreateAtBefore(String guiaTsmo, Date inicio, Date fin);
    // Facturas
    long countByEstadoPago(String estadoPago);
    long countByEstadoPagoAndCreateAtAfterAndCreateAtBefore(String estadoPago, Date inicio, Date fin);
    // Proveedor Rastreo
    long countByGuiaTsmoContainingAndEstadoEnvio(String guisTsmo, String estadoEnvio);
    long countByGuiaTsmoContainingAndEstadoEnvioAndCreateAtAfterAndCreateAtBefore(String guisTsmo, String estadoEnvio, Date inicio, Date fin);
    // Cliente
    // long countByCliente(Long cliente);
    // long countByClienteAndCreateAtAfterAndCreateAtBefore(Long cliente, Date inicio, Date fin);
    // Cliente
    // Envios
    @Query("SELECT DISTINCT cliente.id, cliente.nombre FROM Envio")
    List<Object> findDistinctCliente();
    long countByCliente(Cliente cliente);
    long countByClienteAndCreateAtAfterAndCreateAtBefore(Cliente cliente, Date inicio, Date fin);
    // Facturas
    long countByClienteAndEstadoPago(Cliente cliente, String estadoPago);
    long countByClienteAndEstadoPagoAndCreateAtAfterAndCreateAtBefore(Cliente cliente, String estadoPago, Date inicio, Date fin);
    // Rastreo
    long countByClienteAndEstadoEnvio(Cliente cliente, String estadoEnvio);
    long countByClienteAndEstadoEnvioAndCreateAtAfterAndCreateAtBefore(Cliente cliente, String estadoEnvio, Date inicio, Date fin);
    // Facturas y proveedor

    //List<Envio> findByCreateAtAndEstadoEnvioAndEstadoPagoAndClienteAndDocumentacion(Date createAt, String estadoEnvio, String estadoPago, String cliente, Documentacion documentacion);
    // Listar todos los envios
    List<Envio> findByCreateAtAfterAndCreateAtBefore(Date inicio, Date fin);

    @Query("select e\n" +
            " from Envio e left outer join com.mx.tsmo.documentacion.model.domain.Documentacion d \n" +
            "on e.documentacion = d.id left outer join com.mx.tsmo.cotizacion.model.domain.Cotizacion c \n" +
            "on d.cotizacion = c.id left outer join com.mx.tsmo.cotizacion.model.domain.Origen o\n" +
            "on c.origen = o.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domo\n" +
            "on o.domicilio = domo.id left outer join com.mx.tsmo.cotizacion.model.domain.Destino des\n" +
            "on c.destino = des.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domd\n" +
            "on des.domicilio = domd.id left outer join com.mx.tsmo.clientes.model.domain.Cliente cl\n" +
            "on e.cliente = cl.id\n" +
            "where domo.ciudad LIKE %:origen%\n" +
            "and domd.ciudad LIKE %:destino%")
    List<Envio> findByOrigenAndDestino(@Param("origen") String origen, @Param("destino") String destino);

    @Query("select e\n" +
            " from Envio e left outer join com.mx.tsmo.documentacion.model.domain.Documentacion d \n" +
            "on e.documentacion = d.id left outer join com.mx.tsmo.cotizacion.model.domain.Cotizacion c \n" +
            "on d.cotizacion = c.id left outer join com.mx.tsmo.cotizacion.model.domain.Origen o\n" +
            "on c.origen = o.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domo\n" +
            "on o.domicilio = domo.id left outer join com.mx.tsmo.cotizacion.model.domain.Destino des\n" +
            "on c.destino = des.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domd\n" +
            "on des.domicilio = domd.id left outer join com.mx.tsmo.clientes.model.domain.Cliente cl\n" +
            "on e.cliente = cl.id\n" +
            "where domo.ciudad LIKE %:origen%")
    List<Envio> findByOrigen(@Param("origen") String origen);

    @Query("select e\n" +
            " from Envio e left outer join com.mx.tsmo.documentacion.model.domain.Documentacion d \n" +
            "on e.documentacion = d.id left outer join com.mx.tsmo.cotizacion.model.domain.Cotizacion c \n" +
            "on d.cotizacion = c.id left outer join com.mx.tsmo.cotizacion.model.domain.Origen o\n" +
            "on c.origen = o.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domo\n" +
            "on o.domicilio = domo.id left outer join com.mx.tsmo.cotizacion.model.domain.Destino des\n" +
            "on c.destino = des.id left outer join com.mx.tsmo.cotizacion.model.domain.Domicilio domd\n" +
            "on des.domicilio = domd.id left outer join com.mx.tsmo.clientes.model.domain.Cliente cl\n" +
            "on e.cliente = cl.id\n" +
            "where domd.ciudad LIKE %:destino%")
    List<Envio> findByDestino(@Param("destino") String destino);

    // Envios Mensuales
    long countByCreateAtAfterAndCreateAtBefore(Date inicio, Date fin);
}
