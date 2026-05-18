package com.parcialdos.ciudades.Service;

import com.parcialdos.ciudades.Model.Ciudad;
import com.parcialdos.ciudades.Repository.CiudadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CiudadService  {
@Autowired
    private CiudadRepository TheRepo;

    public List<Ciudad> findall(){
        return TheRepo.findAll();
    };

    public Ciudad FindById(long Id){
        return TheRepo.findById(Id).get();
    };
    public Ciudad save(Ciudad emp){
        return TheRepo.save(emp);
    };
    public void delete(Long id)
    {TheRepo.deleteById(id);}

}
