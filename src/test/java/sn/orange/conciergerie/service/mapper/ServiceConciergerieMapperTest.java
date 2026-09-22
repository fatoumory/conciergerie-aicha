package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.ServiceConciergerieAsserts.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceConciergerieMapperTest {

    private ServiceConciergerieMapper serviceConciergerieMapper;

    @BeforeEach
    void setUp() {
        serviceConciergerieMapper = new ServiceConciergerieMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceConciergerieSample1();
        var actual = serviceConciergerieMapper.toEntity(serviceConciergerieMapper.toDto(expected));
        assertServiceConciergerieAllPropertiesEquals(expected, actual);
    }
}
