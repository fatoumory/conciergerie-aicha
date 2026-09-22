package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CodeQrServiceTestSamples.*;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;
import static sn.orange.conciergerie.domain.UtilisationCodeQrTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class UtilisationCodeQrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisationCodeQr.class);
        UtilisationCodeQr utilisationCodeQr1 = getUtilisationCodeQrSample1();
        UtilisationCodeQr utilisationCodeQr2 = new UtilisationCodeQr();
        assertThat(utilisationCodeQr1).isNotEqualTo(utilisationCodeQr2);

        utilisationCodeQr2.setId(utilisationCodeQr1.getId());
        assertThat(utilisationCodeQr1).isEqualTo(utilisationCodeQr2);

        utilisationCodeQr2 = getUtilisationCodeQrSample2();
        assertThat(utilisationCodeQr1).isNotEqualTo(utilisationCodeQr2);
    }

    @Test
    void codeQrServiceTest() {
        UtilisationCodeQr utilisationCodeQr = getUtilisationCodeQrRandomSampleGenerator();
        CodeQrService codeQrServiceBack = getCodeQrServiceRandomSampleGenerator();

        utilisationCodeQr.setCodeQrService(codeQrServiceBack);
        assertThat(utilisationCodeQr.getCodeQrService()).isEqualTo(codeQrServiceBack);

        utilisationCodeQr.codeQrService(null);
        assertThat(utilisationCodeQr.getCodeQrService()).isNull();
    }

    @Test
    void partenaireTest() {
        UtilisationCodeQr utilisationCodeQr = getUtilisationCodeQrRandomSampleGenerator();
        Partenaire partenaireBack = getPartenaireRandomSampleGenerator();

        utilisationCodeQr.setPartenaire(partenaireBack);
        assertThat(utilisationCodeQr.getPartenaire()).isEqualTo(partenaireBack);

        utilisationCodeQr.partenaire(null);
        assertThat(utilisationCodeQr.getPartenaire()).isNull();
    }
}
