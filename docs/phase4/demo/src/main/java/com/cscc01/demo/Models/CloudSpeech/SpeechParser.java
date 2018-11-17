package com.cscc01.demo.Models.CloudSpeech;

import static com.cscc01.demo.Constants.PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;

public class SpeechParser {
	public static String parse(String fileName) throws Exception {

		Process p1 = Runtime.getRuntime()
				.exec("cmd /c  sox " + PATH + fileName
						+ " -t raw --channels=1 --bits=16 --rate=16000 --encoding=signed-integer --endian=little "
						+ PATH + "random.raw" +
						" && gsutil cp C:/Users/kumar/git/csc01teamprojectrepo2/docs/phase4/demo/target/files/random.raw gs://cscc01kumar/random.raw");

		p1.waitFor();
//
//		Process p2 = Runtime.getRuntime()
//				.exec("");
//		
//		p2.waitFor();
		
		return asyncRecognizeGcs("gs://cscc01kumar/random.raw");
	}

	public static String asyncRecognizeGcs(String gcsUri) throws Exception {
		
		String body = "";
		
		// Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		try (SpeechClient speech = SpeechClient.create()) {

			// Configure remote file request for Linear16
			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16)
					.setLanguageCode("en-US").setSampleRateHertz(16000).build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

			// Use non-blocking call for getting file transcription
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = speech
					.longRunningRecognizeAsync(config, audio);
			while (!response.isDone()) {
				System.out.println("Waiting for response...");
				Thread.sleep(10000);
			}

			List<SpeechRecognitionResult> results = response.get().getResultsList();

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech.
				// Just use the
				// first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				System.out.printf("Transcription: %s\n", alternative.getTranscript());
				body += alternative.getTranscript();
			}
		}
		
		return body;
	}
}
