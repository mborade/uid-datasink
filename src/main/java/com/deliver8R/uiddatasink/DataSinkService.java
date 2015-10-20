package com.deliver8R.uiddatasink;

import com.deliver8R.uiddatasink.exception.AuthorizationException;
import com.deliver8R.uiddatasink.exception.DataSinkException;
import com.deliver8R.uiddatasink.exception.IdAlreadyExistsException;
import com.deliver8R.uiddatasink.exception.IdNotFoundException;
import com.deliver8R.uiddatasink.model.IdentifierData;

public interface DataSinkService {
			
	    public IdentifierData getIdentifierData(String uid) throws DataSinkException, IdNotFoundException, AuthorizationException; 
	    	 
	    public void updateIdentifierData(String uid, IdentifierData idData) throws DataSinkException, IdNotFoundException, AuthorizationException;
	    
	    public String addIdentifierData(IdentifierData idData) throws DataSinkException, AuthorizationException, IdAlreadyExistsException; 
	    
	    public void deleteIdentifierData(String uid) throws DataSinkException, IdNotFoundException, AuthorizationException;

}
