package com.test.google.speech.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayAudio {

	public void playAudio(String filePath) {
		
		try {
			File soundFile = new File(filePath);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			
			Clip clip = AudioSystem.getClip();
			
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
