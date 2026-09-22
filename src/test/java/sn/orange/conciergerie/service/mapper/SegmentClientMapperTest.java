package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.SegmentClientAsserts.*;
import static sn.orange.conciergerie.domain.SegmentClientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SegmentClientMapperTest {

    private SegmentClientMapper segmentClientMapper;

    @BeforeEach
    void setUp() {
        segmentClientMapper = new SegmentClientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSegmentClientSample1();
        var actual = segmentClientMapper.toEntity(segmentClientMapper.toDto(expected));
        assertSegmentClientAllPropertiesEquals(expected, actual);
    }
}
