package sn.orange.conciergerie.domain;

import java.util.UUID;

public class UtilisationCodeQrTestSamples {

    public static UtilisationCodeQr getUtilisationCodeQrSample1() {
        return new UtilisationCodeQr().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static UtilisationCodeQr getUtilisationCodeQrSample2() {
        return new UtilisationCodeQr().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static UtilisationCodeQr getUtilisationCodeQrRandomSampleGenerator() {
        return new UtilisationCodeQr().id(UUID.randomUUID());
    }
}
