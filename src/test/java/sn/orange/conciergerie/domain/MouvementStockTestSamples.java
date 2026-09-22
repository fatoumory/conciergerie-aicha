package sn.orange.conciergerie.domain;

import java.util.UUID;

public class MouvementStockTestSamples {

    public static MouvementStock getMouvementStockSample1() {
        return new MouvementStock().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).motif("motif1");
    }

    public static MouvementStock getMouvementStockSample2() {
        return new MouvementStock().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).motif("motif2");
    }

    public static MouvementStock getMouvementStockRandomSampleGenerator() {
        return new MouvementStock().id(UUID.randomUUID()).motif(UUID.randomUUID().toString());
    }
}
