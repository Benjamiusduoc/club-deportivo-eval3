package com.club.ms_instructores.repository;

import com.club.ms_instructores.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByRut(String rut);

    Optional<Instructor> findByEmail(String email);

    List<Instructor> findByActivoTrue();
}
