package com.tiago.m295_lb;

import com.tiago.m295_lb.exceptions.BadRequestExceptionHandler;
import com.tiago.m295_lb.exceptions.InternalServerErrorExceptionHandler;
import com.tiago.m295_lb.exceptions.NotFoundExceptionHandler;
import com.tiago.m295_lb.exceptions.ConstraintViolationExceptionHandler;
import com.tiago.m295_lb.services.AnimalService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
                AnimalService.class,
                NotFoundExceptionHandler.class,
                ConstraintViolationExceptionHandler.class,
                BadRequestExceptionHandler.class,
                InternalServerErrorExceptionHandler.class));
    }
}
