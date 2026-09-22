package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CodeQrServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeQrServiceDTO.class);
        CodeQrServiceDTO codeQrServiceDTO1 = new CodeQrServiceDTO();
        codeQrServiceDTO1.setId(UUID.randomUUID());
        CodeQrServiceDTO codeQrServiceDTO2 = new CodeQrServiceDTO();
        assertThat(codeQrServiceDTO1).isNotEqualTo(codeQrServiceDTO2);
        codeQrServiceDTO2.setId(codeQrServiceDTO1.getId());
        assertThat(codeQrServiceDTO1).isEqualTo(codeQrServiceDTO2);
        codeQrServiceDTO2.setId(UUID.randomUUID());
        assertThat(codeQrServiceDTO1).isNotEqualTo(codeQrServiceDTO2);
        codeQrServiceDTO1.setId(null);
        assertThat(codeQrServiceDTO1).isNotEqualTo(codeQrServiceDTO2);
    }
}
