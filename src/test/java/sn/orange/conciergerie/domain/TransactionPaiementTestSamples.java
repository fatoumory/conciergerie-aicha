package sn.orange.conciergerie.domain;

import java.util.UUID;

public class TransactionPaiementTestSamples {

    public static TransactionPaiement getTransactionPaiementSample1() {
        return new TransactionPaiement().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).referenceExterne("referenceExterne1");
    }

    public static TransactionPaiement getTransactionPaiementSample2() {
        return new TransactionPaiement().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).referenceExterne("referenceExterne2");
    }

    public static TransactionPaiement getTransactionPaiementRandomSampleGenerator() {
        return new TransactionPaiement().id(UUID.randomUUID()).referenceExterne(UUID.randomUUID().toString());
    }
}
