package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.CodePromoAsserts.*;
import static sn.orange.conciergerie.domain.CodePromoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodePromoMapperTest {

    private CodePromoMapper codePromoMapper;

    @BeforeEach
    void setUp() {
        codePromoMapper = new CodePromoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCodePromoSample1();
        var actual = codePromoMapper.toEntity(codePromoMapper.toDto(expected));
        assertCodePromoAllPropertiesEquals(expected, actual);
    }
}
