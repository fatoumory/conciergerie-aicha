package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CodePromoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodePromoDTO.class);
        CodePromoDTO codePromoDTO1 = new CodePromoDTO();
        codePromoDTO1.setId(UUID.randomUUID());
        CodePromoDTO codePromoDTO2 = new CodePromoDTO();
        assertThat(codePromoDTO1).isNotEqualTo(codePromoDTO2);
        codePromoDTO2.setId(codePromoDTO1.getId());
        assertThat(codePromoDTO1).isEqualTo(codePromoDTO2);
        codePromoDTO2.setId(UUID.randomUUID());
        assertThat(codePromoDTO1).isNotEqualTo(codePromoDTO2);
        codePromoDTO1.setId(null);
        assertThat(codePromoDTO1).isNotEqualTo(codePromoDTO2);
    }
}
