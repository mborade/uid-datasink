package com.deliver8R.uiddatasink;

import com.deliver8R.uiddatasink.model.UIDData;

public interface DataSinkService {
			
	    public UIDData getUIDData(String id) throws DataSinkException; 
	    	 
	    public void updateUIDData(UIDData uidData) throws DataSinkException;
	    
	    public String addUIDData(UIDData uidData) throws DataSinkException; 
	    
	    public void deleteUIDData(String id) throws DataSinkException;

}
