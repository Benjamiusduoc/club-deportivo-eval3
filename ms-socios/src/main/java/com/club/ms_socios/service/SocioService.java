package com.club.ms_socios.service;

import com.club.ms_socios.model.dto.EmailUpdateDTO;
import com.club.ms_socios.model.dto.SocioRequestDTO;
import com.club.ms_socios.model.entity.Socio;
import com.club.ms_socios.repository.SocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service //esta clase es un servicio que maneja lógica
public class SocioService {

    // SLF4J para cumplir con el registro de logs estructurados
    private static final Logger log = LoggerFactory.getLogger(SocioService.class);

    private final SocioRepository socioRepository;

    // Inyección de dependencias a través del constructor
    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public Socio registrarSocio(SocioRequestDTO dto) {
        log.info("Iniciando registro de nuevo socio con RUT: {}", dto.getRut());

        //Valida si el RUT ya existe en la Base de Datos
        Optional<Socio> socioExistenteRut = socioRepository.findByRut(dto.getRut());
        if (socioExistenteRut.isPresent()) {
            log.error("Validación fallida: El RUT {} ya se encuentra registrado", dto.getRut());
            throw new RuntimeException("El RUT ingresado ya pertenece a un socio existente.");
        }

        //Valida si el Email ya existe
        Optional<Socio> socioExistenteEmail = socioRepository.findByEmail(dto.getEmail());
        if (socioExistenteEmail.isPresent()) {
            log.error("Validación fallida: El correo {} ya se encuentra registrado", dto.getEmail());
            throw new RuntimeException("El correo ingresado ya pertenece a un socio existente.");
        }

        // Si pasa la validacion, transformamos el DTO en una Entidad
        Socio nuevoSocio = new Socio();
        nuevoSocio.setNombre(dto.getNombre());
        nuevoSocio.setRut(dto.getRut());
        nuevoSocio.setEmail(dto.getEmail());

        // Asignamos los datos internos que el usuario no debe enviar por seguridad
        nuevoSocio.setFechaInscripcion(LocalDate.now());
        nuevoSocio.setActivo(true); //socio nuevo entra como activo por defecto

        // Guardamos físicamente en la tabla de MySQL
        Socio socioGuardado = socioRepository.save(nuevoSocio);

        log.info("Socio registrado exitosamente con ID: {}", socioGuardado.getId());

        return socioGuardado;
    }

    // Endpoint GET: Listar todos los socios
    public List<Socio> listarTodos() {
        log.info("Consultando la lista completa de socios");
        return socioRepository.findAll();
    }

    // Endpoint GET: Buscar un socio por su ID
    public Socio buscarPorId(Long id) {
        log.info("Buscando socio con ID: {}", id);
        return socioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el socio con ID: {}", id);
                    return new RuntimeException("Socio no encontrado con el ID proporcionado.");
                });
    }

    // Endpoint PUT: Actualizar datos de un socio existente
    public Socio actualizarSocio(Long id, SocioRequestDTO dto) {
        log.info("Actualizando socio con ID: {}", id);

        // 1. Verificamos si existe antes de intentar actualizar
        Socio socioExistente = buscarPorId(id);

        // 2. Actualizamos solo los campos permitidos
        socioExistente.setNombre(dto.getNombre());
        socioExistente.setRut(dto.getRut());
        socioExistente.setEmail(dto.getEmail());

        // 3. Guardamos los cambios
        Socio socioActualizado = socioRepository.save(socioExistente);
        log.info("Socio con ID: {} actualizado exitosamente", id);

        return socioActualizado;
    }

    // Endpoint DELETE
    public Socio desactivarSocio(Long id) {
        log.info("Desactivando socio con ID: {}", id);

        // 1. Buscamos al socio (si no existe, esto lanza la excepción sola)
        Socio socio = buscarPorId(id);

        // 2. Cambiamos su estado a inactivo
        socio.setActivo(false);

        // 3. Guardamos los cambios
        Socio socioDesactivado = socioRepository.save(socio);
        log.info("Socio con ID: {} desactivado exitosamente", id);

        return socioDesactivado;
    }

    // Endpoint Get para buscar un socio por su RUT

    public Socio buscarPorRut(String rut) {
        log.info("Buscando socio con RUT: {}", rut);
        return socioRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.error("No se encontró el socio con RUT: {}", rut);
                    return new RuntimeException("Socio no encontrado con el RUT proporcionado.");
                });
    }

    //Endpoint Put para reactivar un socio
    public Socio reactivarSocio(Long id) {
        log.info("Reactivando socio con ID: {}", id);
        Socio socio = buscarPorId(id);
        socio.setActivo(true);
        Socio guardado = socioRepository.save(socio);
        log.info("Socio con ID: {} reactivado exitosamente", id);
        return guardado;
    }

    //Endpoint para listar socios activos
    public List<Socio> listarActivos() {
        log.info("Consultando socios con estado activo");
        return socioRepository.findByActivoTrue();
    }

    //Endpoint para actualizar el email de un socio
    public Socio actualizarEmail(Long id, EmailUpdateDTO dto) {
        log.info("Actualizando email del socio con ID: {}", id);
        Socio socio = buscarPorId(id);

        socioRepository.findByEmail(dto.getEmail()).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                log.error("Validación fallida: el correo {} ya pertenece a otro socio", dto.getEmail());
                throw new RuntimeException("El correo ingresado ya pertenece a un socio existente.");
            }
        });

        socio.setEmail(dto.getEmail());
        Socio actualizado = socioRepository.save(socio);
        log.info("Email del socio con ID: {} actualizado exitosamente", id);
        return actualizado;
    }

    //Endpoint para contar socios activos
    public long contarSociosActivos() {
        log.info("Contando socios con estado activo");
        return socioRepository.countByActivoTrue();
    }
}
