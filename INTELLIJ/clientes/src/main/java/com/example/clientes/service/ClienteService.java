package com.example.clientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clientes.model.Cliente;
import com.example.clientes.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    //metodo para obtener todos los clientes
    public List<Cliente> getClientes(){
        return clienteRepository.findAll();
    }

    //metodo para obtener un cliente mediante su id
    public Cliente getCliente(Long id){
        return clienteRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
    }

    //metodo para crear un cliente
    public Cliente saveCliente(Cliente cliente){
        return clienteRepository.save(cliente);

    }

}
