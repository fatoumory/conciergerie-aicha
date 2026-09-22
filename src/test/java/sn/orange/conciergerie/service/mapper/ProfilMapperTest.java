package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.ProfilAsserts.*;
import static sn.orange.conciergerie.domain.ProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilMapperTest {

    private ProfilMapper profilMapper;

    @BeforeEach
    void setUp() {
        profilMapper = new ProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfilSample1();
        var actual = profilMapper.toEntity(profilMapper.toDto(expected));
        assertProfilAllPropertiesEquals(expected, actual);
    }
}
