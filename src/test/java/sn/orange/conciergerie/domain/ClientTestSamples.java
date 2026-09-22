package sn.orange.conciergerie.domain;

import java.util.UUID;

public class ClientTestSamples {

    public static Client getClientSample1() {
        return new Client()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .numero("numero1")
            .prenom("prenom1")
            .nom("nom1")
            .email("email1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .numero("numero2")
            .prenom("prenom2")
            .nom("nom2")
            .email("email2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(UUID.randomUUID())
            .numero(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
