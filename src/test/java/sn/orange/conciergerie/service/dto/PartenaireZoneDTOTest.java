package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PartenaireZoneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartenaireZoneDTO.class);
        PartenaireZoneDTO partenaireZoneDTO1 = new PartenaireZoneDTO();
        partenaireZoneDTO1.setId(UUID.randomUUID());
        PartenaireZoneDTO partenaireZoneDTO2 = new PartenaireZoneDTO();
        assertThat(partenaireZoneDTO1).isNotEqualTo(partenaireZoneDTO2);
        partenaireZoneDTO2.setId(partenaireZoneDTO1.getId());
        assertThat(partenaireZoneDTO1).isEqualTo(partenaireZoneDTO2);
        partenaireZoneDTO2.setId(UUID.randomUUID());
        assertThat(partenaireZoneDTO1).isNotEqualTo(partenaireZoneDTO2);
        partenaireZoneDTO1.setId(null);
        assertThat(partenaireZoneDTO1).isNotEqualTo(partenaireZoneDTO2);
    }
}
