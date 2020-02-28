package com.test.google.speech.api.calls;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeRequest;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;

@Service
public class SpeechToText {

	/**
	 * Performs synchronous speech recognition on an audio file
	 *
	 * @param storageUri URI for audio file in Cloud Storage, e.g. gs://[BUCKET]/[FILE]
	 */
	public Optional<String> recognizeVoice(String storageUri) {
	  try (SpeechClient speechClient = SpeechClient.create()) {

	    // The language of the supplied audio
	    String languageCode = "en-US";

	    // Sample rate in Hertz of the audio data sent
	    //int sampleRateHertz = 16000;

	    // Encoding of audio data sent. This sample sets this explicitly.
	    // This field is optional for FLAC and WAV audio formats.
	    RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.LINEAR16;
	    RecognitionConfig config =
	        RecognitionConfig.newBuilder()
	            .setLanguageCode(languageCode)
	           // .setSampleRateHertz(sampleRateHertz)
	            .setEncoding(encoding)
	            .build();
	    RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(storageUri).build();
	    RecognizeRequest request =
	        RecognizeRequest.newBuilder().setConfig(config).setAudio(audio).build();
	    RecognizeResponse response = speechClient.recognize(request);
	    
	    String reply = response.getResultsList().stream().map(SpeechRecognitionResult::getAlternativesList).
	    		flatMap(Collection::stream)
	    		.map(SpeechRecognitionAlternative::getTranscript)
	    		.collect(Collectors.joining(" "));
	    
	    System.out.printf("Transcript: %s\n", reply);
	    
	    return Optional.of(reply);
	    
	    
	    /*for (SpeechRecognitionResult result : response.getResultsList()) {
	      // First alternative is the most probable result
	    //  return Optional.of(result.getAlternativesList().stream().map(SpeechRecognitionAlternative::getTranscript).collect(Collectors.joining(" "));
	      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	     
	      return Optional.of(alternative.getTranscript());
	    }*/
	  } catch (Exception exception) {
	    System.err.println("Failed to create the client due to: " + exception);
	  }
	  
	  return Optional.empty();
	}
}
