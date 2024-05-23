package com.tiago.m295_lb.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation errors:");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append("\n- ").append(violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
    }
}
