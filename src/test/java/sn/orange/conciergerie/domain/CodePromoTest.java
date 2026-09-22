package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CodePromoTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CodePromoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodePromo.class);
        CodePromo codePromo1 = getCodePromoSample1();
        CodePromo codePromo2 = new CodePromo();
        assertThat(codePromo1).isNotEqualTo(codePromo2);

        codePromo2.setId(codePromo1.getId());
        assertThat(codePromo1).isEqualTo(codePromo2);

        codePromo2 = getCodePromoSample2();
        assertThat(codePromo1).isNotEqualTo(codePromo2);
    }
}
