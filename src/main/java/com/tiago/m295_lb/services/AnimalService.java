package com.tiago.m295_lb.services;

import com.tiago.m295_lb.models.Animal;
import com.tiago.m295_lb.models.Keeper;
import com.tiago.m295_lb.repositories.IAnimalRepository;
import com.tiago.m295_lb.repositories.IKeeperRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Path("/animals")
public class AnimalService {

    private final Logger logger = LogManager.getLogger(AnimalService.class);
    private final IAnimalRepository animalRepository;
    private final IKeeperRepository keeperRepository;
    private final DataSource dataSource;

    @Autowired
    public AnimalService(IAnimalRepository animalRepository, IKeeperRepository keeperRepository, DataSource dataSource) {
        this.animalRepository = animalRepository;
        this.keeperRepository = keeperRepository;
        this.dataSource = dataSource;
    }

    @PermitAll
    @GET
    @Path("/get/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalById(@PathParam("animalId") Integer animalId) {
        try {
            Optional<Animal> animal = animalRepository.findById(animalId);
            if (animal.isPresent()) {
                logger.info("Fetched animal with Id: {}", animalId);
                return Response.status(Response.Status.OK).entity(animal).build();
            } else {
                throw new NotFoundException("Animal with Id: " + animalId + " not found");
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PermitAll
    @GET
    @Path("/exists/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean checkIfAnimalExists(@PathParam("animalId") Integer animalId) {
        try {
            boolean exists = animalRepository.existsById(animalId);
            if (exists) {
                logger.info("Animal with Id: {} exists", animalId);
            } else {
                logger.warn("Animal with Id: {} doesn't exist", animalId);
            }
            return exists;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PermitAll
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnimals() {
        try {
            List<Animal> animals = animalRepository.findAll();
            if (animals.isEmpty()) {
                logger.info("No animals found");
                return Response.status(Response.Status.NO_CONTENT).entity("No animals found").build();
            } else {
                logger.info("Fetched all animals");
                return Response.status(Response.Status.OK).entity(animals).build();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PermitAll
    @GET
    @Path("/filter/date/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalsByDate(@PathParam("date") String date) {
        try {
            LocalDate filterDate = LocalDate.parse(date);
            List<Animal> animals = animalRepository.findByDateAcquired(filterDate);
            if (animals.isEmpty()) {
                logger.info("Animal(s) with date filter: {} not found", date);
                return Response.status(Response.Status.NO_CONTENT).entity("Animal(s) with date filter: " + date + " not found").build();
            } else {
                logger.info("Fetched animal(s) with date filter: {}", date);
                return Response.status(Response.Status.OK).entity(animals).build();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PermitAll
    @GET
    @Path("/filter/text/{text}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalsByText(@PathParam("text") String text) {
        try {
            List<Animal> animals = animalRepository.findByNameContainingIgnoreCaseOrSpeciesContainingIgnoreCaseOrHabitatContainingIgnoreCase(text, text, text);
            if (animals.isEmpty()) {
                logger.info("Animal(s) with text filter: {} not found", text);
                return Response.status(Response.Status.NO_CONTENT).entity("Animal(s) with text filter: " + text + " not found").build();
            } else {
                logger.info("Fetched animal(s) with text filter: {}", text);
                return Response.status(Response.Status.OK).entity(animals).build();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PermitAll
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalCount() {
        try {
            long animalCount = animalRepository.count();
            logger.info("Fetched animal count of: {}", animalCount);
            return Response.status(Response.Status.OK).entity(animalCount).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @RolesAllowed({"ADMIN"})
    @POST
    @Path("/createTables")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createTables() {
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("Zoo.sql"));

            logger.info("Tables created successfully");
            return Response.status(Response.Status.OK).entity("Tables created successfully").build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAnimal(Map<String, Object> request) {
        try {
            Animal animal = new Animal();
            populateAnimalFromRequest(animal, request);
            animalRepository.save(animal);
            logger.info("Created a new animal: {}", animal);
            return Response.status(Response.Status.CREATED).build();
        } catch (ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    @POST
    @Path("/createMultiple")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMultipleAnimals(List<Map<String, Object>> requests) {
        try {
            List<Animal> animals = new ArrayList<>();
            for (Map<String, Object> request : requests) {
                Animal animal = new Animal();
                populateAnimalFromRequest(animal, request);
                animals.add(animal);
            }
            animalRepository.saveAll(animals);
            logger.info("Created new animals: {}", animals);
            return Response.status(Response.Status.CREATED).build();
        } catch (ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    @PUT
    @Path("/update/{animalId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAnimal(@PathParam("animalId") Integer animalId, Map<String, Object> request) {
        try {
            Optional<Animal> optionalAnimal = animalRepository.findById(animalId);
            if (optionalAnimal.isEmpty()) {
                throw new NotFoundException("Animal with Id: " + animalId + " not found");
            }

            Animal existingAnimal = optionalAnimal.get();
            populateAnimalFromRequest(existingAnimal, request);

            animalRepository.save(existingAnimal);
            logger.info("Updated animal with Id: {}", animalId);
            return Response.status(Response.Status.OK).entity(existingAnimal).build();
        } catch (NotFoundException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/delete/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAnimalById(@PathParam("animalId") Integer animalId) {
        try {
            if (animalRepository.existsById(animalId)) {
                animalRepository.deleteById(animalId);
                logger.info("Deleted animal with Id: {}", animalId);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                throw new NotFoundException("Animal with Id: " + animalId + " not found");
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/deleteAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllAnimals() {
        try {
            long animalCount = animalRepository.count();
            if (animalCount > 0) {
                animalRepository.deleteAll();
                logger.info("Deleted {} animals", animalCount);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                logger.info("No animals found");
                return Response.status(Response.Status.NO_CONTENT).entity("No animals found").build();
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private void populateAnimalFromRequest(Animal animal, Map<String, Object> request) {
        try {
            if (request.containsKey("name")) {
                animal.setName((String) request.get("name"));
            }
            if (request.containsKey("species")) {
                animal.setSpecies((String) request.get("species"));
            }
            if (request.containsKey("dateAcquired")) {
                animal.setDateAcquired(LocalDate.parse((String) request.get("dateAcquired")));
            }
            if (request.containsKey("weight")) {
                animal.setWeight(new BigDecimal((String) request.get("weight")));
            }
            if (request.containsKey("habitat")) {
                animal.setHabitat((String) request.get("habitat"));
            }
            if (request.containsKey("isEndangered")) {
                animal.setIsEndangered((Boolean) request.get("isEndangered"));
            }
            if (request.containsKey("keeperId")) {
                Integer keeperId = (Integer) request.get("keeperId");
                Optional<Keeper> keeper = keeperRepository.findById(keeperId);
                if (keeper.isPresent()) {
                    animal.setKeeper(keeper.get());
                } else {
                    throw new NotFoundException("Keeper with Id " + keeperId + " not found");
                }
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
