package com.reservas.reservas.service;

import com.reservas.reservas.dto.ReservaRequestDTO;
import com.reservas.reservas.dto.ReservaResponseDTO;
import com.reservas.reservas.model.Reserva;
import com.reservas.reservas.repository.ReservaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {


    private final ReservaRepository reservaRepository;

    private ReservaResponseDTO mapToDTO(Reserva reserva){
        return new ReservaResponseDTO(
                reserva.getIdCita(),
                reserva.getTipoCita(),
                reserva.getFechaCita(),
                String.valueOf(reserva.getIdEmp()),
                reserva.getDisponibilidadCita()
        );


    }

    public ReservaResponseDTO guardar(ReservaRequestDTO dto){
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(dto.getIdUsuario());
        reserva.setIdEmp(dto.getIdEmp());
        reserva.setFechaCita(dto.getFechaCita());
        reserva.setTipoCita(dto.getTipoCita());
        reserva.setDisponibilidadCita("RESERVADA");
        log.info(">>>>> Reserva creada con exito <<<<<");

        return mapToDTO(reservaRepository.save(reserva));

    }




public List<ReservaResponseDTO> listarReservas(){
        return reservaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

public Optional<ReservaResponseDTO> buscarPorId(Long idCita){
        return reservaRepository
                .findById(idCita)
                .map(this::mapToDTO);


    }

public List<ReservaResponseDTO> buscarPorUsuario(Long idUsuario){
        return reservaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(this::mapToDTO)
                .collect((Collectors.toList()));
}

public List<ReservaResponseDTO> buscarPorEmpleado(Long idEmp){
        return reservaRepository.findByIdEmp(idEmp)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

}

public List<ReservaResponseDTO> buscarPorDisponibilidad(String disponibilidadCita){
        return reservaRepository.findByDisponibilidadCita(disponibilidadCita)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
}

    @Transactional
    public Optional<ReservaResponseDTO> cancelarPorId( Long idCita){
        return reservaRepository.findById(idCita)
                .map(reserva -> {reserva.setDisponibilidadCita("CANCELADA");
                log.info("Cita cancelada");
                return mapToDTO(reservaRepository.save(reserva));
                });

    }




}
