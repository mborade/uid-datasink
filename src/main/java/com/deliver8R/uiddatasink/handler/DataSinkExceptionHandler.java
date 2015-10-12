package com.deliver8R.uiddatasink.handler;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.deliver8R.uiddatasink.DataSinkException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class DataSinkExceptionHandler implements ExceptionMapper<DataSinkException>{

	@Override
	public Response toResponse(DataSinkException exception) {				
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("Internal server error").build();
	}

}
