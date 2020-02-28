package com.test.google.speech.api.calls;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class CloudStorage {
	
	public void storeFile(String projectId, String bucketName, String objectName, String filePath) {
		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, objectName);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
			
			storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
			//storage.delete(blobId);
			
			System.out.println(
			        "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void deleteFile(String projectId, String bucketName, String objectName, String filePath) {
		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, objectName);
			
			//storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
			storage.delete(blobId);
			
			System.out.println(
			        "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
