package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.UtilisationCodeQrAsserts.*;
import static sn.orange.conciergerie.domain.UtilisationCodeQrTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilisationCodeQrMapperTest {

    private UtilisationCodeQrMapper utilisationCodeQrMapper;

    @BeforeEach
    void setUp() {
        utilisationCodeQrMapper = new UtilisationCodeQrMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtilisationCodeQrSample1();
        var actual = utilisationCodeQrMapper.toEntity(utilisationCodeQrMapper.toDto(expected));
        assertUtilisationCodeQrAllPropertiesEquals(expected, actual);
    }
}
