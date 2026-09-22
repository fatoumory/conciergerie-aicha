package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.ClientTestSamples.*;
import static sn.orange.conciergerie.domain.CodePromoTestSamples.*;
import static sn.orange.conciergerie.domain.CodeQrServiceTestSamples.*;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.EvaluationTestSamples.*;
import static sn.orange.conciergerie.domain.FactureTestSamples.*;
import static sn.orange.conciergerie.domain.PrestationTestSamples.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;
import static sn.orange.conciergerie.domain.StatutDemandeTestSamples.*;
import static sn.orange.conciergerie.domain.TypeDemandeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class DemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demande.class);
        Demande demande1 = getDemandeSample1();
        Demande demande2 = new Demande();
        assertThat(demande1).isNotEqualTo(demande2);

        demande2.setId(demande1.getId());
        assertThat(demande1).isEqualTo(demande2);

        demande2 = getDemandeSample2();
        assertThat(demande1).isNotEqualTo(demande2);
    }

    @Test
    void clientTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        demande.setClient(clientBack);
        assertThat(demande.getClient()).isEqualTo(clientBack);

        demande.client(null);
        assertThat(demande.getClient()).isNull();
    }

    @Test
    void serviceTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        ServiceConciergerie serviceConciergerieBack = getServiceConciergerieRandomSampleGenerator();

        demande.setService(serviceConciergerieBack);
        assertThat(demande.getService()).isEqualTo(serviceConciergerieBack);

        demande.service(null);
        assertThat(demande.getService()).isNull();
    }

    @Test
    void typeDemandeTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        TypeDemande typeDemandeBack = getTypeDemandeRandomSampleGenerator();

        demande.setTypeDemande(typeDemandeBack);
        assertThat(demande.getTypeDemande()).isEqualTo(typeDemandeBack);

        demande.typeDemande(null);
        assertThat(demande.getTypeDemande()).isNull();
    }

    @Test
    void statutTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        StatutDemande statutDemandeBack = getStatutDemandeRandomSampleGenerator();

        demande.setStatut(statutDemandeBack);
        assertThat(demande.getStatut()).isEqualTo(statutDemandeBack);

        demande.statut(null);
        assertThat(demande.getStatut()).isNull();
    }

    @Test
    void codePromoTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        CodePromo codePromoBack = getCodePromoRandomSampleGenerator();

        demande.setCodePromo(codePromoBack);
        assertThat(demande.getCodePromo()).isEqualTo(codePromoBack);

        demande.codePromo(null);
        assertThat(demande.getCodePromo()).isNull();
    }

    @Test
    void prestationTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Prestation prestationBack = getPrestationRandomSampleGenerator();

        demande.setPrestation(prestationBack);
        assertThat(demande.getPrestation()).isEqualTo(prestationBack);
        assertThat(prestationBack.getDemande()).isEqualTo(demande);

        demande.prestation(null);
        assertThat(demande.getPrestation()).isNull();
        assertThat(prestationBack.getDemande()).isNull();
    }

    @Test
    void codeQrServiceTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        CodeQrService codeQrServiceBack = getCodeQrServiceRandomSampleGenerator();

        demande.setCodeQrService(codeQrServiceBack);
        assertThat(demande.getCodeQrService()).isEqualTo(codeQrServiceBack);
        assertThat(codeQrServiceBack.getDemande()).isEqualTo(demande);

        demande.codeQrService(null);
        assertThat(demande.getCodeQrService()).isNull();
        assertThat(codeQrServiceBack.getDemande()).isNull();
    }

    @Test
    void factureTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        demande.setFacture(factureBack);
        assertThat(demande.getFacture()).isEqualTo(factureBack);
        assertThat(factureBack.getDemande()).isEqualTo(demande);

        demande.facture(null);
        assertThat(demande.getFacture()).isNull();
        assertThat(factureBack.getDemande()).isNull();
    }

    @Test
    void evaluationTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Evaluation evaluationBack = getEvaluationRandomSampleGenerator();

        demande.setEvaluation(evaluationBack);
        assertThat(demande.getEvaluation()).isEqualTo(evaluationBack);
        assertThat(evaluationBack.getDemande()).isEqualTo(demande);

        demande.evaluation(null);
        assertThat(demande.getEvaluation()).isNull();
        assertThat(evaluationBack.getDemande()).isNull();
    }
}
