package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.JournalAuditAsserts.*;
import static sn.orange.conciergerie.domain.JournalAuditTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JournalAuditMapperTest {

    private JournalAuditMapper journalAuditMapper;

    @BeforeEach
    void setUp() {
        journalAuditMapper = new JournalAuditMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJournalAuditSample1();
        var actual = journalAuditMapper.toEntity(journalAuditMapper.toDto(expected));
        assertJournalAuditAllPropertiesEquals(expected, actual);
    }
}
