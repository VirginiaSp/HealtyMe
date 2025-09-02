package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicationPlanDoseCriteriaTest {

    @Test
    void newMedicationPlanDoseCriteriaHasAllFiltersNullTest() {
        var medicationPlanDoseCriteria = new MedicationPlanDoseCriteria();
        assertThat(medicationPlanDoseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicationPlanDoseCriteriaFluentMethodsCreatesFiltersTest() {
        var medicationPlanDoseCriteria = new MedicationPlanDoseCriteria();

        setAllFilters(medicationPlanDoseCriteria);

        assertThat(medicationPlanDoseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicationPlanDoseCriteriaCopyCreatesNullFilterTest() {
        var medicationPlanDoseCriteria = new MedicationPlanDoseCriteria();
        var copy = medicationPlanDoseCriteria.copy();

        assertThat(medicationPlanDoseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationPlanDoseCriteria)
        );
    }

    @Test
    void medicationPlanDoseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicationPlanDoseCriteria = new MedicationPlanDoseCriteria();
        setAllFilters(medicationPlanDoseCriteria);

        var copy = medicationPlanDoseCriteria.copy();

        assertThat(medicationPlanDoseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationPlanDoseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicationPlanDoseCriteria = new MedicationPlanDoseCriteria();

        assertThat(medicationPlanDoseCriteria).hasToString("MedicationPlanDoseCriteria{}");
    }

    private static void setAllFilters(MedicationPlanDoseCriteria medicationPlanDoseCriteria) {
        medicationPlanDoseCriteria.id();
        medicationPlanDoseCriteria.timeOfDay();
        medicationPlanDoseCriteria.notes();
        medicationPlanDoseCriteria.ownerId();
        medicationPlanDoseCriteria.planId();
        medicationPlanDoseCriteria.distinct();
    }

    private static Condition<MedicationPlanDoseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTimeOfDay()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getPlanId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicationPlanDoseCriteria> copyFiltersAre(
        MedicationPlanDoseCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTimeOfDay(), copy.getTimeOfDay()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getPlanId(), copy.getPlanId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
