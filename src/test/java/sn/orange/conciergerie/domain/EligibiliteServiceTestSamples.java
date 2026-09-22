package sn.orange.conciergerie.domain;

import java.util.UUID;

public class EligibiliteServiceTestSamples {

    public static EligibiliteService getEligibiliteServiceSample1() {
        return new EligibiliteService().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static EligibiliteService getEligibiliteServiceSample2() {
        return new EligibiliteService().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static EligibiliteService getEligibiliteServiceRandomSampleGenerator() {
        return new EligibiliteService().id(UUID.randomUUID());
    }
}
