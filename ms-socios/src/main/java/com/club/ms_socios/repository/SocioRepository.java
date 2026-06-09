package com.club.ms_socios.repository;

import com.club.ms_socios.model.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    // Spring Data JPA crea la consulta SQL automáticamente por debajo
    Optional<Socio> findByRut(String rut);
    Optional<Socio> findByEmail(String email);

    List<Socio> findByActivoTrue();

    long countByActivoTrue();
}
