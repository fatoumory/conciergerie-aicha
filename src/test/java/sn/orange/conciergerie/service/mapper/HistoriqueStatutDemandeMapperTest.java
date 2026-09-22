package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.HistoriqueStatutDemandeAsserts.*;
import static sn.orange.conciergerie.domain.HistoriqueStatutDemandeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueStatutDemandeMapperTest {

    private HistoriqueStatutDemandeMapper historiqueStatutDemandeMapper;

    @BeforeEach
    void setUp() {
        historiqueStatutDemandeMapper = new HistoriqueStatutDemandeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueStatutDemandeSample1();
        var actual = historiqueStatutDemandeMapper.toEntity(historiqueStatutDemandeMapper.toDto(expected));
        assertHistoriqueStatutDemandeAllPropertiesEquals(expected, actual);
    }
}
