package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CodeQrServiceTestSamples.*;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CodeQrServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeQrService.class);
        CodeQrService codeQrService1 = getCodeQrServiceSample1();
        CodeQrService codeQrService2 = new CodeQrService();
        assertThat(codeQrService1).isNotEqualTo(codeQrService2);

        codeQrService2.setId(codeQrService1.getId());
        assertThat(codeQrService1).isEqualTo(codeQrService2);

        codeQrService2 = getCodeQrServiceSample2();
        assertThat(codeQrService1).isNotEqualTo(codeQrService2);
    }

    @Test
    void demandeTest() {
        CodeQrService codeQrService = getCodeQrServiceRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        codeQrService.setDemande(demandeBack);
        assertThat(codeQrService.getDemande()).isEqualTo(demandeBack);

        codeQrService.demande(null);
        assertThat(codeQrService.getDemande()).isNull();
    }
}
