package com.tiago.m295_lb.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {

    private final Logger logger = LogManager.getLogger(InternalServerErrorExceptionHandler.class);

    @Override
    public Response toResponse(BadRequestException ex) {
        logger.warn(ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
    }
}
