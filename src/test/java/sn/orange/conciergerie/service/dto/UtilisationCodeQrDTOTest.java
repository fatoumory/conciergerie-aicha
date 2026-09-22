package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class UtilisationCodeQrDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisationCodeQrDTO.class);
        UtilisationCodeQrDTO utilisationCodeQrDTO1 = new UtilisationCodeQrDTO();
        utilisationCodeQrDTO1.setId(UUID.randomUUID());
        UtilisationCodeQrDTO utilisationCodeQrDTO2 = new UtilisationCodeQrDTO();
        assertThat(utilisationCodeQrDTO1).isNotEqualTo(utilisationCodeQrDTO2);
        utilisationCodeQrDTO2.setId(utilisationCodeQrDTO1.getId());
        assertThat(utilisationCodeQrDTO1).isEqualTo(utilisationCodeQrDTO2);
        utilisationCodeQrDTO2.setId(UUID.randomUUID());
        assertThat(utilisationCodeQrDTO1).isNotEqualTo(utilisationCodeQrDTO2);
        utilisationCodeQrDTO1.setId(null);
        assertThat(utilisationCodeQrDTO1).isNotEqualTo(utilisationCodeQrDTO2);
    }
}
