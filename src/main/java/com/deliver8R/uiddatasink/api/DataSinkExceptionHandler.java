package com.deliver8R.uiddatasink.api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.deliver8R.uiddatasink.DataSinkException;

public class DataSinkExceptionHandler implements ExceptionMapper<DataSinkException>{

	public Response toResponse(DataSinkException arg0) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("Internal server error").build();
	}
	
}
