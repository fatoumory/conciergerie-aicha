package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CompteStockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteStockDTO.class);
        CompteStockDTO compteStockDTO1 = new CompteStockDTO();
        compteStockDTO1.setId(UUID.randomUUID());
        CompteStockDTO compteStockDTO2 = new CompteStockDTO();
        assertThat(compteStockDTO1).isNotEqualTo(compteStockDTO2);
        compteStockDTO2.setId(compteStockDTO1.getId());
        assertThat(compteStockDTO1).isEqualTo(compteStockDTO2);
        compteStockDTO2.setId(UUID.randomUUID());
        assertThat(compteStockDTO1).isNotEqualTo(compteStockDTO2);
        compteStockDTO1.setId(null);
        assertThat(compteStockDTO1).isNotEqualTo(compteStockDTO2);
    }
}
