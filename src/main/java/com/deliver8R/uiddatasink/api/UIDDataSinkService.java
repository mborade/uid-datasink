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

import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.model.UIDData;


@Path("/datasink")
public interface UIDDataSinkService {
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response addUIDData(UIDData uidData) throws DataSinkException; 
	
	
	@GET
    @Path("/id/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
	public UIDData getUIDData(@PathParam("id") String id) throws DataSinkException;
	
	@PUT
    @Path("")
    @Consumes (MediaType.APPLICATION_JSON)
	public Response updateUIDData(UIDData uidData) throws DataSinkException;
	
	@DELETE
	@Path("/id/{id}")
	@Produces (MediaType.APPLICATION_JSON)
	public Response deleteUIDData(@PathParam("id") String id) throws DataSinkException;
	
}
