package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.EvaluationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class EvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
        Evaluation evaluation1 = getEvaluationSample1();
        Evaluation evaluation2 = new Evaluation();
        assertThat(evaluation1).isNotEqualTo(evaluation2);

        evaluation2.setId(evaluation1.getId());
        assertThat(evaluation1).isEqualTo(evaluation2);

        evaluation2 = getEvaluationSample2();
        assertThat(evaluation1).isNotEqualTo(evaluation2);
    }

    @Test
    void demandeTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        evaluation.setDemande(demandeBack);
        assertThat(evaluation.getDemande()).isEqualTo(demandeBack);

        evaluation.demande(null);
        assertThat(evaluation.getDemande()).isNull();
    }
}
