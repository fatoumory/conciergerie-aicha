package sn.orange.conciergerie.domain;

import java.util.UUID;

public class ServiceConciergerieTestSamples {

    public static ServiceConciergerie getServiceConciergerieSample1() {
        return new ServiceConciergerie()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .code("code1")
            .libelle("libelle1")
            .description("description1");
    }

    public static ServiceConciergerie getServiceConciergerieSample2() {
        return new ServiceConciergerie()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .code("code2")
            .libelle("libelle2")
            .description("description2");
    }

    public static ServiceConciergerie getServiceConciergerieRandomSampleGenerator() {
        return new ServiceConciergerie()
            .id(UUID.randomUUID())
            .code(UUID.randomUUID().toString())
            .libelle(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
