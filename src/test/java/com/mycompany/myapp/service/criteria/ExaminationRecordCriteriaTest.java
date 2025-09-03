package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

class ExaminationRecordCriteriaTest {

    @Test
    void newExaminationRecordCriteriaHasAllFiltersNullTest() {
        var examinationRecordCriteria = new ExaminationRecordCriteria();
        assertThat(examinationRecordCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void examinationRecordCriteriaSettersCreateFiltersTest() {
        var examinationRecordCriteria = new ExaminationRecordCriteria();

        setAllFilters(examinationRecordCriteria);

        assertThat(examinationRecordCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void examinationRecordCriteriaCopyCreatesNullFilterTest() {
        var examinationRecordCriteria = new ExaminationRecordCriteria();
        var copy = examinationRecordCriteria.copy();

        assertThat(examinationRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && Objects.equals(a, b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(examinationRecordCriteria)
        );
    }

    @Test
    void examinationRecordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var examinationRecordCriteria = new ExaminationRecordCriteria();
        setAllFilters(examinationRecordCriteria);

        var copy = examinationRecordCriteria.copy();

        assertThat(examinationRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && Objects.equals(a, b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(examinationRecordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var examinationRecordCriteria = new ExaminationRecordCriteria();

        assertThat(examinationRecordCriteria).hasToString("ExaminationRecordCriteria{}");
    }

    private static void setAllFilters(ExaminationRecordCriteria examinationRecordCriteria) {
        examinationRecordCriteria.setId(new LongFilter());
        examinationRecordCriteria.setTitle(new StringFilter());
        examinationRecordCriteria.setExamDate(new LocalDateFilter());
        examinationRecordCriteria.setOriginalFilename(new StringFilter());
        examinationRecordCriteria.setStoredFilename(new StringFilter());
        examinationRecordCriteria.setNotes(new StringFilter());
        examinationRecordCriteria.setOwnerId(new LongFilter());
        examinationRecordCriteria.setCategoryId(new LongFilter());
        examinationRecordCriteria.setDistinct(Boolean.TRUE);
    }

    private static Condition<ExaminationRecordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getExamDate()) &&
                condition.apply(criteria.getOriginalFilename()) &&
                condition.apply(criteria.getStoredFilename()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExaminationRecordCriteria> copyFiltersAre(
        ExaminationRecordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getExamDate(), copy.getExamDate()) &&
                condition.apply(criteria.getOriginalFilename(), copy.getOriginalFilename()) &&
                condition.apply(criteria.getStoredFilename(), copy.getStoredFilename()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
