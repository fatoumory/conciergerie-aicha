package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;
import static sn.orange.conciergerie.domain.PartenaireZoneTestSamples.*;
import static sn.orange.conciergerie.domain.ZoneTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PartenaireZoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartenaireZone.class);
        PartenaireZone partenaireZone1 = getPartenaireZoneSample1();
        PartenaireZone partenaireZone2 = new PartenaireZone();
        assertThat(partenaireZone1).isNotEqualTo(partenaireZone2);

        partenaireZone2.setId(partenaireZone1.getId());
        assertThat(partenaireZone1).isEqualTo(partenaireZone2);

        partenaireZone2 = getPartenaireZoneSample2();
        assertThat(partenaireZone1).isNotEqualTo(partenaireZone2);
    }

    @Test
    void partenaireTest() {
        PartenaireZone partenaireZone = getPartenaireZoneRandomSampleGenerator();
        Partenaire partenaireBack = getPartenaireRandomSampleGenerator();

        partenaireZone.setPartenaire(partenaireBack);
        assertThat(partenaireZone.getPartenaire()).isEqualTo(partenaireBack);

        partenaireZone.partenaire(null);
        assertThat(partenaireZone.getPartenaire()).isNull();
    }

    @Test
    void zoneTest() {
        PartenaireZone partenaireZone = getPartenaireZoneRandomSampleGenerator();
        Zone zoneBack = getZoneRandomSampleGenerator();

        partenaireZone.setZone(zoneBack);
        assertThat(partenaireZone.getZone()).isEqualTo(zoneBack);

        partenaireZone.zone(null);
        assertThat(partenaireZone.getZone()).isNull();
    }
}
