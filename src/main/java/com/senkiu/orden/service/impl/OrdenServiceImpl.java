package com.senkiu.orden.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiu.carrito.model.Carrito;
import com.senkiu.carrito.model.CarritoItem;
import com.senkiu.carrito.repository.CarritoRepository;

import com.senkiu.estado.orden.model.EstadoOrden;
import com.senkiu.estado.orden.repository.EstadoOrdenRepository;

import com.senkiu.orden.model.Orden;
import com.senkiu.orden.model.OrdenDetalle;
import com.senkiu.orden.repository.OrdenRepository;
import com.senkiu.orden.service.OrdenService;

import com.senkiu.pago.service.PagoService;
import com.senkiu.pago.dto.CrearPagoRequest;
import com.senkiu.pago.model.MetodoPago;
import com.senkiu.pago.model.Pago;
import com.senkiu.pago.model.TipoPago;

import com.senkiu.producto.model.Producto;
import com.senkiu.producto.repository.ProductoRepository;

import com.senkiu.entrega.service.EntregaService;

@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final CarritoRepository carritoRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final ProductoRepository productoRepository;
    private final EntregaService entregaService;
    private final PagoService pagoService;

    public OrdenServiceImpl(
            OrdenRepository ordenRepository,
            CarritoRepository carritoRepository,
            EstadoOrdenRepository estadoOrdenRepository,
            ProductoRepository productoRepository,
            EntregaService entregaService,
            PagoService pagoService
    ) {
        this.ordenRepository = ordenRepository;
        this.carritoRepository = carritoRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.productoRepository = productoRepository;
        this.entregaService = entregaService;
        this.pagoService = pagoService;
    }

        @Override
        @Transactional
        public Orden crearOrden(Long usuarioId, Long empresaId) {

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems().isEmpty()) {
                throw new RuntimeException("El carrito está vacío");
        }

        EstadoOrden estado = estadoOrdenRepository
                .findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        Orden orden = new Orden();
        orden.setUsuario(carrito.getUsuario());
        orden.setEmpresa(carrito.getEmpresa());
        orden.setEstado(estado);

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {

                Producto producto = item.getProducto();

                if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre());
                }

                producto.setStock(producto.getStock() - item.getCantidad());
                productoRepository.save(producto);

                OrdenDetalle detalle = new OrdenDetalle();
                detalle.setOrden(orden);
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecio(item.getPrecio());

                orden.getDetalles().add(detalle);

                BigDecimal subtotal = item.getPrecio()
                        .multiply(BigDecimal.valueOf(item.getCantidad()));

                total = total.add(subtotal);
        }

        orden.setTotal(total);

        // 1. Guardas orden
        Orden ordenGuardada = ordenRepository.save(orden);

        // 2. Creas pago
        CrearPagoRequest pagoRequest = new CrearPagoRequest();
        pagoRequest.setUsuarioId(usuarioId);
        pagoRequest.setTipo(TipoPago.ORDEN);
        pagoRequest.setReferenciaId(ordenGuardada.getId());
        pagoRequest.setMonto(total);
        pagoRequest.setMetodo(MetodoPago.MOCK);

        Pago pago = pagoService.crearPago(pagoRequest);

        // 🔥 CLAVE: enlazar pago con orden
        ordenGuardada.setPago(pago);
        ordenRepository.save(ordenGuardada);

        entregaService.crearEntregaInicial(ordenGuardada.getId());

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return ordenGuardada;
        }

    @Override
    public List<Orden> listarPorUsuario(
            Long usuarioId
    ) {
        return ordenRepository
                .findByUsuarioId(usuarioId);
    }

    @Override
    public List<Orden> listarPorEmpresa(
            Long empresaId
    ) {
        return ordenRepository
                .findByEmpresaId(empresaId);
    }

    @Override
    public void cambiarEstado(
            Long ordenId,
            String estadoNombre
    ) {

        Orden orden = ordenRepository
                .findById(ordenId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Orden no encontrada"
                        )
                );

        EstadoOrden estado = estadoOrdenRepository
                .findByNombre(estadoNombre)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Estado no encontrado"
                        )
                );

        orden.setEstado(estado);

        ordenRepository.save(orden);
    }
}