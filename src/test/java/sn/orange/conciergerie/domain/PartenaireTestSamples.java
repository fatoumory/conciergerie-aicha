package sn.orange.conciergerie.domain;

import java.util.UUID;

public class PartenaireTestSamples {

    public static Partenaire getPartenaireSample1() {
        return new Partenaire().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").libelle("libelle1");
    }

    public static Partenaire getPartenaireSample2() {
        return new Partenaire().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").libelle("libelle2");
    }

    public static Partenaire getPartenaireRandomSampleGenerator() {
        return new Partenaire().id(UUID.randomUUID()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
