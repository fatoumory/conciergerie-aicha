package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilDTO.class);
        ProfilDTO profilDTO1 = new ProfilDTO();
        profilDTO1.setId(UUID.randomUUID());
        ProfilDTO profilDTO2 = new ProfilDTO();
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
        profilDTO2.setId(profilDTO1.getId());
        assertThat(profilDTO1).isEqualTo(profilDTO2);
        profilDTO2.setId(UUID.randomUUID());
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
        profilDTO1.setId(null);
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
    }
}
