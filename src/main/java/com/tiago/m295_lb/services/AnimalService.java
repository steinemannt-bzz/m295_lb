package com.tiago.m295_lb.services;

import com.tiago.m295_lb.models.Animal;
import com.tiago.m295_lb.models.Keeper;
import com.tiago.m295_lb.repositories.IAnimalRepository;
import com.tiago.m295_lb.repositories.IKeeperRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("/animals")
public class AnimalService {

    private final Logger logger = LogManager.getLogger(AnimalService.class);
    private final IAnimalRepository animalRepository;
    private final IKeeperRepository keeperRepository;

    @Autowired
    public AnimalService(IAnimalRepository animalRepository, IKeeperRepository keeperRepository) {
        this.animalRepository = animalRepository;
        this.keeperRepository = keeperRepository;
    }


    @GET
    @Path("/get/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Animal getAnimalById(@PathParam("animalId") Integer animalId) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new NotFoundException("Animal with Id " + animalId + " not found");
        }
        logger.info("Fetched animal with Id: {}", animalId);
        return animal;
    }

    @GET
    @Path("/exists/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean checkIfAnimalExists(@PathParam("animalId") Integer animalId) {
        if (!animalRepository.existsById(animalId)) {
            logger.warn("Animal with Id: {} doesnt exist!", animalId);
            return false;
        }
        logger.info("Animal with Id: {} exists!", animalId);
        return true;
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Animal> getAllAnimals() {
        List<Animal> animals = animalRepository.findAll();
        logger.info("Fetched all animals");
        return animals;
    }

    @GET
    @Path("/filter/date/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Animal> getAnimalsByDate(@PathParam("date") String date) {
        LocalDate filterDate = LocalDate.parse(date);
        return animalRepository.findByDateAcquired(filterDate);
    }

    @GET
    @Path("/filter/text/{text}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Animal> getAnimalsByText(@PathParam("text") String text) {
        return animalRepository.findByNameContainingIgnoreCaseOrSpeciesContainingIgnoreCaseOrHabitatContainingIgnoreCase(text, text, text);
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getAnimalCount() {
        long animalCount = animalRepository.count();
        logger.info("Fetched animal count of {}", animalCount);
        return animalCount;
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAnimal(Map<String, Object> request) {
        try {
            Animal animal = createAnimalFromRequest(request);
            animalRepository.save(animal);
            logger.info("Created a new animal: {}", animal);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            logger.error("Error creating animal: ", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/createMultiple")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMultipleAnimals(List<Map<String, Object>> requests) {
        List<Animal> animals = new ArrayList<>();
        try {
            for (Map<String, Object> request : requests) {
                Animal animal = createAnimalFromRequest(request);
                animals.add(animal);
            }
            animalRepository.saveAll(animals);
            logger.info("Created new animals: {}", animals);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            logger.error("Error creating animals: ", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private Animal createAnimalFromRequest(Map<String, Object> request) throws Exception {
        String name = (String) request.get("name");
        String species = (String) request.get("species");
        LocalDate dateAcquired = LocalDate.parse((String) request.get("dateAcquired"));
        BigDecimal weight = new BigDecimal((String) request.get("weight"));
        String habitat = (String) request.get("habitat");
        Boolean isEndangered = (Boolean) request.get("isEndangered");
        Integer keeperId = (Integer) request.get("keeperId");

        Keeper keeper = keeperRepository.findById(keeperId).orElseThrow(() -> new NotFoundException("Keeper with Id " + keeperId + " not found"));

        return new Animal(name, species, dateAcquired, weight, habitat, isEndangered, keeper);
    }
}
