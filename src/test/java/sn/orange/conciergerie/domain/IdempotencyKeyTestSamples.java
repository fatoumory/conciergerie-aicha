package sn.orange.conciergerie.domain;

import java.util.UUID;

public class IdempotencyKeyTestSamples {

    public static IdempotencyKey getIdempotencyKeySample1() {
        return new IdempotencyKey()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .cle("cle1")
            .typeOperation("typeOperation1")
            .resourceId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static IdempotencyKey getIdempotencyKeySample2() {
        return new IdempotencyKey()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .cle("cle2")
            .typeOperation("typeOperation2")
            .resourceId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static IdempotencyKey getIdempotencyKeyRandomSampleGenerator() {
        return new IdempotencyKey()
            .id(UUID.randomUUID())
            .cle(UUID.randomUUID().toString())
            .typeOperation(UUID.randomUUID().toString())
            .resourceId(UUID.randomUUID());
    }
}
