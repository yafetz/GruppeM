package com.example.demo12.repository;

import com.example.demo12.entities.Lehrender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LehrenderRepository extends JpaRepository<Lehrender,Integer> {

}
