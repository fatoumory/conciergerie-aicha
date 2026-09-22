package sn.orange.conciergerie.domain;

import java.util.UUID;

public class CodePromoTestSamples {

    public static CodePromo getCodePromoSample1() {
        return new CodePromo().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1");
    }

    public static CodePromo getCodePromoSample2() {
        return new CodePromo().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2");
    }

    public static CodePromo getCodePromoRandomSampleGenerator() {
        return new CodePromo().id(UUID.randomUUID()).code(UUID.randomUUID().toString());
    }
}
