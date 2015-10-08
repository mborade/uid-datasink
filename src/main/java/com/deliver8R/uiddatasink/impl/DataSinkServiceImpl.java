package com.deliver8R.uiddatasink.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deliver8R.uiddatasink.DataSinkException;
import com.deliver8R.uiddatasink.DataSinkService;
import com.deliver8R.uiddatasink.model.UIDData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DataSinkServiceImpl implements DataSinkService {
	
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
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

			byte[] salt = FileUtils.readFileToByteArray(new File(rootPath+"/salt"));
			
			pbeParameterSpec = new PBEParameterSpec(salt, 100);

			cipher = Cipher.getInstance("PBEWithMD5AndDES");
		}
		    	 
	    public UIDData getUIDData(String id) throws DataSinkException {
	    	logger.info("----invoking getInfo, id is: " + id);
	        
	        File file = FileUtils.getFile(rootPath+"/"+id);
	        if (file == null)
	        {
	        	logger.error("File "+id+" not found");
	        	throw new DataSinkException("id "+id +" not found");
	        }
	        
	        try {
	        	byte[] byteArr = null;
				try {
					byteArr = FileUtils.readFileToByteArray(file);
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
						return mapper.readValue(resultBytes, UIDData.class);
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
	    
	    
	    
	    
	    public void updateUIDData(UIDData uidInfo) {
	    	logger.info("----invoking updateUIDInfo uid is: " + uidInfo.getUid());
	        Map infoMap = uidInfo.getKeyValueMap();
	        Response r;
	
	        return;
	    }


	    
	    public String addUIDData(UIDData uidData) throws DataSinkException {
	    	logger.info("----invoking addUIDInfo, uid info: " + uidData.getKeyValueMap());
	        
	        UUID uid = UUID.randomUUID();
	        ObjectMapper mapper = new ObjectMapper();
	        uidData.setUid(uid.toString());
	        String output;
	        
			try {
				output = mapper.writeValueAsString(uidData);
			} catch (JsonProcessingException e2) {
				logger.error(e2.getMessage(), e2);
				throw new DataSinkException(e2.getMessage(), e2);
			}
			
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
	        File file = new File(rootPath, uid.toString());
	        FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(file);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
			
	        try {
				fileWriter.write(encryptedText);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataSinkException(e.getMessage(), e);
			}
	        finally
	        {
	        	try {
	        		if (fileWriter != null)
	        			fileWriter.close(); 
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new DataSinkException(e.getMessage(), e);
				}
	        }
	        
	       logger.info(output);
	        
	        return uid.toString();
	    }

		
	    public void deleteUIDData(String id) throws DataSinkException {
			// TODO Auto-generated method stub
			
		}

	    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, DataSinkException, IOException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	    {
	    	DataSinkService dataSinkService = new DataSinkServiceImpl("/Users/tkmablj/uid-data");
	    	
	    	UIDData uidData = new UIDData();
	   
	    	Map<String, String> dataMap = new HashMap<String, String>();
	    	dataMap.put("fname", "John");
	    	dataMap.put("mname", "E");
	    	dataMap.put("lname", "Doe");
	    	uidData.setKeyValueMap(dataMap);
	    	
	    	String uid = dataSinkService.addUIDData(uidData);
	    	
	    	UIDData result = dataSinkService.getUIDData(uid);
	    	
	    	System.out.println(result.getUid());
	    	System.out.println(result.getKeyValueMap());
	    	
//	    	byte[] salt = new byte[8];
//			Random random = new Random();
//			random.nextBytes(salt);
//			FileUtils.writeByteArrayToFile(new File(rootPath+"/salt"), salt);
	    }
}
