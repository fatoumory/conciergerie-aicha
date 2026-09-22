package sn.orange.conciergerie.domain;

import java.util.UUID;

public class DemandeTestSamples {

    public static Demande getDemandeSample1() {
        return new Demande().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).description("description1");
    }

    public static Demande getDemandeSample2() {
        return new Demande().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).description("description2");
    }

    public static Demande getDemandeRandomSampleGenerator() {
        return new Demande().id(UUID.randomUUID()).description(UUID.randomUUID().toString());
    }
}
