package com.deliver8R.uiddatasink.api;

import javax.ws.rs.core.Response;

import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.DataSinkService;
import com.deliver8R.uiddatasink.model.UIDData;


public class UIDDataSinkServiceImpl implements UIDDataSinkService {
	
	DataSinkService dataSinkService;

	public Response addUIDData(UIDData uidData) {
		
		try {
			
			String uid = dataSinkService.addUIDData(uidData);
			
			return Response.ok().entity(uid).build();
			
		} 
		catch (DataSinkException e) 
		{
			return Response.serverError().entity(e.getMessage()).build();
		} 
		
	}

	public UIDData getUIDData(String id) throws DataSinkException {
		
		return dataSinkService.getUIDData(id);	
	}

	
	public Response updateUIDData(UIDData uidData) throws DataSinkException {
		
		dataSinkService.updateUIDData(uidData);
		return Response.ok().build();
	
	}

	
	public Response deleteUIDData(String id) {
		// TODO Auto-generated method stub
		return null;
	}


	public void setDataSinkService(DataSinkService dataSinkService) {
		this.dataSinkService = dataSinkService;
	}
	
	

}
