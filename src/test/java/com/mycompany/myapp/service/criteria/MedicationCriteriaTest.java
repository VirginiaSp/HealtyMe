package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicationCriteriaTest {

    @Test
    void newMedicationCriteriaHasAllFiltersNullTest() {
        var medicationCriteria = new MedicationCriteria();
        assertThat(medicationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicationCriteriaFluentMethodsCreatesFiltersTest() {
        var medicationCriteria = new MedicationCriteria();

        setAllFilters(medicationCriteria);

        assertThat(medicationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicationCriteriaCopyCreatesNullFilterTest() {
        var medicationCriteria = new MedicationCriteria();
        var copy = medicationCriteria.copy();

        assertThat(medicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationCriteria)
        );
    }

    @Test
    void medicationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicationCriteria = new MedicationCriteria();
        setAllFilters(medicationCriteria);

        var copy = medicationCriteria.copy();

        assertThat(medicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicationCriteria = new MedicationCriteria();

        assertThat(medicationCriteria).hasToString("MedicationCriteria{}");
    }

    private static void setAllFilters(MedicationCriteria medicationCriteria) {
        medicationCriteria.id();
        medicationCriteria.name();
        medicationCriteria.rating();
        medicationCriteria.notes();
        medicationCriteria.ownerId();
        medicationCriteria.categoriesId();
        medicationCriteria.distinct();
    }

    private static Condition<MedicationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getCategoriesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicationCriteria> copyFiltersAre(MedicationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getCategoriesId(), copy.getCategoriesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
