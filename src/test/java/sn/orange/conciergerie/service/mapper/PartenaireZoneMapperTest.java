package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.PartenaireZoneAsserts.*;
import static sn.orange.conciergerie.domain.PartenaireZoneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartenaireZoneMapperTest {

    private PartenaireZoneMapper partenaireZoneMapper;

    @BeforeEach
    void setUp() {
        partenaireZoneMapper = new PartenaireZoneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPartenaireZoneSample1();
        var actual = partenaireZoneMapper.toEntity(partenaireZoneMapper.toDto(expected));
        assertPartenaireZoneAllPropertiesEquals(expected, actual);
    }
}
