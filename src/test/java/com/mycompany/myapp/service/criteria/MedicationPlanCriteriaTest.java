package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicationPlanCriteriaTest {

    @Test
    void newMedicationPlanCriteriaHasAllFiltersNullTest() {
        var medicationPlanCriteria = new MedicationPlanCriteria();
        assertThat(medicationPlanCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicationPlanCriteriaFluentMethodsCreatesFiltersTest() {
        var medicationPlanCriteria = new MedicationPlanCriteria();

        setAllFilters(medicationPlanCriteria);

        assertThat(medicationPlanCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicationPlanCriteriaCopyCreatesNullFilterTest() {
        var medicationPlanCriteria = new MedicationPlanCriteria();
        var copy = medicationPlanCriteria.copy();

        assertThat(medicationPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationPlanCriteria)
        );
    }

    @Test
    void medicationPlanCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicationPlanCriteria = new MedicationPlanCriteria();
        setAllFilters(medicationPlanCriteria);

        var copy = medicationPlanCriteria.copy();

        assertThat(medicationPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationPlanCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicationPlanCriteria = new MedicationPlanCriteria();

        assertThat(medicationPlanCriteria).hasToString("MedicationPlanCriteria{}");
    }

    private static void setAllFilters(MedicationPlanCriteria medicationPlanCriteria) {
        medicationPlanCriteria.id();
        medicationPlanCriteria.planName();
        medicationPlanCriteria.startDate();
        medicationPlanCriteria.endDate();
        medicationPlanCriteria.medicationName();
        medicationPlanCriteria.colorHex();
        medicationPlanCriteria.dosesId();
        medicationPlanCriteria.ownerId();
        medicationPlanCriteria.distinct();
    }

    private static Condition<MedicationPlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPlanName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getMedicationName()) &&
                condition.apply(criteria.getColorHex()) &&
                condition.apply(criteria.getDosesId()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicationPlanCriteria> copyFiltersAre(
        MedicationPlanCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPlanName(), copy.getPlanName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getMedicationName(), copy.getMedicationName()) &&
                condition.apply(criteria.getColorHex(), copy.getColorHex()) &&
                condition.apply(criteria.getDosesId(), copy.getDosesId()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
