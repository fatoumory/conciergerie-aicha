package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.CouverturePartenaireAsserts.*;
import static sn.orange.conciergerie.domain.CouverturePartenaireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CouverturePartenaireMapperTest {

    private CouverturePartenaireMapper couverturePartenaireMapper;

    @BeforeEach
    void setUp() {
        couverturePartenaireMapper = new CouverturePartenaireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCouverturePartenaireSample1();
        var actual = couverturePartenaireMapper.toEntity(couverturePartenaireMapper.toDto(expected));
        assertCouverturePartenaireAllPropertiesEquals(expected, actual);
    }
}
