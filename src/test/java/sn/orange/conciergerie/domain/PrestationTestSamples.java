package sn.orange.conciergerie.domain;

import java.util.UUID;

public class PrestationTestSamples {

    public static Prestation getPrestationSample1() {
        return new Prestation().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Prestation getPrestationSample2() {
        return new Prestation().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Prestation getPrestationRandomSampleGenerator() {
        return new Prestation().id(UUID.randomUUID());
    }
}
