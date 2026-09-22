package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.CodeQrServiceAsserts.*;
import static sn.orange.conciergerie.domain.CodeQrServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeQrServiceMapperTest {

    private CodeQrServiceMapper codeQrServiceMapper;

    @BeforeEach
    void setUp() {
        codeQrServiceMapper = new CodeQrServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCodeQrServiceSample1();
        var actual = codeQrServiceMapper.toEntity(codeQrServiceMapper.toDto(expected));
        assertCodeQrServiceAllPropertiesEquals(expected, actual);
    }
}
