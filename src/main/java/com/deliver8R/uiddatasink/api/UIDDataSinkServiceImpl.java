package com.deliver8R.uiddatasink.api;

import javax.ws.rs.core.Response;

import com.deliver8R.uiddatasink.AuthorizationException;
import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.DataSinkService;
import com.deliver8R.uiddatasink.IdAlreadyExistsException;
import com.deliver8R.uiddatasink.IdNotFoundException;
import com.deliver8R.uiddatasink.model.IdentifierData;


public class UIDDataSinkServiceImpl implements UIDDataSinkService {
	
	DataSinkService dataSinkService;

	@Override
	public Response addIdentifierData(IdentifierData uidData) throws DataSinkException, AuthorizationException, IdAlreadyExistsException {
		
			String uid = dataSinkService.addIdentifierData(uidData);
			
			return Response.ok().entity(uid).build();		
	}

	@Override
	public IdentifierData getIdentifierData(String id) throws DataSinkException, IdNotFoundException, AuthorizationException {
		
		return dataSinkService.getIdentifierData(id);	
	}

	@Override
	public Response updateIdentifierData(String id, IdentifierData uidData) throws DataSinkException, IdNotFoundException, AuthorizationException {
		
		dataSinkService.updateIdentifierData(id, uidData);
		return Response.ok().build();
	
	}

	@Override
	public Response deleteIdentifierData(String id) throws DataSinkException, IdNotFoundException, AuthorizationException {
		dataSinkService.deleteIdentifierData(id);
		return Response.ok().build();
	}


	public void setDataSinkService(DataSinkService dataSinkService) {
		this.dataSinkService = dataSinkService;
	}
	
	

}
