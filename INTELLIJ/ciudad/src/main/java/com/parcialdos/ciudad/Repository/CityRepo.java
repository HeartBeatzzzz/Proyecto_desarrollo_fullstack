package com.parcialdos.ciudad.Repository;

import com.parcialdos.ciudad.Model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends JpaRepository<Ciudad,Long> {

}
