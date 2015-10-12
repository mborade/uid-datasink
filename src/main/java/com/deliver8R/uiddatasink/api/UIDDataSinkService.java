package com.deliver8R.uiddatasink.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.deliver8R.uiddatasink.AuthorizationException;
import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.IdAlreadyExistsException;
import com.deliver8R.uiddatasink.IdNotFoundException;
import com.deliver8R.uiddatasink.model.IdentifierData;


@Path("/datasink")
public interface UIDDataSinkService {
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response addIdentifierData(IdentifierData uidData) throws DataSinkException, AuthorizationException, IdAlreadyExistsException; 
	
	
	@GET
    @Path("/id/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
	public IdentifierData getIdentifierData(@PathParam("id") String id) throws DataSinkException, IdNotFoundException, AuthorizationException;
	
	@PUT
    @Path("/id/{id}")
    @Consumes (MediaType.APPLICATION_JSON)
	public Response updateIdentifierData(@PathParam("id") String id, IdentifierData uidData) throws DataSinkException, IdNotFoundException, AuthorizationException;
	
	@DELETE
	@Path("/id/{id}")
	@Produces (MediaType.APPLICATION_JSON)
	public Response deleteIdentifierData(@PathParam("id") String id) throws DataSinkException, AuthorizationException, IdNotFoundException;
	
}
