package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.CompteStockAsserts.*;
import static sn.orange.conciergerie.domain.CompteStockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompteStockMapperTest {

    private CompteStockMapper compteStockMapper;

    @BeforeEach
    void setUp() {
        compteStockMapper = new CompteStockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompteStockSample1();
        var actual = compteStockMapper.toEntity(compteStockMapper.toDto(expected));
        assertCompteStockAllPropertiesEquals(expected, actual);
    }
}
