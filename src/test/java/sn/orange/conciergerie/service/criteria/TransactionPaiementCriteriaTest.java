package sn.orange.conciergerie.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransactionPaiementCriteriaTest {

    @Test
    void newTransactionPaiementCriteriaHasAllFiltersNullTest() {
        var transactionPaiementCriteria = new TransactionPaiementCriteria();
        assertThat(transactionPaiementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transactionPaiementCriteriaFluentMethodsCreatesFiltersTest() {
        var transactionPaiementCriteria = new TransactionPaiementCriteria();

        setAllFilters(transactionPaiementCriteria);

        assertThat(transactionPaiementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transactionPaiementCriteriaCopyCreatesNullFilterTest() {
        var transactionPaiementCriteria = new TransactionPaiementCriteria();
        var copy = transactionPaiementCriteria.copy();

        assertThat(transactionPaiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionPaiementCriteria)
        );
    }

    @Test
    void transactionPaiementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transactionPaiementCriteria = new TransactionPaiementCriteria();
        setAllFilters(transactionPaiementCriteria);

        var copy = transactionPaiementCriteria.copy();

        assertThat(transactionPaiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionPaiementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transactionPaiementCriteria = new TransactionPaiementCriteria();

        assertThat(transactionPaiementCriteria).hasToString("TransactionPaiementCriteria{}");
    }

    private static void setAllFilters(TransactionPaiementCriteria transactionPaiementCriteria) {
        transactionPaiementCriteria.id();
        transactionPaiementCriteria.montant();
        transactionPaiementCriteria.modePaiement();
        transactionPaiementCriteria.statut();
        transactionPaiementCriteria.referenceExterne();
        transactionPaiementCriteria.dateTransaction();
        transactionPaiementCriteria.demandeId();
        transactionPaiementCriteria.distinct();
    }

    private static Condition<TransactionPaiementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMontant()) &&
                condition.apply(criteria.getModePaiement()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getReferenceExterne()) &&
                condition.apply(criteria.getDateTransaction()) &&
                condition.apply(criteria.getDemandeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransactionPaiementCriteria> copyFiltersAre(
        TransactionPaiementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMontant(), copy.getMontant()) &&
                condition.apply(criteria.getModePaiement(), copy.getModePaiement()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getReferenceExterne(), copy.getReferenceExterne()) &&
                condition.apply(criteria.getDateTransaction(), copy.getDateTransaction()) &&
                condition.apply(criteria.getDemandeId(), copy.getDemandeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
