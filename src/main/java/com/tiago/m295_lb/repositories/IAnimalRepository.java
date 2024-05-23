package com.tiago.m295_lb.repositories;

import com.tiago.m295_lb.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IAnimalRepository extends JpaRepository<Animal, Integer> {

    List<Animal> findByDateAcquired(LocalDate dateAcquired);

    List<Animal> findByNameContainingIgnoreCaseOrSpeciesContainingIgnoreCaseOrHabitatContainingIgnoreCase(String name, String species, String habitat);
}
