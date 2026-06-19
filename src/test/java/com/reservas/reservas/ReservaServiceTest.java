package com.reservas.reservas;

import com.reservas.reservas.dto.ReservaRequestDTO;
import com.reservas.reservas.dto.ReservaResponseDTO;
import com.reservas.reservas.model.Reserva;
import com.reservas.reservas.repository.ReservaRepository;
import com.reservas.reservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void listarReservas(){
        Reserva reserva = new Reserva();
        reserva.setIdCita(1L);
        reserva.setIdUsuario(10L);
        reserva.setIdEmp(20L);
        reserva.setTipoCita("consulta");
        reserva.setFechaCita(LocalDate.now().plusDays(1));
        reserva.setDisponibilidadCita("RESERVADA");

        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
        List<ReservaResponseDTO> resultados = reservaService.listarReservas();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("consulta", resultados.get(0).getTipoCita());
        verify(reservaRepository, times(1)).findAll();
    }
    @Test
    void buscarPorId(){
        Reserva reserva = new Reserva();
        reserva.setIdCita(1L);
        reserva.setIdUsuario(10L);
        reserva.setIdEmp(20L);
        reserva.setTipoCita("control");
        reserva.setFechaCita(LocalDate.now().plusDays(1));
        reserva.setDisponibilidadCita("RESERVADA");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<ReservaResponseDTO> resultado = reservaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdCita());
        assertEquals("control", resultado.get().getTipoCita());

        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void guardar(){
        ReservaRequestDTO reservaRequest = new ReservaRequestDTO();
        reservaRequest.setIdUsuario(10L);
        reservaRequest.setIdEmp(20L);
        reservaRequest.setTipoCita("vacunacion");
        reservaRequest.setFechaCita(LocalDate.now().plusDays(1));

        Reserva reserva = new Reserva();
        reserva.setIdCita(1L);
        reserva.setIdUsuario(10L);
        reserva.setIdEmp(20L);
        reserva.setTipoCita("vacunacion");
        reserva.setFechaCita(LocalDate.now().plusDays(1));
        reserva.setDisponibilidadCita("RESERVADA");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaResponseDTO resultado = reservaService.guardar(reservaRequest);

        assertNotNull(resultado);
        assertEquals("vacunacion", resultado.getTipoCita());
        assertEquals("RESERVADA", resultado.getDisponibilidadCita());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }
    @Test
    void cancelarPorId(){
        Reserva reserva = new Reserva();
        reserva.setIdCita(1L);
        reserva.setIdUsuario(10L);
        reserva.setIdEmp(20L);
        reserva.setTipoCita("consulta");
        reserva.setFechaCita(LocalDate.now().plusDays(1));
        reserva.setDisponibilidadCita("RESERVADA");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Optional<ReservaResponseDTO> resultado = reservaService.cancelarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("CANCELADA", resultado.get().getDisponibilidadCita());

        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
    }
}
