package com.test.google.speech;

import java.util.Optional;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.test.google.speech.api.calls.CloudStorage;
import com.test.google.speech.api.calls.SpeechToText;
import com.test.google.speech.api.calls.TextToSpeech;
import com.test.google.speech.business.BusinessExtract;


@SpringBootApplication
public class SpeechApplication {
	
	
	public static String projectId = "erudite-nation-268921";
	public static String bucketName = "erudite-nation-268921";
	public static String objectName = "AudioSamples/audio.oog";
	public static String filePath = "audio.oog";

	
	public static void main(String[] args) {
		
		SpeechToText speechToText = new SpeechToText();
		TextToSpeech textToSpeech = new TextToSpeech();
		String storageUri = "gs://erudite-nation-268921/AudioSamples/audio.mp3";
		
		//speechToText.recognizeVoice(storageUri);
		System.out.println("uploading the item to Cloud Storage");
		CloudStorage cloudStorage = new CloudStorage();
		cloudStorage.storeFile(projectId, bucketName, objectName, filePath);
		
		System.out.println("magic dust to convert sound to words");
		Optional<String> matchedText = speechToText.recognizeVoice(storageUri);
		
		
		if(matchedText.isPresent()) {
			System.out.println("calling business logic");
			BusinessExtract businessExtract = new BusinessExtract();
			String reply = businessExtract.getBusinessExtract(matchedText.get());
			System.out.println("Got reply back from business logic");
			
			System.out.println("deleting the data from cloud storage");
			cloudStorage.deleteFile(projectId, bucketName, objectName, filePath);
			
			System.out.println("converting reply to audio now");
			textToSpeech.textToSpeechConverter(reply, "output_atul_new.oog");
			
		} else {
			System.out.println("Unable to decode voice");
		}
		
	}
}
