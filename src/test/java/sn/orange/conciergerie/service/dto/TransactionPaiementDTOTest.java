package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TransactionPaiementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionPaiementDTO.class);
        TransactionPaiementDTO transactionPaiementDTO1 = new TransactionPaiementDTO();
        transactionPaiementDTO1.setId(UUID.randomUUID());
        TransactionPaiementDTO transactionPaiementDTO2 = new TransactionPaiementDTO();
        assertThat(transactionPaiementDTO1).isNotEqualTo(transactionPaiementDTO2);
        transactionPaiementDTO2.setId(transactionPaiementDTO1.getId());
        assertThat(transactionPaiementDTO1).isEqualTo(transactionPaiementDTO2);
        transactionPaiementDTO2.setId(UUID.randomUUID());
        assertThat(transactionPaiementDTO1).isNotEqualTo(transactionPaiementDTO2);
        transactionPaiementDTO1.setId(null);
        assertThat(transactionPaiementDTO1).isNotEqualTo(transactionPaiementDTO2);
    }
}
