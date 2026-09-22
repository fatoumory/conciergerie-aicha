package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.PrestationAsserts.*;
import static sn.orange.conciergerie.domain.PrestationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestationMapperTest {

    private PrestationMapper prestationMapper;

    @BeforeEach
    void setUp() {
        prestationMapper = new PrestationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrestationSample1();
        var actual = prestationMapper.toEntity(prestationMapper.toDto(expected));
        assertPrestationAllPropertiesEquals(expected, actual);
    }
}
