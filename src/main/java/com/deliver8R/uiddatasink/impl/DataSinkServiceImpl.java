package com.deliver8R.uiddatasink.impl;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deliver8R.uiddatasink.AuthorizationException;
import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.DataSinkService;
import com.deliver8R.uiddatasink.IdAlreadyExistsException;
import com.deliver8R.uiddatasink.IdNotFoundException;
import com.deliver8R.uiddatasink.model.IdentifierData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DataSinkServiceImpl implements DataSinkService {
	
	private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";
	private static final Logger logger = LoggerFactory.getLogger(DataSinkServiceImpl.class);
	private SecretKey secretKey;
	private Cipher cipher;
	private String rootPath = "";
	private String password = "secretKey";
	private PBEParameterSpec pbeParameterSpec;
	
		public DataSinkServiceImpl(String rootPath) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
		{
			this.rootPath = rootPath;
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
			secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

			byte[] salt = FileUtils.readFileToByteArray(new File(rootPath+"/salt"));
			
			pbeParameterSpec = new PBEParameterSpec(salt, 100);

			cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		}
		    	 
		@Override
	    public IdentifierData getIdentifierData(String uid) throws DataSinkException, IdNotFoundException {
	    	logger.info("----invoking getInfo, uid is: " + uid);
      
	        try {

	        	byte[] byteArr = null;
				
	        	try {
					
					if(!checkIfIdExists(uid))
		        	{
		        		logger.error("UID "+uid+" does not exist");
						throw new IdNotFoundException("UID "+uid+" does not exist");
		        	}
					
					byteArr = FileUtils.readFileToByteArray(new File(rootPath+"/"+uid));
					
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new DataSinkException(e.getMessage(), e);
				}
				
				try {
					cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
				} catch (InvalidAlgorithmParameterException e) {
					logger.error(e.getMessage(), e);
					throw new DataSinkException(e.getMessage(), e);
				}
				
				try {
					Base64.Decoder decoder = Base64.getDecoder();
					
					byte[] resultBytes = cipher.doFinal(decoder.decode(byteArr));
					
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.readValue(resultBytes, IdentifierData.class);
					} catch (JsonParseException e) {

						logger.error(e.getMessage(), e);
						throw new DataSinkException(e.getMessage(), e);
					} catch (JsonMappingException e) {

						logger.error(e.getMessage(), e);
						throw new DataSinkException(e.getMessage(), e);
					} catch (IOException e) {

						logger.error(e.getMessage(), e);
						throw new DataSinkException(e.getMessage(), e);
					}
				} catch (IllegalBlockSizeException e) {

					logger.error(e.getMessage(), e);
					throw new DataSinkException(e.getMessage(), e);
				} catch (BadPaddingException e) {

					logger.error(e.getMessage(), e);
					throw new DataSinkException(e.getMessage(), e);
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}

	    }

		
		private boolean checkIfIdExists(String id) throws IOException {	        
			File f = new File(rootPath, id);
			return f.exists();	        
		}
	    
	    
	    
	    @Override
	    public void updateIdentifierData(String uid, IdentifierData identifierData) throws IdNotFoundException, AuthorizationException, DataSinkException {
	    	logger.info("----invoking updateUIDInfo uid is: " + identifierData.getIdentifier());
	    	
	    	try {
				if (!checkIfIdExists(uid))
				{
					logger.error("UID "+uid+" does not exist");
					throw new IdNotFoundException("UID "+uid+" does not exist");
				}								
				
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
	    	
	    	ObjectMapper mapper = new ObjectMapper();	       
	        String output;
	        
			try {
				output = mapper.writeValueAsString(identifierData);
			} catch (JsonProcessingException e2) {
				logger.error(e2.getMessage(), e2);
				throw new DataSinkException(e2.getMessage(), e2);
			}
	    	
	    	String encryptedText = encryptData(output);
	        
	    	try {
				FileUtils.moveFile(new File(rootPath, uid), new File(rootPath+"/tmp", uid));
				writeDataToFile(uid, encryptedText);
				FileUtils.deleteQuietly(new File(rootPath+"/tmp", uid));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        
	        
	       logger.info(output);
	       
	       return;
	    }


	    @Override
	    public String addIdentifierData(IdentifierData identifierData) throws DataSinkException, AuthorizationException, IdAlreadyExistsException {
	    	logger.info("----invoking addUIDInfo, uid info: " + identifierData.getAdditionalData());
	        
	        String hashedId = new String(DigestUtils.sha1Hex(identifierData.getIdentifier()));
	        try {
				if (checkIfIdExists(hashedId))
				{
					logger.error("Identifier "+identifierData.getIdentifier()+" already exists");
					throw new IdAlreadyExistsException("Identifier "+identifierData.getIdentifier()+" already exists");
				}
	        }catch (IOException e) {
	        	logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
	        
	        ObjectMapper mapper = new ObjectMapper();	       
	        String output;
	        
			try {
				output = mapper.writeValueAsString(identifierData);
			} catch (JsonProcessingException e2) {
				logger.error(e2.getMessage(), e2);
				throw new DataSinkException(e2.getMessage(), e2);
			}
			
	        String encryptedText = encryptData(output);
	        
	        writeDataToFile(hashedId, encryptedText);
	        
	       logger.info(output);
	        
	       return hashedId;
	    }

	    
		private void writeDataToFile(String fileName, String encryptedText) throws DataSinkException {
			
			File file = new File(rootPath, fileName);
			file.setExecutable(true, false);
			file.setWritable(false);			
			
			try {				
				FileUtils.writeStringToFile(file, encryptedText);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
		}
	    

		private String encryptData(String output) throws DataSinkException {
			try {
				try {
					cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
				} catch (InvalidAlgorithmParameterException e1) {
					logger.error(e1.getMessage(), e1);
					throw new DataSinkException(e1.getMessage(), e1);
				}
			} catch (InvalidKeyException e1) {
				logger.error(e1.getMessage(), e1);
				throw new DataSinkException(e1.getMessage(), e1);
			}
	        
	        byte[] bytes = null;
			
	        try {
				bytes = cipher.doFinal(output.getBytes());
			} catch (IllegalBlockSizeException e1) {
				logger.error(e1.getMessage(), e1);
				throw new DataSinkException(e1.getMessage(), e1);
			} catch (BadPaddingException e1) {
				logger.error(e1.getMessage(), e1);
				throw new DataSinkException(e1.getMessage(), e1);
			}
				               	
			Base64.Encoder encoder = Base64.getEncoder();
	        String encryptedText = encoder.encodeToString(bytes);
			return encryptedText;
		}

		@Override
	    public void deleteIdentifierData(String uid) throws DataSinkException, IdNotFoundException, AuthorizationException {
			
			try {
				if(!checkIfIdExists(uid))
				{
					logger.error("UID "+uid+" does not exist");
					throw new IdNotFoundException("UID "+uid+" does not exist");
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
			
			FileUtils.deleteQuietly(new File(rootPath+"/"+uid));
			
		}

	    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, DataSinkException, IOException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, AuthorizationException, IdNotFoundException, IdAlreadyExistsException
	    {
	    	DataSinkService dataSinkService = new DataSinkServiceImpl("/Users/tkmablj/uid-data");
	    	
	    	IdentifierData uidData = new IdentifierData();
	   
	    	Map<String, String> dataMap = new HashMap<String, String>();
	    	dataMap.put("fname", "John");
	    	dataMap.put("mname", "E");
	    	dataMap.put("lname", "Doe");
	    	uidData.setAdditionalData(dataMap);
	    	uidData.setIdentifier("ident6");
	    	
	    	dataSinkService.updateIdentifierData("0b9bfa4c2a3ba37531fc52d5ccf3496d4f6c4e590", uidData);
	    	
//	    	dataSinkService.addIdentifierData(uidData);
//	    	IdentifierData result = dataSinkService.getIdentifierData(uid);	    	
//	    		
//	    	System.out.println(result.getIdentifier());
//	    	System.out.println(result.getAdditionalData());

	    }
}
