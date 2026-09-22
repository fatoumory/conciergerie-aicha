package sn.orange.conciergerie.domain;

import java.util.UUID;

public class StatutDemandeTestSamples {

    public static StatutDemande getStatutDemandeSample1() {
        return new StatutDemande().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1").libelle("libelle1");
    }

    public static StatutDemande getStatutDemandeSample2() {
        return new StatutDemande().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2").libelle("libelle2");
    }

    public static StatutDemande getStatutDemandeRandomSampleGenerator() {
        return new StatutDemande().id(UUID.randomUUID()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
