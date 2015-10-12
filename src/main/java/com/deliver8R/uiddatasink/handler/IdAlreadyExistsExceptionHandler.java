package com.deliver8R.uiddatasink.handler;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.deliver8R.uiddatasink.IdAlreadyExistsException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class IdAlreadyExistsExceptionHandler implements ExceptionMapper<IdAlreadyExistsException>{
		
@Override	
public Response toResponse(IdAlreadyExistsException exception) {				
		
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(exception.getMessage()).build();
	}

}