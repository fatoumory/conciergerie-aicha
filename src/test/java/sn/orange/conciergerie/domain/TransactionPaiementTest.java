package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.TransactionPaiementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TransactionPaiementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionPaiement.class);
        TransactionPaiement transactionPaiement1 = getTransactionPaiementSample1();
        TransactionPaiement transactionPaiement2 = new TransactionPaiement();
        assertThat(transactionPaiement1).isNotEqualTo(transactionPaiement2);

        transactionPaiement2.setId(transactionPaiement1.getId());
        assertThat(transactionPaiement1).isEqualTo(transactionPaiement2);

        transactionPaiement2 = getTransactionPaiementSample2();
        assertThat(transactionPaiement1).isNotEqualTo(transactionPaiement2);
    }

    @Test
    void demandeTest() {
        TransactionPaiement transactionPaiement = getTransactionPaiementRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        transactionPaiement.setDemande(demandeBack);
        assertThat(transactionPaiement.getDemande()).isEqualTo(demandeBack);

        transactionPaiement.demande(null);
        assertThat(transactionPaiement.getDemande()).isNull();
    }
}
