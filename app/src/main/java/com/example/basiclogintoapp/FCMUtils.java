package com.example.basiclogintoapp;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FCMUtils {

    public static void sendNotificationToTopic(String topic, String title, String message) {
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", message);

        RemoteMessage remoteMessage = new RemoteMessage.Builder(topic)
                .setData(data)
                .build();

        FirebaseMessaging.getInstance().send(remoteMessage);
    }
}
