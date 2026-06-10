package com.club.ms_notificaciones.repository;

import com.club.ms_notificaciones.model.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByIdSocio(Long idSocio);

    List<Notificacion> findByIdSocioAndLeido(Long idSocio, Boolean leido);

    List<Notificacion> findByLeido(Boolean leido);

    long countByIdSocioAndLeido(Long idSocio, Boolean leido);
}
