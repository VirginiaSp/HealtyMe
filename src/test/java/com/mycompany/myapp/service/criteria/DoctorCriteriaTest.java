package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DoctorCriteriaTest {

    @Test
    void newDoctorCriteriaHasAllFiltersNullTest() {
        var doctorCriteria = new DoctorCriteria();
        assertThat(doctorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void doctorCriteriaFluentMethodsCreatesFiltersTest() {
        var doctorCriteria = new DoctorCriteria();

        setAllFilters(doctorCriteria);

        assertThat(doctorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void doctorCriteriaCopyCreatesNullFilterTest() {
        var doctorCriteria = new DoctorCriteria();
        var copy = doctorCriteria.copy();

        assertThat(doctorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(doctorCriteria)
        );
    }

    @Test
    void doctorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var doctorCriteria = new DoctorCriteria();
        setAllFilters(doctorCriteria);

        var copy = doctorCriteria.copy();

        assertThat(doctorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(doctorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var doctorCriteria = new DoctorCriteria();

        assertThat(doctorCriteria).hasToString("DoctorCriteria{}");
    }

    private static void setAllFilters(DoctorCriteria doctorCriteria) {
        doctorCriteria.id();
        doctorCriteria.firstName();
        doctorCriteria.lastName();
        doctorCriteria.specialty();
        doctorCriteria.phone();
        doctorCriteria.address();
        doctorCriteria.notes();
        doctorCriteria.ownerId();
        doctorCriteria.distinct();
    }

    private static Condition<DoctorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getSpecialty()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DoctorCriteria> copyFiltersAre(DoctorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getSpecialty(), copy.getSpecialty()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
