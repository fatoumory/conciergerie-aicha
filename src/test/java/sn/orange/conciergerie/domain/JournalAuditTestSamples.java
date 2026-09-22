package sn.orange.conciergerie.domain;

import java.util.UUID;

public class JournalAuditTestSamples {

    public static JournalAudit getJournalAuditSample1() {
        return new JournalAudit()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .action("action1")
            .typeObjet("typeObjet1")
            .objetId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .correlationId("correlationId1");
    }

    public static JournalAudit getJournalAuditSample2() {
        return new JournalAudit()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .action("action2")
            .typeObjet("typeObjet2")
            .objetId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .correlationId("correlationId2");
    }

    public static JournalAudit getJournalAuditRandomSampleGenerator() {
        return new JournalAudit()
            .id(UUID.randomUUID())
            .action(UUID.randomUUID().toString())
            .typeObjet(UUID.randomUUID().toString())
            .objetId(UUID.randomUUID())
            .correlationId(UUID.randomUUID().toString());
    }
}
