package com.parcialdos.ciudad.Service;

import com.parcialdos.ciudad.Model.Ciudad;
import com.parcialdos.ciudad.Repository.CityRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CityService {
@Autowired
private CityRepo TheRepo;


public List<Ciudad> findall(){
return    TheRepo.findAll();
};

public void delete(Long ID){
    TheRepo.deleteById(ID);
}
public Ciudad save(Ciudad city){
    return TheRepo.save(city);
}

}
