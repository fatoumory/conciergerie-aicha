package sn.orange.conciergerie.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DemandeCriteriaTest {

    @Test
    void newDemandeCriteriaHasAllFiltersNullTest() {
        var demandeCriteria = new DemandeCriteria();
        assertThat(demandeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void demandeCriteriaFluentMethodsCreatesFiltersTest() {
        var demandeCriteria = new DemandeCriteria();

        setAllFilters(demandeCriteria);

        assertThat(demandeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void demandeCriteriaCopyCreatesNullFilterTest() {
        var demandeCriteria = new DemandeCriteria();
        var copy = demandeCriteria.copy();

        assertThat(demandeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(demandeCriteria)
        );
    }

    @Test
    void demandeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var demandeCriteria = new DemandeCriteria();
        setAllFilters(demandeCriteria);

        var copy = demandeCriteria.copy();

        assertThat(demandeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(demandeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var demandeCriteria = new DemandeCriteria();

        assertThat(demandeCriteria).hasToString("DemandeCriteria{}");
    }

    private static void setAllFilters(DemandeCriteria demandeCriteria) {
        demandeCriteria.id();
        demandeCriteria.dateCreation();
        demandeCriteria.description();
        demandeCriteria.clientId();
        demandeCriteria.serviceId();
        demandeCriteria.typeDemandeId();
        demandeCriteria.statutId();
        demandeCriteria.codePromoId();
        demandeCriteria.prestationId();
        demandeCriteria.codeQrServiceId();
        demandeCriteria.factureId();
        demandeCriteria.evaluationId();
        demandeCriteria.distinct();
    }

    private static Condition<DemandeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getServiceId()) &&
                condition.apply(criteria.getTypeDemandeId()) &&
                condition.apply(criteria.getStatutId()) &&
                condition.apply(criteria.getCodePromoId()) &&
                condition.apply(criteria.getPrestationId()) &&
                condition.apply(criteria.getCodeQrServiceId()) &&
                condition.apply(criteria.getFactureId()) &&
                condition.apply(criteria.getEvaluationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DemandeCriteria> copyFiltersAre(DemandeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getServiceId(), copy.getServiceId()) &&
                condition.apply(criteria.getTypeDemandeId(), copy.getTypeDemandeId()) &&
                condition.apply(criteria.getStatutId(), copy.getStatutId()) &&
                condition.apply(criteria.getCodePromoId(), copy.getCodePromoId()) &&
                condition.apply(criteria.getPrestationId(), copy.getPrestationId()) &&
                condition.apply(criteria.getCodeQrServiceId(), copy.getCodeQrServiceId()) &&
                condition.apply(criteria.getFactureId(), copy.getFactureId()) &&
                condition.apply(criteria.getEvaluationId(), copy.getEvaluationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
