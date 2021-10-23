package com.mx.tsmo.envios.controller;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.correos.service.NotificacionService;
import com.mx.tsmo.cotizacion.service.CostoService;
import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.documento.domain.dto.EnvioDoc;
import com.mx.tsmo.documento.service.DocumentoService;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.EnviosGranel;
import com.mx.tsmo.envios.model.domain.Rastreo;
import com.mx.tsmo.envios.model.dto.CiudadesDto;
import com.mx.tsmo.envios.model.dto.EnvioDto;
import com.mx.tsmo.envios.model.enums.EstadoEnvio;
import com.mx.tsmo.envios.model.enums.EstadoPago;
import com.mx.tsmo.envios.service.EnvioService;
import com.mx.tsmo.envios.service.ExportarExcelEnvioService;
import com.mx.tsmo.envios.service.RastreoService;
import com.mx.tsmo.security.entity.Rol;
import com.mx.tsmo.security.entity.Usuario;
import com.mx.tsmo.security.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PersistentObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("envios")
@CrossOrigin("*")
@Slf4j
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private RastreoService rastreoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private CostoService costoService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ExportarExcelEnvioService exportarExcelEnvioService;

    private static final String RECOLECCION = "Recolección";
    // private static final String DESCRIPCION_RECOLECCION = "En camino a Recolección";
    private static final String DESCRIPCION_RECOLECCION = "Se ha documentado el pedido para envio";
    private static final String CANCELADO = "Cancelado";
    private static final String DESCRIPCION_CANCELADO = "Hubo error al generar pedido de envio";
    // private Object Optional;

    /*
    * Guardar el envio
    * */
    @PostMapping("/{cliente}/{documentacion}/{proveedor}/{tipoPaquete}/{usuario}")
    public ResponseEntity<Envio> guardar(
            @RequestBody Envio envio, @PathVariable("cliente") Long cliente,
            @PathVariable("documentacion") Long documentacion,
            @PathVariable("proveedor") String proveedor,
            @PathVariable("tipoPaquete") String tipoPaquete,
            @PathVariable("usuario") String usuario) {
        log.info("Entra a servicio para guardar envio");
        envio.setGuiaTsmo(envioService.generarGuia(proveedor, tipoPaquete));
        envio.setCliente(Cliente.builder().id(cliente).build());
        envio.setDocumentacion(Documentacion.builder().id(documentacion).build());
        envio.setEstadoPago(EstadoPago.PENDIENTE.toString());
        if (proveedor.equalsIgnoreCase("TSMO")) {
            log.info("Entra a guardar para TSMO");
            envio.setEstadoEnvio(EstadoEnvio.PENDIENTE.toString());
        }
        if (usuario != null)
            envio.setUsuario(usuarioService.getByNombreUsuario(usuario));
        Envio envioBD = envioService.guardar(envio);
        if (envio == null) {
            log.error("ERROR: No se pudo guardar el envio");
            return new ResponseEntity("ERROR: No se pudo guardar el envío", HttpStatus.BAD_REQUEST);
        }
        log.info("Guarda el envio exitosamente");
        return ResponseEntity.ok(envioBD);
    }

    @PostMapping("/actualizarPago/{estadoPago}")
    public ResponseEntity<Envio> actualizarPago(@RequestBody Envio envio, @PathVariable("estadoPago") String estadoPago) {
        log.info("Entra a servicio para actualizar estado de pago");
        log.info(envio.toString());
        log.info("Pago:");
        Envio envioBD = envioService.actualizarEstadoPago(envio, estadoPago);
        if (envioBD == null) {
            return new ResponseEntity("ERROR: El objeto que se envio no existe en la Base de Datos", HttpStatus.BAD_REQUEST);
        }
        if (envioBD.getEstadoPago().equalsIgnoreCase(EstadoPago.RECHAZADO.toString())) {
            log.info("Rechazado");
            if (envioService.cancelarEnvio(envioBD)  != null) {
                envioBD.setEstadoEnvio(EstadoEnvio.CANCELADO.toString());
            } else {
                log.error("ERROR: Error al cancelar con el proveedor", HttpStatus.CONFLICT);
            }
        } else if (envioBD.getEstadoPago().equalsIgnoreCase((EstadoPago.APROBADO.toString()))) {
            log.info("Aprobado");
            envioBD.setEstadoEnvio(EstadoEnvio.PENDIENTE.toString());
        } else if (envioBD.getEstadoPago().equalsIgnoreCase(EstadoPago.PENDIENTE.toString())) {
            log.info("Pendiente");
            if (envioService.cancelarEnvio(envioBD) != null) {
                envioBD.setEstadoEnvio(EstadoEnvio.CANCELADO.toString());
            } else {
                log.error("ERROR: Error al cancelar con el proveedor", HttpStatus.CONFLICT);
            }
        }
        log.info("Va a actualizar estado de envio");
        /*
        envioBD = envioService.actualizarEstadoEnvio(envioBD);
        if (envioBD == null) {
            log.error("ERROR");
            return new ResponseEntity("ERROR: Al actualizar el estado del envio", HttpStatus.BAD_REQUEST);
        }
         */
        if (envio.getGuiaProveedor() != null) {
            log.info("Guia proveedor: "+envio.getGuiaProveedor());
            envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        }
        // envioBD.setEstadoEnvio(estadoPago);
        if (this.guardarPrimerRastreo(envioBD, estadoPago) == null) {
            log.error("Hubo error al guardar el primer rastreo");
        }
        return ResponseEntity.ok(envioService.actualizarEstadoEnvio(envioBD));
    }

    @PostMapping("/actualizarEstadoEnvio/{estadoEnvio}")
    public ResponseEntity<Envio> actualizarEstadoEnvio(@RequestBody Envio envio, @PathVariable("estadoEnvio") String estadoEnvio) {
        log.info("Entra a servicio para actualizar estado del envio");
        log.info("Estado");
        Envio envioBD;
        try {
            envioBD = envioService.buscarPorId(envio.getId());
        } catch (NullPointerException e) {
            log.error("ERROR: "+ e.getMessage());
            return new ResponseEntity("ERROR: El envio no cuenta con id", HttpStatus.BAD_REQUEST);
        }
        if (envioBD == null) {
            return new ResponseEntity("ERROR: No se encuentra registro en Base de Datos", HttpStatus.BAD_REQUEST);
        }
        if (envio.getGuiaProveedor() != null) {
            log.info("Guia proveedor: "+envio.getGuiaProveedor());
            envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        }
        envioBD.setEstadoEnvio(estadoEnvio);
        if (this.guardarPrimerRastreo(envioBD, estadoEnvio) == null) {
            log.error("Hubo error al guardar el primer rastreo");
        }
        return ResponseEntity.ok(envioService.actualizarEstadoEnvio(envioBD));
    }

    @GetMapping("/buscar/{guia}")
    public ResponseEntity<Envio> buscarPorGuia(@PathVariable("guia") String guia) {
        log.info("Entra a controlador para buscar envio por guia: " + guia);
        Envio envioBD = envioService.buscarPorGuiaTsmo(guia);
        if (envioBD == null) {
            log.error("ERROR: No existe envio con ese numero de guia");
            return new ResponseEntity("ERROR: No existe envío con ese número de guía", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioBD);
    }

    public Rastreo guardarPrimerRastreo(Envio envio, String estadoEnvio) {
        log.info("Entra a servicio para rastreo");
        log.info("Envio: "+envio.getUsuario());
        Rastreo rastreoBD = new Rastreo();
        switch(estadoEnvio) {
            case "APROBADO":
                rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(RECOLECCION).createAt(new Date()).descripcion(DESCRIPCION_RECOLECCION).etapa(0).envio(envio).build());
                break;
            case "PENDIENTE":
                log.info("Entra a switch pendiente");
                if (envio.getUsuario() != null) {
                    log.info("El envio es solicitado para un usuario registrado del Portal");
                    rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(RECOLECCION).createAt(new Date()).descripcion(DESCRIPCION_RECOLECCION).etapa(0).envio(envio).build());
                    /*for (Rol rol : envio.getUsuario().getRoles()) {
                        switch (rol.getRolNombre()) {
                            case ROL_TSMO:
                                rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(RECOLECCION).createAt(new Date()).descripcion(DESCRIPCION_RECOLECCION).etapa(0).envio(envio).build());
                                break;
                            case ROL_CLIENTE:
                                rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(RECOLECCION).createAt(new Date()).descripcion(DESCRIPCION_RECOLECCION).etapa(0).envio(envio).build());
                                break;
                        }
                    }*/
                } else {
                    rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(CANCELADO).createAt(new Date()).descripcion(DESCRIPCION_CANCELADO).etapa(0).envio(envio).build());
                }
                break;
            case "RECHAZADO":
                rastreoBD = rastreoService.guardar(Rastreo.builder().nombre(CANCELADO).createAt(new Date()).descripcion(DESCRIPCION_CANCELADO).etapa(0).envio(envio).build());
                break;
        }
        return rastreoBD;
    }

    /*
    * SERVICIO PARA BUSCAR ENVIOS POR FILTROS
    * Servicio REST para buscar envios por filtro de busqueda
    * @author Rodrigo Robles
    * @param allParams Este parametro de entrada contiene todas las opciones de parametros para poder realizar la búsqueda con filtro
    *                   Estos paramatros pueden ser:
    *                   @ periodo : fecha inicial
    *                   @ periodo : fecha final
    *                   @ estadoEnvio: est
    * */
    @GetMapping("/buscar/filtros")
    public ResponseEntity<List<Envio>> buscarEnvios(@RequestParam Map<String, String> allParams) {
        log.info("Entra a controlador envio para buscar filtros");
        List<Envio> envios = envioService.buscarEnvioParams(allParams);
        if (envios.size() == 0) {
            return ResponseEntity.status(200).body(envios);
        }
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/prueba/criteria")
    public ResponseEntity<List<Envio>> pruebaCriteria(@RequestParam Map<String, String> allParams) {
        log.info("Entra a servicio para prueba Criteria");
        // return ResponseEntity.ok(envioService.buscarFiltro());
        return ResponseEntity.ok(envioService.buscarEnvioParams(allParams));
        //return ResponseEntity.ok(envioService.buscarEnvioYPago("PENDIENTE",""));
    }

    @GetMapping("/prueba/count/{proveedor}")
    public ResponseEntity<Long> pruebaCount(@PathVariable("proveedor") String proveedor) {
        log.info("Entra a controlador para contar");

        return ResponseEntity.ok(envioService.contarPorProveedor(proveedor));
    }

    @GetMapping("/prueba/count/{proveedor}/{fecha}")
    public ResponseEntity<Long> pruebaCountFecha(@PathVariable("proveedor") String proveedor, @PathVariable("fecha") String fecha) {
        log.info("Entra a controlador para contar");
        Date fechaCreateAt;
        try {
            fechaCreateAt = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha a objeto Date", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(envioService.contarPorProveedorYFechaCraacion(proveedor, fechaCreateAt));
    }

    @GetMapping("/count/proveedor/periodo/{proveedor}/{inicio}/{fin}")
    public ResponseEntity<Long> pruebaCountFechaInicioFin(@PathVariable("proveedor") String proveedor, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para contar por fecha inicio y fin y proveedor");
        Date fechaInicio;
        Date fechaFin;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorProveedorFechaInicioFechaFin(proveedor, fechaInicio, fechaFin));
    }

    @GetMapping("/count/factura/{estadoPago}")
    public ResponseEntity<Long> contadorFacturas(@PathVariable("estadoPago") String estadoPago) {
        log.info("Entra a controlador para regresar contador facturas");
        return ResponseEntity.ok(envioService.contarPorFacturaPago(estadoPago));
    }

    @GetMapping("/count/factura/periodo/{estadoPago}/{inicio}/{fin}")
    public ResponseEntity<Long> conteoPeriodoFactura(@PathVariable("estadoPago") String estadoPago, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para regresar contador de facturas por periodo de tiempo");
        Date fechaInicio;
        Date fechaFin;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorFacturaFechaIncioFechaFin(estadoPago, fechaInicio, fechaFin));
    }

    @GetMapping("/count/proveedor/{proveedor}/envio/{estadoEnvio}")
    public ResponseEntity<Long> conteoProveedorEstadosEnvios(@PathVariable("proveedor") String proveedor, @PathVariable("estadoEnvio") String estadoEnvio) {
        log.info("Entra a controlador para regresar conteo de envios por proveedor y su estado de envio");
        return ResponseEntity.ok(envioService.contarPorProveedorEstadoEnvio(proveedor, estadoEnvio));
    }

    @GetMapping("/count/proveedor/{proveedor}/envio/{estadoEnvio}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoProveedorEstadoEnvioPeriodo(@PathVariable("proveedor") String proveedor, @PathVariable("estadoEnvio") String estadoEnvio, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para regresar coneto de envio por proveedor, estado de envio y periodo");
        Date fechaInicio;
        Date fechaFin;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorProveedorEstadoEnvioFechaIncioFechaFin(proveedor, estadoEnvio, fechaInicio, fechaFin));
    }

    @GetMapping("/count/cliente/{cliente}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoClientePeriodo(@PathVariable("cliente") Long cliente, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para regresar conteo de cada uno de los cliente");
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorClienteFechaInicioFechaFin(Cliente.builder().id(cliente).build(), fechaInicio, fechaFinal));
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Object>> buscarClientes() {
        log.info("Entra a controlador para buscar clientes");
        return ResponseEntity.ok(envioService.buscarTodosClientes());
    }

    @GetMapping("/count/cliente/{cliente}/envio/{estadoEnvio}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoClienteEstadoEnvioPeriodo(@PathVariable("cliente") Long cliente, @PathVariable("estadoEnvio") String estadoEnvio, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para buscar envios por cliente, estadoEnvio y periodo de fecha");
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorClienteEstadoEnvioFechaInicioFechaFin(Cliente.builder().id(cliente).build(), estadoEnvio, fechaInicio, fechaFinal));
    }

    @GetMapping("/count/cliente/{cliente}/factura/{estadoPago}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoClientePagoPeriodo(@PathVariable("cliente") Long cliente, @PathVariable("estadoPago") String estadoPago, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para buscar envios por cliente, estadoPago y periodo de fecha");
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorClienteEstadoPagoFechaInicioFechaFin(Cliente.builder().id(cliente).build(), estadoPago, fechaInicio, fechaFinal));
    }

    /*
    * SERVICIO PARA CONTAR CLIENTE, ESTADO ENVIO CON PERIODO
    * Servicio REST para contar envios por cliente con estado de envio y periodo de fecha
    * @author Rodrigo Robles
    * @param usuario El parametro cliente es de tipo String, para poder realizar busqueda de usuario y de ahi asignar cliente para realizar la busqueda
    * @param estadoEnvio El parametro estadoEnvio es de tipo String, para poder asignar en la busqueda
    * @param inicio El parametro inicio es de tipo String, para despues convertirlo a tipo Date para realizar busqueda por fecha de inicio
    * @param fin El parametro fin es de tipo String, para despues convertirlo a tipo Date para realizar busqueda por fecha de fin
    * */
    @GetMapping("count/cliente/{usuario}/edoEnv/{estadoEnvio}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoClienteEstadoEnvioPeriodo(@PathVariable("usuario") String usuario, @PathVariable("estadoEnvio") String estadoEnvio, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para buscar envios por usuario (cliente), estadoEnvio y periodo de fecha");
        Usuario cliente = usuarioService.getByNombreUsuario(usuario);
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorClienteEstadoEnvioFechaInicioFechaFin(Cliente.builder().id(cliente.getId()).build(), estadoEnvio, fechaInicio, fechaFinal));
    }

    /*
    * SERVICIO PARA CONTAR CLIENTE, FACTURA CON PERIODO
    * Servicio REST para contar envios por cliente con estado de pago y periodo de fecha
    * @author Rodrigo Robles
    * @param usuario El parametro cliente es de tipo String, para poder realizar busqueda de usuario y de ahi asignar cliente para realizar la busqueda
    * @param estadoPago El parametro estadoPago es de tipo String, para poder asignar en la busqueda
    * @param inicio El parametro inicio es de tipo String, para despues convertirlo a tipo Date para realizar busqueda por fecha de inicio
    * @param fin El parametro fin es de tipo String, para despues convertirlo a tipo Date para realizar busqueda por fecha de fin
    */
    @GetMapping("count/cliente/{usuario}/edoPgo/{estadoPago}/periodo/{inicio}/{fin}")
    public ResponseEntity<Long> conteoClienteEstadoPagoPeriodo(@PathVariable("usuario") String usuario, @PathVariable("estadoPago") String estadoPago, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para buscar envios por usuario (cliente), estadoPago y periodo de fecha");
        Usuario cliente = usuarioService.getByNombreUsuario(usuario);
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioService.contarPorClienteEstadoPagoFechaInicioFechaFin(Cliente.builder().id(cliente.getId()).build(), estadoPago, fechaInicio, fechaFinal));
    }


    /*
    * SERVICIO CLIENTE TSMO
    * Servicio REST para guardar envio para los clientes TSMO
    * @author Rodrigo Robles
    * @param usuario El parametro usuario es de tipo String, para poder realizar busqueda del usuario y asi obtener cliente registrado de TSMO
    * @param envio Objeto de envio obtenido desde formulario
    */
    @PostMapping("/guardar/cliente/{usuario}/{documentacion}/{proveedor}/{tipoPaquete}")
    public ResponseEntity<Envio> guardarParaClienteTSMO(
            @RequestBody Envio envio,
            @PathVariable("usuario") String usuario,
            @PathVariable("documentacion") Long documentacion,
            @PathVariable("proveedor") String proveedor,
            @PathVariable("tipoPaquete") String tipoPaquete) {
        log.info("Entra a controlador envios para guardar envio para usuarios TSMO");
        Usuario usuarioDB = usuarioService.getByNombreUsuario(usuario);
        if (usuarioDB == null) {
            log.error("No existe usuario (cliente) con ese nombre de usuario");
            return new ResponseEntity("ERROR: No se pudo encontrar usuario que quiere registrar la contratacion de envio", HttpStatus.BAD_REQUEST);
        }
        log.info("Despues de buscar usuario");
        log.info("Envio: "+envio.toString());
        log.info("Cliente: "+envio.getCliente().getId());
        envio.setGuiaTsmo(envioService.generarGuia(proveedor, tipoPaquete));
        //envio.setCliente(Cliente.builder().id(cliente).build());
        envio.setDocumentacion(Documentacion.builder().id(documentacion).build());
        envio.setEstadoPago(EstadoPago.PENDIENTE.toString());
        envio.setUsuario(new Usuario(usuarioDB.getId()));
        envio.setCliente(usuarioDB.getCliente());
        if (proveedor.equalsIgnoreCase("TSMO")) {
            log.info("Entra a guardar para TSMO");
            envio.setEstadoEnvio(EstadoEnvio.PENDIENTE.toString());
        }
        Envio envioBD = null;
        log.info("Antes de guardar el envio");
        try {
            envioBD = envioService.guardar(envio);
        } catch (PersistentObjectException poe) {
            log.error("ERROR: "+poe.getMessage());
        }


        if (envio == null) {
            log.error("ERROR: No se pudo guardar el envio");
            return new ResponseEntity("ERROR: No se pudo guardar el envío", HttpStatus.BAD_REQUEST);
        }
        log.info("Guarda el envio exitosamente");
        if (envio.getGuiaProveedor() != null) {
            log.info("Guia proveedor: "+envio.getGuiaProveedor());
            envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        }
        // envioBD.setEstadoEnvio(estadoPago);
        if (this.guardarPrimerRastreo(envioBD, EstadoPago.PENDIENTE.toString()) == null) {
            log.error("Hubo error al guardar el primer rastreo");
        }
        return ResponseEntity.ok(envioBD);
    }

    /* *
    * SERVICIO OCASIONAL
    * Servicio REST para guardar envio para clientes ocasionales
    * @author Rodrigo Robles
    * @param cliente El parametro cliente es de tipo Long, es el id de cliente obtenido desde la vista para asignar cliente al envio
    * @param envio El Objeto envio que se ha llenado desde formulario de la vista
    * */
    @PostMapping("/guardar/{cliente}")
    public ResponseEntity<Envio> guardarParaClienteOcasional(@RequestBody Envio envio, @PathVariable("cliente") Long cliente) {
        log.info("Entra a controlador envios para guardar envio para clientes");
        return null;
    }

    /* *
    * SERVICIO EMPLEADO TSMO
    * Servicio REST para guardar envio para el personal de TSMO
    * @author Rodrigo Robles
    * @param usuario El parametro usuario es de tipo Long, es el id de usuario obtenido desde la vista para asignar usuario al envio
    * @param envio El Objeto envio que se ha llenado desde formulario de la vista
    * */
    @PostMapping("/guardar/usuario/{documentacion}/{proveedor}/{tipoPaquete}/{usuario}")
    //@GetMapping("/guardar/usuario/{documentacion}/{proveedor}/{tipoPaquete}/{usuario}")
    //public ResponseEntity<Envio> guardarParaPersonalTSMO(@PathVariable("documentacion") Long documentacion, @PathVariable("proveedor") String proveedor, @PathVariable("tipoPaquete") Long tipoPaquete, @PathVariable("usuario") String usuario) {
    public ResponseEntity<Envio> guardarParaPersonalTSMO(
            @RequestBody Envio envio,
            @PathVariable("documentacion") Long documentacion,
            @PathVariable("proveedor") String proveedor,
            @PathVariable("tipoPaquete") String tipoPaquete,
            @PathVariable("usuario") String usuario) {
        log.info("Entra a controlador envios para guardar envio para usuarios");

        Usuario usuarioDB = usuarioService.getByNombreUsuario(usuario);
        if (usuarioDB == null) {
            log.error("No existe usuario (personal TSMO) con ese nombre de usuario");
            return new ResponseEntity("ERROR: No se pudo encontrar usuario que quiere registrar la contratacion de envio", HttpStatus.BAD_REQUEST);
        }
        log.info("Despues de buscar usuario");
        log.info("Envio: "+envio.toString());
        log.info("Cliente: "+envio.getCliente().getId());
        envio.setGuiaTsmo(envioService.generarGuia(proveedor, tipoPaquete));
        //envio.setCliente(Cliente.builder().id(cliente).build());
        envio.setDocumentacion(Documentacion.builder().id(documentacion).build());
        envio.setEstadoPago(EstadoPago.PENDIENTE.toString());
        envio.setUsuario(new Usuario(usuarioDB.getId()));
        envio.setCliente(Cliente.builder().id(envio.getCliente().getId()).build());
        if (proveedor.equalsIgnoreCase("TSMO")) {
            log.info("Entra a guardar para TSMO");
            envio.setEstadoEnvio(EstadoEnvio.PENDIENTE.toString());
        }
        Envio envioBD = null;
        log.info("Antes de guardar el envio");
        try {
            envioBD = envioService.guardar(envio);
        } catch (PersistentObjectException poe) {
            log.error("ERROR: "+poe.getMessage());
        }


        if (envio == null) {
            log.error("ERROR: No se pudo guardar el envio");
            return new ResponseEntity("ERROR: No se pudo guardar el envío", HttpStatus.BAD_REQUEST);
        }
        log.info("Guarda el envio exitosamente");
        if (envio.getGuiaProveedor() != null) {
            log.info("Guia proveedor: "+envio.getGuiaProveedor());
            envioBD.setGuiaProveedor(envio.getGuiaProveedor());
        }
        // envioBD.setEstadoEnvio(estadoPago);
        if (this.guardarPrimerRastreo(envioBD, EstadoPago.PENDIENTE.toString()) == null) {
            log.error("Hubo error al guardar el primer rastreo");
        }
        return ResponseEntity.ok(envioBD);
    }

    /*
    * SERVICIO PARA ACTUALIZAR GUIA PROVEEDOR DEL ENVIO
    * Servicio REST para guardar envio con numero de guia de proveedor
    * @author Rodrigo Robles
    * @param envio El objeto envio que se ha llenado con el numero de guia de proveedor obtenido anteriormente
     */
    @PostMapping("/proveedor/guia")
    public ResponseEntity<Envio> guardarGuiaProveedor(@RequestBody Envio envio) {
        log.info("Entra a controlador para almacenar guia de proveedor en el envio");
        Envio envioBD = envioService.actualizarGuiaProveedor(envio);
        if (envioBD == null) {
            return new ResponseEntity("ERROR: No se pudo almacenar el envio con la guia de proveedor", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(envioBD);
    }

    /*
    * METODO PARA BUSCAR ENVIOS REALIZADOS POR CLIENTE TSMO
    * Servicio REST para buscar envios realizados por el cliente (usuario)
    * @author Rodrigo Robles
    * @param usuario String El parametro usuario es de tipo String, porque se obtiene el nombreUsuario para buscar usaurio y de ahi obtener cliente
    *
     */
    @GetMapping("/listar/envios/{usuario}")
    public ResponseEntity<List<Envio>> listarEnviosPorCliente(@PathVariable("usuario") String usuario) {
        log.info("Entra a controlador para buscar todos los envio realizados por el cliente");
        Usuario usuarioBD = usuarioService.getByNombreUsuario(usuario);
        if (usuarioBD == null) {
            String msg = "ERROR: No existe usuario con ese nombre de usuario";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        if (usuarioBD.getCliente() == null) {
            String msg = "ERROR: Usuario no esta relacionado con algún cliente";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        log.info("");
        return null;
    }

    /*
    * METODO PARA BUSCAR TODOS LOS ENVIOS Y REGRESAR CONTEO POR ESTADO
    * Servicio REST para buscar envios realizados por el usuario TSMO
    * @author Rodrigo Robles
    * @param inicio String, Parametro inicio es de tipo String para despues convertir a tipo Date y realizar busqeuda por periodo de busqueda
    * @param fin String, Parametro fin es de tipo String para despues convertir a tipo Date y realizar busqeuda por periodo de busqueda
    * */
    @GetMapping("/listar/destinos/{inicio}/{fin}")
    public ResponseEntity<Map<String, Integer>> listarEnviosMapDestinos(@PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador para buscar todos los envios y regresar MAP de destino y conteo");
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        // List<Envio> envios = envioService.listar();
        List<Envio> envios = envioService.listarPorFechaInicioFechaFin(fechaInicio, fechaFinal);
        Set<String> ciudades = new HashSet<>();
        for (Envio envio : envios) {
            ciudades.add(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad());
        }
        Map<String, Integer> map = new HashMap<>();
        for (String ciudad : ciudades) {
            int count = 0;
            for (Envio envio : envios) {
                if (ciudad.equalsIgnoreCase(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad())) {
                    count++;
                }
            }
            map.put(ciudad, count);
        }
        return ResponseEntity.ok(map);
    }

    /*
    * METODO PARA BUSCAR ENVIOS POR DESTINO
    * Servicio REST para buscar envios por destino por periodo de busqueda
    * @author Rodrigo Robles
    * @param inicio String, Parametro inicio es de tipo String para despues convertir a tipo Date y realizar busqueda por periodo de busqueda
    * @param fin String, Parametro fin es de tipo String para despues convertir a tipo Date y realizar busqueda por periodo de busqueda
    * @param destino String, Parametro destino es de tipo String para filtrar los envios por la ciudad de destino
     */
    @GetMapping("/buscar/destino/{destino}/periodo/{inicio}/{fin}")
    public ResponseEntity<List<Envio>> buscarPorDestinoPeriodo(@PathVariable("destino") String destino, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
        log.info("Entra a controlador Envio para buscar envios por destino y periodo de tiempo");
        Date fechaInicio;
        Date fechaFinal;
        try{
            fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(inicio);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha inicio a objeto Date", HttpStatus.BAD_REQUEST);
        }
        try {
            fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fin);
        } catch (ParseException pe) {
            return new ResponseEntity("ERROR: Al convertir la cadena de fecha fin a objeto Date", HttpStatus.BAD_REQUEST);
        }
        // List<Envio> envios = envioService.listar();
        List<Envio> envios = envioService.listarPorFechaInicioFechaFin(fechaInicio, fechaFinal);
        List<Envio> enviosReturn = new ArrayList<>();
        for (Envio envio : envios) {
            if (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad().equalsIgnoreCase(destino)) {
                enviosReturn.add(envio);
            }
        }
        if (enviosReturn.isEmpty()) {
            return new ResponseEntity("ERROR: No existen envios para ese destino", HttpStatus.BAD_REQUEST);
        }
        log.info("Envios: "+enviosReturn.size());
        return ResponseEntity.ok(enviosReturn);
    }

    /*
    * METODO PARA ACTUALIAR ESTADO DE PAGO DE ENVIO
    * Servicio REST para actualizar envio
    * @author Rodrigo Robles
    * @param envio Long, Parametro envio es de tipo Long porque es el id del envio
    * @param estadoPago String, Parametro estadoPago es de tipo String porque es el valor a actualizar en estado pago
    * @return id Long, regresamos el id del envio
    * */
    @GetMapping("/actualizar/pago/{envio}/{estadoPago}")
    public ResponseEntity<Long> actualizarEstadoPago(@PathVariable("envio") Long envio, @PathVariable("estadoPago") String estadoPago) {
        log.info("Entra a controlador Envio para actualizar envio: " +envio);
        Envio envioBD = envioService.buscarPorId(envio);
        if (envioBD == null) {
            log.error("ERROR: No se encontro envio con ese id");
            return new ResponseEntity("ERROR: No se encontro envio con ese id", HttpStatus.BAD_REQUEST);
        }
        envioBD.setEstadoPago(estadoPago);
        return ResponseEntity.ok(envioService.guardar(envioBD).getId());
    }

    /*
    * METODO DE BUSQUEDA POR ORIGEN Y DESITNO
    * Servicio REST para buscar envios por origen y destino
    * @author Rodrigo Robles
    * @param origen String
    * @param destino String
    * @param allParams Map<String, String>
    * @return envios List<Envio>
    * */
    @GetMapping("/buscar/origen/{origen}/destino/{destino}")
    public ResponseEntity<List<Envio>> buscarPorOrigenDestino(@PathVariable("origen") String origen, @PathVariable("destino") String destino, @RequestParam Map<String, String> allParams) {
        log.info("Entra a controlador Envio para buscar envios por origen y destino");
        List<Envio> envios = envioService.buscarPorOrigenYDestino(origen, destino);
        if (envios.isEmpty()) {
            String msg = "ERROR: No se encontraron envios con esos datos";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        List<Envio> enviosFiltered = this.filtrarListaEnvios(allParams, envios, new CiudadesDto());
        if (enviosFiltered.isEmpty() || enviosFiltered == null) {
            String msg = "ERROR: No se pudo completar la busqueda con filtros";
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        log.info("Envios Filtrados: "+enviosFiltered.size());
        return ResponseEntity.ok(enviosFiltered);
    }

    /*
    * METODO DE BUSQUEDA DE ENVIOS POR ORIGEN
    * Servicio REST para buscar envios por origen
    * @author Rodrigo Robles
    * @param origen String
    * @param allParams Map<String, String>
    * @return envios List<Envio>
    *  */
    @GetMapping("/buscar/origen/{origen}")
    public ResponseEntity<List<Envio>> buscarPorOrige(@PathVariable("origen") String origen, @RequestParam Map<String, String> allParams) {
        log.info("Entra a controlador Envio para buscar envios por origen");
        List<Envio> envios = envioService.buscarPorOrigen(origen);
        if (envios.isEmpty()) {
            String msg = "ERROR: No se encontraron envios con esos datos";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        List<Envio> enviosFiltered = this.filtrarListaEnvios(allParams, envios, new CiudadesDto());
        if (enviosFiltered.isEmpty() || enviosFiltered == null) {
            String msg = "ERROR: No se pudo completar la busqueda con filtros";
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        log.info("Envios Filtrados: "+enviosFiltered.size());
        return ResponseEntity.ok(enviosFiltered);
    }

    /*
    * METODO DE BUSQEUDA DE ENVIOS POR DESTINO
    * Servicio REST para buscar envios por destino
    * @author Rodrigo Robles
    * @param destino string
    * @param allParams Map<String, String>
    * @return envios List<Envio>
    * */
    @GetMapping("/buscar/destino/{destino}")
    public ResponseEntity<List<Envio>> buscarPorDestino(@PathVariable("destino") String destino, @RequestParam Map<String, String> allParams) {
        log.info("Entra a controlador Envio para buscar envios por destino");
        List<Envio> envios = envioService.buscarPorDestino(destino);
        if (envios.isEmpty()) {
            String msg = "ERROR: No se encontraron envios con esos datos";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        List<Envio> enviosFiltered = this.filtrarListaEnvios(allParams, envios, new CiudadesDto());
        if (enviosFiltered.isEmpty() || enviosFiltered == null) {
            String msg = "ERROR: No se pudo completar la busqueda con filtros";
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        log.info("Envios Filtrados: "+enviosFiltered.size());
        return ResponseEntity.ok(enviosFiltered);
    }

    /*
    * METODO PARA FILTRAR LISTA DE ENVIOS POR PARAMETROS
    * Metodo para filtrar lista de envios donde ya se ha realizado una busqueda previa por Origen y/ o Destino
    * @author Rodrigo Robles
    * @param allParams Map<String, String>, parametro obtenido desde el formulario de busqueda de envios
    * @param envios Envio, parametro obtenido de la previa busqueda realizada por Origen y/o Destino
    * @return enviosFiltered List<Envio>, parametro que contiene la lista filtrada por los parametros
    * */
    public List<Envio> filtrarListaEnvios(Map<String, String> allParams, List<Envio> envios, CiudadesDto ciudadesDto) {
        log.info("Entra a metodo filtrarListaEnvios");
        log.info("Envios: "+envios.size());
        List<Envio> enviosFiltered = new ArrayList<>();
        try {
            log.info("Params: ",ciudadesDto.getParams());
            if (ciudadesDto.getParams()) {
                log.info("Entra a filtrar por parametros");
                Date periodoInicial = null;
                for (Map.Entry<String, String> entry : allParams.entrySet()) {
                    log.info(entry.getKey());
                    log.info(entry.getValue());
                    switch (entry.getKey()) {
                        case "periodoInicial":
                            try {
                                periodoInicial = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                                Date finalPeriodoInicial = periodoInicial;
                                log.info("finalPeriodoInicial" + finalPeriodoInicial);
                                enviosFiltered = envios.stream()
                                        .filter(envio -> envio.getCreateAt().after(finalPeriodoInicial))
                                        .collect(Collectors.toList());
                            } catch (ParseException p) {
                                String msg = "ERROR: Error en parse en exception periodo inicial";
                                log.error(msg);
                                //return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
                                return null;
                            }
                            break;
                        case "periodoFinal":
                            try {
                                Date periodoFinal = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                                Calendar c = Calendar.getInstance();
                                c.setTime(periodoFinal);
                                c.add(Calendar.DATE, 1);
                                Date finalPeriodoFinal = c.getTime();
                                log.info("finalPeriodoFinal:" + finalPeriodoFinal);
                                enviosFiltered = envios.stream()
                                        .filter(envio -> envio.getCreateAt().before(finalPeriodoFinal))
                                        .collect(Collectors.toList());
                            } catch (ParseException pe) {
                                String msg = "ERROR: Error en parse en exception periodo final";
                                log.error(msg);
                                // return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
                                return null;
                            }
                            break;
                        case "estadoEnvio":
                            enviosFiltered = envios.stream()
                                    .filter(envio -> envio.getEstadoEnvio().equalsIgnoreCase(entry.getValue()))
                                    .collect(Collectors.toList());
                            break;
                        case "estadoPago":
                            enviosFiltered = envios.stream()
                                    .filter(envio -> envio.getEstadoPago().equalsIgnoreCase(entry.getValue()))
                                    .collect(Collectors.toList());
                            break;
                        case "cliente":
                            //Cliente cliente = Cliente.builder().id(Long.parseLong(entry.getValue())).build();
                            enviosFiltered = envios.stream()
                                    .filter(envio -> envio.getCliente().getId().compareTo(Long.valueOf(entry.getValue())) == 0)
                                    .collect(Collectors.toList());
                            break;
                        case "proveedor":
                            enviosFiltered = envios.stream()
                                    .filter(envio -> envio.getGuiaTsmo().contains(entry.getValue()))
                                    .collect(Collectors.toList());
                            // Documentacion doc = Documentacion.builder().cotizacion(Cotizacion.builder().realiza(entry.getValue()).build()).build();
                            // predicates.add(cb.equal(fromEnvio.get("documentacion"), doc));
                            break;
                    }
                }
            } else {
                log.info("Regresa lista de envios original");
                return envios;
            }
        } catch (NullPointerException ne) {
            log.error("ERROR: "+ne.getMessage());
            log.error("ERR: "+ne.fillInStackTrace());
            log.error("E:"+ne.getStackTrace());
            ne.printStackTrace();
            return null;
        }
        return enviosFiltered;
    }

    /*
    * METODO PARA BUSCAR ENVIO POR BUSQUEDA DE FILTROS
    * Servicio REST para buscar envios por filtros
    * @author Rodrigo Robles
    * @params allParams Map<String, String>, contiene todos los parametros de los filtros aplicados para la busqueda
    * @return enviosFiltered List<Envio>
    * */
    @GetMapping("buscar/filtros/params")
    public ResponseEntity<List<Envio>> buscar(@RequestParam Map<String, String> allParams) {
        log.info("Entra a controlador para buscar envios por parametros");
        List<Envio> envios = new ArrayList<>();
        CiudadesDto ciudadesDto = this.verificarParametrosParaBusqueda(allParams);
        switch (ciudadesDto.getBusqueda()) {
            case "ambos":
                log.info("Busca por ambos");
                envios = envioService.buscarPorOrigenYDestino(ciudadesDto.getOrigen(), ciudadesDto.getDestino());
                break;
            case "origen":
                log.info("Busca por origen");
                envios = envioService.buscarPorOrigen(ciudadesDto.getOrigen());
                break;
            case "destino":
                log.info("Busca por destino");
                envios = envioService.buscarPorDestino(ciudadesDto.getDestino());
                break;
            case "params":
                log.info("Busca por params");
                envios = envioService.buscarEnvioParams(allParams);
                break;
        }
        log.info("Envios: "+envios.size());
        if (!ciudadesDto.getBusqueda().equalsIgnoreCase("params")) {
            List<Envio> enviosFiltered = this.filtrarListaEnvios(allParams, envios, ciudadesDto);
            log.info("EnviosFiltered.isEmpty(): "+enviosFiltered.isEmpty());
            log.info("EnviosFiltered == Null: ",enviosFiltered==null);
            log.info("True:",enviosFiltered.isEmpty() || enviosFiltered == null);
            if (enviosFiltered.isEmpty() || enviosFiltered == null) {
                String msg = "ERROR: No se pudo completar la busqueda con filtros";
                log.error(msg);
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
            log.info("Envios Filtrados: "+enviosFiltered.size());
            log.info("Envios: "+enviosFiltered.size());
            return ResponseEntity.ok(enviosFiltered);
        }
        log.info("Envios: "+envios.size());
        return ResponseEntity.ok(envios);
    }

    /*
    *
     */
    public CiudadesDto verificarParametrosParaBusqueda(Map<String, String> allParams) {
        log.info("Entra a metodo para identificar busqueda");
        Boolean origenExiste = false;
        Boolean destinoExiste = false;
        Boolean params = false;
        CiudadesDto ciudadesDto = new CiudadesDto();
        ciudadesDto.setParams(false);
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().contains("origen")) {
                origenExiste = true;
                ciudadesDto.setOrigen(entry.getValue());
            } else if (entry.getKey().contains("destino")) {
                destinoExiste = true;
                ciudadesDto.setDestino(entry.getValue());
            } else {
                ciudadesDto.setParams(true);
            }
        }
        if (origenExiste && destinoExiste) {
            ciudadesDto.setBusqueda("ambos");
            //return "ambos";
        } else if (origenExiste) {
            ciudadesDto.setBusqueda("origen");
            // return "origen";
        } else if (destinoExiste) {
            ciudadesDto.setBusqueda("destino");
            // return "destino";
        } else {
            ciudadesDto.setBusqueda("params");
            // return "params";
        }
        return ciudadesDto;
    }

    /*
    * SERVICIO REST PARA EXPORTAR EXCEL
    * Metodo para exportar Excel con lista de envios
    * @author Rodrigo Robles
    * @param envios List<Envios>, Lista de envios que se desea exportar para el Excel
    * @return Excel response
    * */
    @GetMapping("/exportar/excel")
    public void exportToExcel(HttpServletResponse response, @RequestParam Map<String, String> allParams) throws IOException {
        log.info("Entra a controlador para exportar excel");
        List<Envio> envios = new ArrayList<>();
        CiudadesDto ciudadesDto = this.verificarParametrosParaBusqueda(allParams);
        switch (ciudadesDto.getBusqueda()) {
            case "ambos":
                log.info("Busca por ambos");
                envios = envioService.buscarPorOrigenYDestino(ciudadesDto.getOrigen(), ciudadesDto.getDestino());
                break;
            case "origen":
                log.info("Busca por origen");
                envios = envioService.buscarPorOrigen(ciudadesDto.getOrigen());
                break;
            case "destino":
                log.info("Busca por destino");
                envios = envioService.buscarPorDestino(ciudadesDto.getDestino());
                break;
            case "params":
                log.info("Busca por params");
                envios = envioService.buscarEnvioParams(allParams);
                break;
        }
        log.info("Envios: "+envios.size());
        if (!ciudadesDto.getBusqueda().equalsIgnoreCase("params")) {
            List<Envio> enviosFiltered = this.filtrarListaEnvios(allParams, envios, ciudadesDto);
            log.info("EnviosFiltered.isEmpty(): "+enviosFiltered.isEmpty());
            log.info("EnviosFiltered == Null: ",enviosFiltered==null);
            log.info("True:",enviosFiltered.isEmpty() || enviosFiltered == null);
            if (enviosFiltered.isEmpty() || enviosFiltered == null) {
                String msg = "ERROR: No se pudo completar la busqueda con filtros";
                log.error(msg);
                // return HttpStatus.BAD_REQUEST;
            }
            log.info("Envios Filtrados: "+enviosFiltered.size());
            log.info("Envios: "+enviosFiltered.size());
            // return ResponseEntity.ok(enviosFiltered);
        }
        log.info("Envios: "+envios.size());

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= envios_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        exportarExcelEnvioService = new ExportarExcelEnvioService(envios);
        exportarExcelEnvioService.export(response);
    }

    /*
    * SERVICIO REST PARA CREAR DOCUMENTO DE GUIA
    * Método para crear documento de guia para imprimir
    * @author Rodrigo Robles
    * @param guia String, Numero de guia de tipo string que se desea imprimir
    * @return Archivo PDF
    * */
    @GetMapping("/imprimir/{guia}")
    public ResponseEntity<byte[]> imprimirGuia(@PathVariable("guia") String guia) {
        log.info("Ingresa a controlador REST para crear documetno de guia: "+guia);
        Envio envioBD = envioService.buscarPorGuiaTsmo(guia);
        if (envioBD == null) {
            String msg = "No hay envio con ese numero de guia";
            log.info(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        byte[] guiaPDF = documentoService.crearDocumentoGuia(notificacionService.crearEnvioDoc(envioBD));
        if (guiaPDF == null) {
            String msg = "ERROR: Ocurrio un error en crear el archivo pdf";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        String fileName = guia + ".pdf";
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(guiaPDF.length);
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return new ResponseEntity<byte[]>(guiaPDF, respHeaders, HttpStatus.OK);
    }

    /*
    @GetMapping("prueba/fechas")
    public Map<String, String> pruebaFechas() {
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
            if (i == 1 && envioService.getBisiesto(anio)) {
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
    */

    /*
    * SERVICIO REST PARA GENERAR GUIAS DE VARIOS ENVIOS
    * Metodo para realizar documentacion de los envios a granel
    * @author Rodrigo Robles
    * @param envioGranel EnviosGranel, Contiene la informacion para solicitar los envios a granel
    * @return envioGranelReturn EnviosGranel, Regresa los envios con toda la informacion correspondiente de BD para su manejo
    * */
    @PostMapping("/envios/clientes")
    public ResponseEntity<EnviosGranel> genarcionGuidsGranel(@RequestBody EnviosGranel envioGranel) {
        log.info("Entra a servicio de controlador para generar guias de clientes para envios a granel");

        for (int i = 0; i <= envioGranel.getEnvios().size(); i++) {
            //envioGranel.getEnvios().get(0).setGuiaTsmo(envioService.generarGuia(envioGranel.getEnvios().get(0).getDocumentacion().getCotizacion().getRealiza()),);
        }
        return null;
    }

}
