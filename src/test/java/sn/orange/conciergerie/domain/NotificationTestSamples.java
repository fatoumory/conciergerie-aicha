package sn.orange.conciergerie.domain;

import java.util.UUID;

public class NotificationTestSamples {

    public static Notification getNotificationSample1() {
        return new Notification().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).titre("titre1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).titre("titre2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification().id(UUID.randomUUID()).titre(UUID.randomUUID().toString());
    }
}
