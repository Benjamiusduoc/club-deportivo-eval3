package com.club.ms_instructores.service;

import com.club.ms_instructores.exception.RecursoNoEncontradoException;
import com.club.ms_instructores.exception.ReglaNegocioException;
import com.club.ms_instructores.model.dto.InstructorRequestDTO;
import com.club.ms_instructores.model.dto.InstructorResponseDTO;
import com.club.ms_instructores.model.entity.Instructor;
import com.club.ms_instructores.repository.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    private InstructorRequestDTO crearDtoValido() {
        InstructorRequestDTO dto = new InstructorRequestDTO();
        dto.setNombre("Carlos Garcia");
        dto.setRut("87654321-0");
        dto.setEmail("carlos@ejemplo.cl");
        dto.setTelefono("+56912345678");
        dto.setEspecialidad("Yoga");
        return dto;
    }

    private Instructor crearInstructorExistente() {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setNombre("Carlos Garcia");
        instructor.setRut("87654321-0");
        instructor.setEmail("carlos@ejemplo.cl");
        instructor.setTelefono("+56912345678");
        instructor.setEspecialidad("Yoga");
        instructor.setActivo(true);
        return instructor;
    }

    @Test
    @DisplayName("Given datos validos, when crearInstructor, then crea instructor activo")
    void crearInstructor_exitoso() {
        InstructorRequestDTO dto = crearDtoValido();
        when(instructorRepository.findByRut(dto.getRut())).thenReturn(Optional.empty());
        when(instructorRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(invocation -> {
            Instructor i = invocation.getArgument(0);
            i.setId(1L);
            return i;
        });

        InstructorResponseDTO resultado = instructorService.crearInstructor(dto);

        assertNotNull(resultado.getId());
        assertEquals("Carlos Garcia", resultado.getNombre());
        assertTrue(resultado.getActivo());

        ArgumentCaptor<Instructor> captor = ArgumentCaptor.forClass(Instructor.class);
        verify(instructorRepository).save(captor.capture());
        assertEquals("87654321-0", captor.getValue().getRut());
    }

    @Test
    @DisplayName("Given RUT duplicado, when crearInstructor, then lanza ReglaNegocioException")
    void crearInstructor_rutDuplicado() {
        InstructorRequestDTO dto = crearDtoValido();
        when(instructorRepository.findByRut(dto.getRut())).thenReturn(Optional.of(crearInstructorExistente()));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> instructorService.crearInstructor(dto));
        assertTrue(ex.getMessage().contains("RUT"));
        verify(instructorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given email duplicado, when crearInstructor, then lanza ReglaNegocioException")
    void crearInstructor_emailDuplicado() {
        InstructorRequestDTO dto = crearDtoValido();
        when(instructorRepository.findByRut(dto.getRut())).thenReturn(Optional.empty());
        when(instructorRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(crearInstructorExistente()));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> instructorService.crearInstructor(dto));
        assertTrue(ex.getMessage().contains("email"));
        verify(instructorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(instructorRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> instructorService.buscarPorId(99L));
        assertEquals("Instructor no encontrado con el ID proporcionado.", ex.getMessage());
    }

    @Test
    @DisplayName("Given instructores en BD, when listarTodos, then retorna lista")
    void listarTodos_retornaLista() {
        List<Instructor> instructores = List.of(crearInstructorExistente());
        when(instructorRepository.findAll()).thenReturn(instructores);

        List<InstructorResponseDTO> resultado = instructorService.listarTodos();

        assertEquals(1, resultado.size());
        verify(instructorRepository).findAll();
    }
}
