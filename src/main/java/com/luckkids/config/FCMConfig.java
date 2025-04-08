package com.luckkids.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		FirebaseApp firebaseApp = getFirebaseApp();
		return FirebaseMessaging.getInstance(firebaseApp);
	}

	private FirebaseApp getFirebaseApp() throws IOException {
		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

		if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
			for (FirebaseApp app : firebaseAppList) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					return app;
				}
			}
		} else {
			return initializeFirebaseApp();
		}
		throw new IllegalStateException("Failed to get FirebaseApp.");
	}

	private FirebaseApp initializeFirebaseApp() throws IOException {
		ClassPathResource resource = new ClassPathResource("luck-kids-5063f5491195.json");

		try (InputStream inputStream = resource.getInputStream()) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(inputStream))
				.build();

			return FirebaseApp.initializeApp(options);
		}
	}
}
