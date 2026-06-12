package com.senkiu.ong.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.senkiu.estado.organizacion.model.EstadoOrganizacion;
import com.senkiu.estado.organizacion.repository.EstadoOrganizacionRepository;
import com.senkiu.ong.model.Ong;
import com.senkiu.ong.repository.OngRepository;
import com.senkiu.ong.service.OngService;
import com.senkiu.rifa.model.Rifa;
import com.senkiu.rifa.repository.RifaRepository;
import com.senkiu.rifa.ticket.dto.TicketRifaDetalleResponse;
import com.senkiu.solicitud.model.SolicitudOrganizacion;
import com.senkiu.ticket.repository.TicketRepository;
import com.senkiu.usuario.model.Usuario;
import com.senkiu.usuario.repository.UsuarioRepository;

@Service
public class OngServiceImpl implements OngService {

    private final OngRepository ongRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoOrganizacionRepository estadoRepo;
    private final RifaRepository rifaRepository;
    private final TicketRepository ticketRepository;

        public OngServiceImpl(
                OngRepository ongRepository,
                UsuarioRepository usuarioRepository,
                EstadoOrganizacionRepository estadoRepo,
                RifaRepository rifaRepository,
                TicketRepository ticketRepository
        ) {
        this.ongRepository = ongRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepo = estadoRepo;
        this.rifaRepository = rifaRepository;
        this.ticketRepository = ticketRepository;
        }

    private String extraerDescripcion(String datosJson) {

        if (datosJson == null || datosJson.isBlank()) {
                return "";
        }

        try {

                int inicio = datosJson.indexOf("\"descripcion\":\"");

                if (inicio == -1) {
                return "";
                }

                inicio += 15;

                int fin = datosJson.indexOf("\"", inicio);

                if (fin == -1) {
                return "";
                }

                return datosJson.substring(inicio, fin);

        } catch (Exception e) {

                return "";
        }
        }

    @Override
    public Ong obtenerPorUsuario(Long usuarioId) {

        Usuario user = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        return ongRepository.findByUsuario(user)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));
    }

    @Override
        public Ong crearDesdeSolicitud(SolicitudOrganizacion sol) {

        EstadoOrganizacion aprobado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        Ong ong = new Ong();

        ong.setNombre(sol.getNombre());
        ong.setRuc(sol.getRuc());
        ong.setEmail(sol.getEmail());
        ong.setDireccion(sol.getDireccion());
        ong.setUsuario(sol.getUsuario());
        ong.setEstado(aprobado);

        String descripcion = extraerDescripcion(sol.getDatosJson());

        ong.setDescripcion(descripcion);

        Ong saved = ongRepository.save(ong);

        Usuario user = usuarioRepository.findById(
                sol.getUsuario().getId()
        ).orElseThrow();

        System.out.println("ANTES: " + user.getRol());

        user.setRol("ROLE_ONG");

        System.out.println("DESPUÉS: " + user.getRol());

        usuarioRepository.saveAndFlush(user);

        Usuario actualizado = usuarioRepository.findById(
                user.getId()
        ).orElseThrow();

        System.out.println("BD: " + actualizado.getRol());

        return saved;
        }

    @Override
    public void suspender(Long ongId) {

        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("SUSPENDIDO")
                .orElseThrow();

        ong.setEstado(estado);

        ongRepository.save(ong);
    }

    @Override
    public void activar(Long ongId) {

        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new RuntimeException("ONG no existe"));

        EstadoOrganizacion estado = estadoRepo.findByNombre("APROBADO")
                .orElseThrow();

        ong.setEstado(estado);

        ongRepository.save(ong);
    }

    @Override
    public List<Ong> findAll() {
        return ongRepository.findAll();
    }

        @Override
        public List<TicketRifaDetalleResponse> obtenerTickets(Long ongId) {

        List<Rifa> rifas = rifaRepository.findByOngId(ongId);

        return rifas.stream()
                .flatMap(rifa ->
                        ticketRepository.findTicketsByRifaId(rifa.getId()).stream()
                )
                .toList();
        }
}