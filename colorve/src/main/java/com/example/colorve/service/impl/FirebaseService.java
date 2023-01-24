package com.example.colorve.service.impl;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotification(String token, String title, String body) throws InterruptedException, ExecutionException {
    	Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    	
    	Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
    	
        firebaseMessaging.sendAsync(message).get();
    }
}