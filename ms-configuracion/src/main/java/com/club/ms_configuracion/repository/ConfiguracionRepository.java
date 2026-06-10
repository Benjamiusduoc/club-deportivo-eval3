package com.club.ms_configuracion.repository;

import com.club.ms_configuracion.model.entity.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {

    Optional<Configuracion> findByClave(String clave);

    List<Configuracion> findAllByOrderByClaveAsc();
}
