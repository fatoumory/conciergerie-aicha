package sn.orange.conciergerie.domain;

import java.util.UUID;

public class TypeClientTestSamples {

    public static TypeClient getTypeClientSample1() {
        return new TypeClient().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").libelle("libelle1");
    }

    public static TypeClient getTypeClientSample2() {
        return new TypeClient().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").libelle("libelle2");
    }

    public static TypeClient getTypeClientRandomSampleGenerator() {
        return new TypeClient().id(UUID.randomUUID()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
