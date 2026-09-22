package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.EligibiliteServiceAsserts.*;
import static sn.orange.conciergerie.domain.EligibiliteServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EligibiliteServiceMapperTest {

    private EligibiliteServiceMapper eligibiliteServiceMapper;

    @BeforeEach
    void setUp() {
        eligibiliteServiceMapper = new EligibiliteServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEligibiliteServiceSample1();
        var actual = eligibiliteServiceMapper.toEntity(eligibiliteServiceMapper.toDto(expected));
        assertEligibiliteServiceAllPropertiesEquals(expected, actual);
    }
}
