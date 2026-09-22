package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.TypeClientAsserts.*;
import static sn.orange.conciergerie.domain.TypeClientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeClientMapperTest {

    private TypeClientMapper typeClientMapper;

    @BeforeEach
    void setUp() {
        typeClientMapper = new TypeClientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeClientSample1();
        var actual = typeClientMapper.toEntity(typeClientMapper.toDto(expected));
        assertTypeClientAllPropertiesEquals(expected, actual);
    }
}
