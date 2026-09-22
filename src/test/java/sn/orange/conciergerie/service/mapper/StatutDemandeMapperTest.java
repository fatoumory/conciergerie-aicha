package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.StatutDemandeAsserts.*;
import static sn.orange.conciergerie.domain.StatutDemandeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatutDemandeMapperTest {

    private StatutDemandeMapper statutDemandeMapper;

    @BeforeEach
    void setUp() {
        statutDemandeMapper = new StatutDemandeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStatutDemandeSample1();
        var actual = statutDemandeMapper.toEntity(statutDemandeMapper.toDto(expected));
        assertStatutDemandeAllPropertiesEquals(expected, actual);
    }
}
