package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.TransactionPaiementAsserts.*;
import static sn.orange.conciergerie.domain.TransactionPaiementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionPaiementMapperTest {

    private TransactionPaiementMapper transactionPaiementMapper;

    @BeforeEach
    void setUp() {
        transactionPaiementMapper = new TransactionPaiementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransactionPaiementSample1();
        var actual = transactionPaiementMapper.toEntity(transactionPaiementMapper.toDto(expected));
        assertTransactionPaiementAllPropertiesEquals(expected, actual);
    }
}
