package sn.orange.conciergerie.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConsommationQuotaCriteriaTest {

    @Test
    void newConsommationQuotaCriteriaHasAllFiltersNullTest() {
        var consommationQuotaCriteria = new ConsommationQuotaCriteria();
        assertThat(consommationQuotaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void consommationQuotaCriteriaFluentMethodsCreatesFiltersTest() {
        var consommationQuotaCriteria = new ConsommationQuotaCriteria();

        setAllFilters(consommationQuotaCriteria);

        assertThat(consommationQuotaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void consommationQuotaCriteriaCopyCreatesNullFilterTest() {
        var consommationQuotaCriteria = new ConsommationQuotaCriteria();
        var copy = consommationQuotaCriteria.copy();

        assertThat(consommationQuotaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(consommationQuotaCriteria)
        );
    }

    @Test
    void consommationQuotaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var consommationQuotaCriteria = new ConsommationQuotaCriteria();
        setAllFilters(consommationQuotaCriteria);

        var copy = consommationQuotaCriteria.copy();

        assertThat(consommationQuotaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(consommationQuotaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var consommationQuotaCriteria = new ConsommationQuotaCriteria();

        assertThat(consommationQuotaCriteria).hasToString("ConsommationQuotaCriteria{}");
    }

    private static void setAllFilters(ConsommationQuotaCriteria consommationQuotaCriteria) {
        consommationQuotaCriteria.id();
        consommationQuotaCriteria.quantite();
        consommationQuotaCriteria.dateConsommation();
        consommationQuotaCriteria.clientId();
        consommationQuotaCriteria.quotaServiceId();
        consommationQuotaCriteria.quotaDetailId();
        consommationQuotaCriteria.distinct();
    }

    private static Condition<ConsommationQuotaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantite()) &&
                condition.apply(criteria.getDateConsommation()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getQuotaServiceId()) &&
                condition.apply(criteria.getQuotaDetailId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConsommationQuotaCriteria> copyFiltersAre(
        ConsommationQuotaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantite(), copy.getQuantite()) &&
                condition.apply(criteria.getDateConsommation(), copy.getDateConsommation()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getQuotaServiceId(), copy.getQuotaServiceId()) &&
                condition.apply(criteria.getQuotaDetailId(), copy.getQuotaDetailId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
