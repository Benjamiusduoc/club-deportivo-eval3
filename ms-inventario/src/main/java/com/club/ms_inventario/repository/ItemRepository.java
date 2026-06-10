package com.club.ms_inventario.repository;

import com.club.ms_inventario.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByIdActividad(Long idActividad);

    List<Item> findByEstado(String estado);

    List<Item> findByNombreContainingIgnoreCase(String nombre);
}
