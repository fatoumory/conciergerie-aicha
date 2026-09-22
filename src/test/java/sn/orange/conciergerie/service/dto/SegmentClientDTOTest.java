package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class SegmentClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SegmentClientDTO.class);
        SegmentClientDTO segmentClientDTO1 = new SegmentClientDTO();
        segmentClientDTO1.setId(UUID.randomUUID());
        SegmentClientDTO segmentClientDTO2 = new SegmentClientDTO();
        assertThat(segmentClientDTO1).isNotEqualTo(segmentClientDTO2);
        segmentClientDTO2.setId(segmentClientDTO1.getId());
        assertThat(segmentClientDTO1).isEqualTo(segmentClientDTO2);
        segmentClientDTO2.setId(UUID.randomUUID());
        assertThat(segmentClientDTO1).isNotEqualTo(segmentClientDTO2);
        segmentClientDTO1.setId(null);
        assertThat(segmentClientDTO1).isNotEqualTo(segmentClientDTO2);
    }
}
