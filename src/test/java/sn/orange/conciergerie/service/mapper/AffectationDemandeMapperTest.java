package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.AffectationDemandeAsserts.*;
import static sn.orange.conciergerie.domain.AffectationDemandeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffectationDemandeMapperTest {

    private AffectationDemandeMapper affectationDemandeMapper;

    @BeforeEach
    void setUp() {
        affectationDemandeMapper = new AffectationDemandeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAffectationDemandeSample1();
        var actual = affectationDemandeMapper.toEntity(affectationDemandeMapper.toDto(expected));
        assertAffectationDemandeAllPropertiesEquals(expected, actual);
    }
}
