package com.tiago.m295_lb.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    private final Logger logger = LogManager.getLogger(NotFoundExceptionHandler.class);

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation errors:");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append("\n- ").append(violation.getMessage());
        }

        logger.warn(errorMessage.toString());
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
    }
}
