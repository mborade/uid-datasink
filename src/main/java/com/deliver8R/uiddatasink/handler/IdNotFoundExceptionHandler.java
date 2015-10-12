package com.deliver8R.uiddatasink.handler;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.deliver8R.uiddatasink.IdNotFoundException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class IdNotFoundExceptionHandler implements ExceptionMapper<IdNotFoundException>{
		
@Override	
public Response toResponse(IdNotFoundException exception) {				
		
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(exception.getMessage()).build();
	}

}
