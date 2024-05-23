package com.tiago.m295_lb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer animalId;

    @Length(max = 100)
    @NotNull(message = "Name cannot be null.")
    private String name;

    @Length(max = 100)
    @NotNull(message = "Species cannot be null.")
    private String species;

    @PastOrPresent(message = "Acquire date must be in the past or present.")
    private LocalDate dateAcquired;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    @Length(max = 100)
    private String habitat;

    private Boolean isEndangered;

    @ManyToOne
    @JoinColumn(name = "keeperId", nullable = false)
    private Keeper keeper;

    public Animal() {
    }

    public Animal(String name, String species, LocalDate dateAcquired, BigDecimal weight, String habitat, Boolean isEndangered, Keeper keeper) {
        this.name = name;
        this.species = species;
        this.dateAcquired = dateAcquired;
        this.weight = weight;
        this.habitat = habitat;
        this.isEndangered = isEndangered;
        this.keeper = keeper;
    }

    public Integer getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Integer animalId) {
        this.animalId = animalId;
    }

    public @Length(max = 100) @NotNull(message = "Name cannot be null.") String getName() {
        return name;
    }

    public void setName(@Length(max = 100) @NotNull(message = "Name cannot be null.") String name) {
        this.name = name;
    }

    public @Length(max = 100) @NotNull(message = "Species cannot be null.") String getSpecies() {
        return species;
    }

    public void setSpecies(@Length(max = 100) @NotNull(message = "Species cannot be null.") String species) {
        this.species = species;
    }

    public @PastOrPresent(message = "Acquire date must be in the past or present.") LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(@PastOrPresent(message = "Acquire date must be in the past or present.") LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public @Length(max = 100) String getHabitat() {
        return habitat;
    }

    public void setHabitat(@Length(max = 100) String habitat) {
        this.habitat = habitat;
    }

    public Boolean getIsEndangered() {
        return isEndangered;
    }

    public void setIsEndangered(Boolean isEndangered) {
        this.isEndangered = isEndangered;
    }

    public Keeper getKeeper() {
        return keeper;
    }

    public void setKeeper(Keeper keeper) {
        this.keeper = keeper;
    }
}
