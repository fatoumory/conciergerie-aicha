package sn.orange.conciergerie.domain;

import java.util.UUID;

public class TypeServiceTestSamples {

    public static TypeService getTypeServiceSample1() {
        return new TypeService().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").libelle("libelle1");
    }

    public static TypeService getTypeServiceSample2() {
        return new TypeService().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").libelle("libelle2");
    }

    public static TypeService getTypeServiceRandomSampleGenerator() {
        return new TypeService().id(UUID.randomUUID()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
