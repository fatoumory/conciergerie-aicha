package sn.orange.conciergerie.domain;

import java.util.UUID;

public class CodeQrServiceTestSamples {

    public static CodeQrService getCodeQrServiceSample1() {
        return new CodeQrService().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).code("code1");
    }

    public static CodeQrService getCodeQrServiceSample2() {
        return new CodeQrService().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).code("code2");
    }

    public static CodeQrService getCodeQrServiceRandomSampleGenerator() {
        return new CodeQrService().id(UUID.randomUUID()).code(UUID.randomUUID().toString());
    }
}
