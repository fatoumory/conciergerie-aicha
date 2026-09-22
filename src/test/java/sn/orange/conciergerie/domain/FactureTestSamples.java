package sn.orange.conciergerie.domain;

import java.util.UUID;

public class FactureTestSamples {

    public static Facture getFactureSample1() {
        return new Facture().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).numero("numero1");
    }

    public static Facture getFactureSample2() {
        return new Facture().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).numero("numero2");
    }

    public static Facture getFactureRandomSampleGenerator() {
        return new Facture().id(UUID.randomUUID()).numero(UUID.randomUUID().toString());
    }
}
