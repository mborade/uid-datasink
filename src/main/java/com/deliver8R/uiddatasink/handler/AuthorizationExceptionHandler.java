package com.deliver8R.uiddatasink.handler;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.deliver8R.uiddatasink.AuthorizationException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AuthorizationExceptionHandler implements ExceptionMapper<AuthorizationException>{
		
@Override	
public Response toResponse(AuthorizationException exception) {				
		
		return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(exception.getMessage()).build();
	}

}