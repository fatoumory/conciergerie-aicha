package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class MouvementStockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MouvementStockDTO.class);
        MouvementStockDTO mouvementStockDTO1 = new MouvementStockDTO();
        mouvementStockDTO1.setId(UUID.randomUUID());
        MouvementStockDTO mouvementStockDTO2 = new MouvementStockDTO();
        assertThat(mouvementStockDTO1).isNotEqualTo(mouvementStockDTO2);
        mouvementStockDTO2.setId(mouvementStockDTO1.getId());
        assertThat(mouvementStockDTO1).isEqualTo(mouvementStockDTO2);
        mouvementStockDTO2.setId(UUID.randomUUID());
        assertThat(mouvementStockDTO1).isNotEqualTo(mouvementStockDTO2);
        mouvementStockDTO1.setId(null);
        assertThat(mouvementStockDTO1).isNotEqualTo(mouvementStockDTO2);
    }
}
