package com.example.demo12.repository;

import com.example.demo12.entities.Lehrveranstaltung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LehrveranstaltungRepository extends JpaRepository<Lehrveranstaltung,Integer> {
}
