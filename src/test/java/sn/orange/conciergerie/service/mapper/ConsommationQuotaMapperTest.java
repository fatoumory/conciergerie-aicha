package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.ConsommationQuotaAsserts.*;
import static sn.orange.conciergerie.domain.ConsommationQuotaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsommationQuotaMapperTest {

    private ConsommationQuotaMapper consommationQuotaMapper;

    @BeforeEach
    void setUp() {
        consommationQuotaMapper = new ConsommationQuotaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsommationQuotaSample1();
        var actual = consommationQuotaMapper.toEntity(consommationQuotaMapper.toDto(expected));
        assertConsommationQuotaAllPropertiesEquals(expected, actual);
    }
}
