package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.MouvementStockAsserts.*;
import static sn.orange.conciergerie.domain.MouvementStockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MouvementStockMapperTest {

    private MouvementStockMapper mouvementStockMapper;

    @BeforeEach
    void setUp() {
        mouvementStockMapper = new MouvementStockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMouvementStockSample1();
        var actual = mouvementStockMapper.toEntity(mouvementStockMapper.toDto(expected));
        assertMouvementStockAllPropertiesEquals(expected, actual);
    }
}
