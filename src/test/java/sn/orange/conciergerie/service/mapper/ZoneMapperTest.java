package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.ZoneAsserts.*;
import static sn.orange.conciergerie.domain.ZoneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZoneMapperTest {

    private ZoneMapper zoneMapper;

    @BeforeEach
    void setUp() {
        zoneMapper = new ZoneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getZoneSample1();
        var actual = zoneMapper.toEntity(zoneMapper.toDto(expected));
        assertZoneAllPropertiesEquals(expected, actual);
    }
}
