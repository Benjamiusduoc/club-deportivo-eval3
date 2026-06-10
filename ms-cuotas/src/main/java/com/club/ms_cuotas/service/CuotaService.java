package com.club.ms_cuotas.service;

import com.club.ms_cuotas.client.SocioClient;
import com.club.ms_cuotas.exception.RecursoNoEncontradoException;
import com.club.ms_cuotas.exception.ReglaNegocioException;
import com.club.ms_cuotas.model.dto.CuotaRequestDTO;
import com.club.ms_cuotas.model.dto.CuotaResponseDTO;
import com.club.ms_cuotas.model.dto.PagoRequestDTO;
import com.club.ms_cuotas.model.entity.Cuota;
import com.club.ms_cuotas.repository.CuotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CuotaService {

    private static final Logger log = LoggerFactory.getLogger(CuotaService.class);

    private final CuotaRepository cuotaRepository;
    private final SocioClient socioClient;

    public CuotaService(CuotaRepository cuotaRepository, SocioClient socioClient) {
        this.cuotaRepository = cuotaRepository;
        this.socioClient = socioClient;
    }

    @Transactional
    public CuotaResponseDTO crearCuota(CuotaRequestDTO dto) {
        log.info("Creando cuota para socio ID: {}, periodo: {}", dto.getIdSocio(), dto.getPeriodo());

        socioClient.validarSocioActivo(dto.getIdSocio());

        Optional<Cuota> existente = cuotaRepository.findByIdSocioAndPeriodo(dto.getIdSocio(), dto.getPeriodo());
        if (existente.isPresent()) {
            throw new ReglaNegocioException(
                    "El socio ya tiene una cuota registrada para el periodo " + dto.getPeriodo());
        }

        Cuota cuota = new Cuota();
        cuota.setIdSocio(dto.getIdSocio());
        cuota.setMonto(dto.getMonto());
        cuota.setPeriodo(dto.getPeriodo());
        cuota.setFechaEmision(LocalDate.now());
        cuota.setFechaVencimiento(dto.getFechaVencimiento());
        cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);

        Cuota guardada = cuotaRepository.save(cuota);
        log.info("Cuota creada exitosamente con ID: {}", guardada.getId());

        return CuotaResponseDTO.fromEntity(guardada);
    }

    @Transactional(readOnly = true)
    public List<CuotaResponseDTO> listarTodas() {
        return cuotaRepository.findAll().stream()
                .map(CuotaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuotaResponseDTO buscarPorId(Long id) {
        Cuota cuota = cuotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuota no encontrada con ID: " + id));
        return CuotaResponseDTO.fromEntity(cuota);
    }

    @Transactional(readOnly = true)
    public List<CuotaResponseDTO> listarPorSocio(Long idSocio) {
        return cuotaRepository.findByIdSocio(idSocio).stream()
                .map(CuotaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CuotaResponseDTO> listarPorEstado(Cuota.EstadoCuota estado) {
        return cuotaRepository.findByEstado(estado).stream()
                .map(CuotaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CuotaResponseDTO pagarCuota(Long id, PagoRequestDTO dto) {
        log.info("Procesando pago de cuota ID: {}", id);

        Cuota cuota = cuotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuota no encontrada con ID: " + id));

        if (cuota.getEstado() == Cuota.EstadoCuota.PAGADA) {
            throw new ReglaNegocioException("La cuota ya se encuentra pagada");
        }

        cuota.setEstado(Cuota.EstadoCuota.PAGADA);
        cuota.setFechaPago(LocalDate.now());
        cuota.setMetodoPago(dto.getMetodoPago());

        Cuota actualizada = cuotaRepository.save(cuota);
        log.info("Cuota ID: {} pagada exitosamente con {}", id, dto.getMetodoPago());

        return CuotaResponseDTO.fromEntity(actualizada);
    }

    @Transactional
    public void actualizarCuotasVencidas() {
        List<Cuota> vencidas = cuotaRepository.findAll().stream()
                .filter(c -> c.getEstado() == Cuota.EstadoCuota.PENDIENTE
                        && c.getFechaVencimiento().isBefore(LocalDate.now()))
                .toList();

        if (!vencidas.isEmpty()) {
            vencidas.forEach(c -> c.setEstado(Cuota.EstadoCuota.VENCIDA));
            cuotaRepository.saveAll(vencidas);
            log.info("{} cuotas marcadas como vencidas", vencidas.size());
        }
    }
}
