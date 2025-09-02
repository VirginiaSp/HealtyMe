package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicationCategoryCriteriaTest {

    @Test
    void newMedicationCategoryCriteriaHasAllFiltersNullTest() {
        var medicationCategoryCriteria = new MedicationCategoryCriteria();
        assertThat(medicationCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicationCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var medicationCategoryCriteria = new MedicationCategoryCriteria();

        setAllFilters(medicationCategoryCriteria);

        assertThat(medicationCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicationCategoryCriteriaCopyCreatesNullFilterTest() {
        var medicationCategoryCriteria = new MedicationCategoryCriteria();
        var copy = medicationCategoryCriteria.copy();

        assertThat(medicationCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationCategoryCriteria)
        );
    }

    @Test
    void medicationCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicationCategoryCriteria = new MedicationCategoryCriteria();
        setAllFilters(medicationCategoryCriteria);

        var copy = medicationCategoryCriteria.copy();

        assertThat(medicationCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicationCategoryCriteria = new MedicationCategoryCriteria();

        assertThat(medicationCategoryCriteria).hasToString("MedicationCategoryCriteria{}");
    }

    private static void setAllFilters(MedicationCategoryCriteria medicationCategoryCriteria) {
        medicationCategoryCriteria.id();
        medicationCategoryCriteria.name();
        medicationCategoryCriteria.description();
        medicationCategoryCriteria.ownerId();
        medicationCategoryCriteria.medicationsId();
        medicationCategoryCriteria.distinct();
    }

    private static Condition<MedicationCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getMedicationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicationCategoryCriteria> copyFiltersAre(
        MedicationCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getMedicationsId(), copy.getMedicationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
