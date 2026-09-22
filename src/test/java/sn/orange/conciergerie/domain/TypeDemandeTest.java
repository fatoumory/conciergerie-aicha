package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.TypeDemandeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeDemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDemande.class);
        TypeDemande typeDemande1 = getTypeDemandeSample1();
        TypeDemande typeDemande2 = new TypeDemande();
        assertThat(typeDemande1).isNotEqualTo(typeDemande2);

        typeDemande2.setId(typeDemande1.getId());
        assertThat(typeDemande1).isEqualTo(typeDemande2);

        typeDemande2 = getTypeDemandeSample2();
        assertThat(typeDemande1).isNotEqualTo(typeDemande2);
    }
}
