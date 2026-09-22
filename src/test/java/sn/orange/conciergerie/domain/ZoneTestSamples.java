package sn.orange.conciergerie.domain;

import java.util.UUID;

public class ZoneTestSamples {

    public static Zone getZoneSample1() {
        return new Zone().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").libelle("libelle1");
    }

    public static Zone getZoneSample2() {
        return new Zone().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").libelle("libelle2");
    }

    public static Zone getZoneRandomSampleGenerator() {
        return new Zone().id(UUID.randomUUID()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
