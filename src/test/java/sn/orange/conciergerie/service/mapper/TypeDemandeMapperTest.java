package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.TypeDemandeAsserts.*;
import static sn.orange.conciergerie.domain.TypeDemandeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeDemandeMapperTest {

    private TypeDemandeMapper typeDemandeMapper;

    @BeforeEach
    void setUp() {
        typeDemandeMapper = new TypeDemandeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeDemandeSample1();
        var actual = typeDemandeMapper.toEntity(typeDemandeMapper.toDto(expected));
        assertTypeDemandeAllPropertiesEquals(expected, actual);
    }
}
