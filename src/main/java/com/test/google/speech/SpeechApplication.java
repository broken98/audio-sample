package com.test.google.speech;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.google.speech.api.calls.CloudStorage;
import com.test.google.speech.api.calls.SpeechToText;
import com.test.google.speech.api.calls.TextToSpeech;
import com.test.google.speech.audio.AudioRecorder;
import com.test.google.speech.audio.PlayAudio;
import com.test.google.speech.business.BusinessExtract;


@SpringBootApplication
public class SpeechApplication {
	
	
	public static String projectId = "erudite-nation-268921";
	public static String bucketName = "erudite-nation-268921";
	public static String objectName = "AudioSamples/atul_test.wav";
	public static String filePath = "atul_test.wav";

	
	public static void main(String[] args) throws InterruptedException {
		
		//SpringApplication.run(SpeechApplication.class, args);
		
		
		AudioRecorder audioRecorder = new AudioRecorder();
		File audio = audioRecorder.recordAudio(filePath);
		
		SpeechToText speechToText = new SpeechToText();
		TextToSpeech textToSpeech = new TextToSpeech();
		String storageUri = "gs://erudite-nation-268921/AudioSamples/atul_test.wav";
		
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
			System.out.println("Got reply back from business logic" + reply);
			
			System.out.println("deleting the data from cloud storage");
			//cloudStorage.deleteFile(projectId, bucketName, objectName, filePath);
			
			System.out.println("converting reply to audio now");
			textToSpeech.textToSpeechConverter(reply, "output_atul_new.wav");
			
		} else {
			System.out.println("Unable to decode voice");
		}
		
		PlayAudio playAudio = new PlayAudio();
		System.out.println("trying to play the video");
		playAudio.playAudio("output_atul_new.wav");
		
		Thread.sleep(5000);
		
		
	}


}
