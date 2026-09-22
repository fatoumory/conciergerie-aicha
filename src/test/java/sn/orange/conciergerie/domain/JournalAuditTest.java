package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.JournalAuditTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class JournalAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalAudit.class);
        JournalAudit journalAudit1 = getJournalAuditSample1();
        JournalAudit journalAudit2 = new JournalAudit();
        assertThat(journalAudit1).isNotEqualTo(journalAudit2);

        journalAudit2.setId(journalAudit1.getId());
        assertThat(journalAudit1).isEqualTo(journalAudit2);

        journalAudit2 = getJournalAuditSample2();
        assertThat(journalAudit1).isNotEqualTo(journalAudit2);
    }
}
