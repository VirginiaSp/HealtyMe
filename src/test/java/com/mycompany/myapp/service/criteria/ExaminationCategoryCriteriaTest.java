package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExaminationCategoryCriteriaTest {

    @Test
    void newExaminationCategoryCriteriaHasAllFiltersNullTest() {
        var examinationCategoryCriteria = new ExaminationCategoryCriteria();
        assertThat(examinationCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void examinationCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var examinationCategoryCriteria = new ExaminationCategoryCriteria();

        setAllFilters(examinationCategoryCriteria);

        assertThat(examinationCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void examinationCategoryCriteriaCopyCreatesNullFilterTest() {
        var examinationCategoryCriteria = new ExaminationCategoryCriteria();
        var copy = examinationCategoryCriteria.copy();

        assertThat(examinationCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(examinationCategoryCriteria)
        );
    }

    @Test
    void examinationCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var examinationCategoryCriteria = new ExaminationCategoryCriteria();
        setAllFilters(examinationCategoryCriteria);

        var copy = examinationCategoryCriteria.copy();

        assertThat(examinationCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(examinationCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var examinationCategoryCriteria = new ExaminationCategoryCriteria();

        assertThat(examinationCategoryCriteria).hasToString("ExaminationCategoryCriteria{}");
    }

    private static void setAllFilters(ExaminationCategoryCriteria examinationCategoryCriteria) {
        examinationCategoryCriteria.id();
        examinationCategoryCriteria.name();
        examinationCategoryCriteria.description();
        examinationCategoryCriteria.ownerId();
        examinationCategoryCriteria.distinct();
    }

    private static Condition<ExaminationCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExaminationCategoryCriteria> copyFiltersAre(
        ExaminationCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
