package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class JournalAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalAuditDTO.class);
        JournalAuditDTO journalAuditDTO1 = new JournalAuditDTO();
        journalAuditDTO1.setId(UUID.randomUUID());
        JournalAuditDTO journalAuditDTO2 = new JournalAuditDTO();
        assertThat(journalAuditDTO1).isNotEqualTo(journalAuditDTO2);
        journalAuditDTO2.setId(journalAuditDTO1.getId());
        assertThat(journalAuditDTO1).isEqualTo(journalAuditDTO2);
        journalAuditDTO2.setId(UUID.randomUUID());
        assertThat(journalAuditDTO1).isNotEqualTo(journalAuditDTO2);
        journalAuditDTO1.setId(null);
        assertThat(journalAuditDTO1).isNotEqualTo(journalAuditDTO2);
    }
}
