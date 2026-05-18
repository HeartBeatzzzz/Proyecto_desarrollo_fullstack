package com.parcialdos.empleados.Service;

import com.parcialdos.empleados.DTO.EmpleadoDTO;
import com.parcialdos.empleados.Model.Empleados;
import com.parcialdos.empleados.Repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpleadoService {

    private final EmpleadoRepository TheRepo;
    public List<EmpleadoDTO> findAll(){

        return TheRepo.findAll().stream()
                .map(Empleados -> new EmpleadoDTO(Empleados.getId_emp(), Empleados.getNombre_emp(), Empleados.getEspecialidad(), Empleados.getTelefono_emp(), Empleados.getEmail_emp()))
                .collect(Collectors.toList());
    };
    public Empleados FindById(long Id){
        return TheRepo.findById(Id).get();
    };
    public Empleados save(Empleados emp){
        return TheRepo.save(emp);
    };
    public void delete(Long id)
    {TheRepo.deleteById(id);}
}
