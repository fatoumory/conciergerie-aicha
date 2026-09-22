package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.QuotaServiceAsserts.*;
import static sn.orange.conciergerie.domain.QuotaServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuotaServiceMapperTest {

    private QuotaServiceMapper quotaServiceMapper;

    @BeforeEach
    void setUp() {
        quotaServiceMapper = new QuotaServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuotaServiceSample1();
        var actual = quotaServiceMapper.toEntity(quotaServiceMapper.toDto(expected));
        assertQuotaServiceAllPropertiesEquals(expected, actual);
    }
}
