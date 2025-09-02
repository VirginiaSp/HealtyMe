package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MedicationRepositoryWithBagRelationshipsImpl implements MedicationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MEDICATIONS_PARAMETER = "medications";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Medication> fetchBagRelationships(Optional<Medication> medication) {
        return medication.map(this::fetchCategories);
    }

    @Override
    public Page<Medication> fetchBagRelationships(Page<Medication> medications) {
        return new PageImpl<>(fetchBagRelationships(medications.getContent()), medications.getPageable(), medications.getTotalElements());
    }

    @Override
    public List<Medication> fetchBagRelationships(List<Medication> medications) {
        return Optional.of(medications).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    Medication fetchCategories(Medication result) {
        return entityManager
            .createQuery(
                "select medication from Medication medication left join fetch medication.categories where medication.id = :id",
                Medication.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Medication> fetchCategories(List<Medication> medications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, medications.size()).forEach(index -> order.put(medications.get(index).getId(), index));
        List<Medication> result = entityManager
            .createQuery(
                "select medication from Medication medication left join fetch medication.categories where medication in :medications",
                Medication.class
            )
            .setParameter(MEDICATIONS_PARAMETER, medications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
