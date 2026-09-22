package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CompteStockTestSamples.*;
import static sn.orange.conciergerie.domain.MouvementStockTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class MouvementStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MouvementStock.class);
        MouvementStock mouvementStock1 = getMouvementStockSample1();
        MouvementStock mouvementStock2 = new MouvementStock();
        assertThat(mouvementStock1).isNotEqualTo(mouvementStock2);

        mouvementStock2.setId(mouvementStock1.getId());
        assertThat(mouvementStock1).isEqualTo(mouvementStock2);

        mouvementStock2 = getMouvementStockSample2();
        assertThat(mouvementStock1).isNotEqualTo(mouvementStock2);
    }

    @Test
    void compteStockTest() {
        MouvementStock mouvementStock = getMouvementStockRandomSampleGenerator();
        CompteStock compteStockBack = getCompteStockRandomSampleGenerator();

        mouvementStock.setCompteStock(compteStockBack);
        assertThat(mouvementStock.getCompteStock()).isEqualTo(compteStockBack);

        mouvementStock.compteStock(null);
        assertThat(mouvementStock.getCompteStock()).isNull();
    }
}
