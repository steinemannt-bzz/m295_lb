package com.tiago.m295_lb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "keeper")
public class Keeper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer keeperId;

    @Length(max = 50)
    @NotNull(message = "Firstname cannot be null.")
    private String firstname;

    @Length(max = 50)
    @NotNull(message = "Lastname cannot be null.")
    private String lastname;

    @JsonIgnore
    @OneToMany(mappedBy = "keeper", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals;

    public Keeper() {
    }

    public Keeper(String firstname, String lastname, List<Animal> animals) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.animals = animals;
    }

    public Integer getKeeperId() {
        return keeperId;
    }

    public void setKeeperId(Integer keeperId) {
        this.keeperId = keeperId;
    }

    public @Length(max = 50) @NotNull(message = "Lastname cannot be null.") String getLastname() {
        return lastname;
    }

    public void setLastname(@Length(max = 50) @NotNull(message = "Lastname cannot be null.") String lastname) {
        this.lastname = lastname;
    }

    public @Length(max = 50) @NotNull(message = "Firstname cannot be null.") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@Length(max = 50) @NotNull(message = "Firstname cannot be null.") String firstname) {
        this.firstname = firstname;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
