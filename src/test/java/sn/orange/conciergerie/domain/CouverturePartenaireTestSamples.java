package sn.orange.conciergerie.domain;

import java.util.UUID;

public class CouverturePartenaireTestSamples {

    public static CouverturePartenaire getCouverturePartenaireSample1() {
        return new CouverturePartenaire().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static CouverturePartenaire getCouverturePartenaireSample2() {
        return new CouverturePartenaire().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static CouverturePartenaire getCouverturePartenaireRandomSampleGenerator() {
        return new CouverturePartenaire().id(UUID.randomUUID());
    }
}
