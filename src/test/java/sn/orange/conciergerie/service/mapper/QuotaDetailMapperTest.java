package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.QuotaDetailAsserts.*;
import static sn.orange.conciergerie.domain.QuotaDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuotaDetailMapperTest {

    private QuotaDetailMapper quotaDetailMapper;

    @BeforeEach
    void setUp() {
        quotaDetailMapper = new QuotaDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuotaDetailSample1();
        var actual = quotaDetailMapper.toEntity(quotaDetailMapper.toDto(expected));
        assertQuotaDetailAllPropertiesEquals(expected, actual);
    }
}
