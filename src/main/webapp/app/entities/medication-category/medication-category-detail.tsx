import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medication-category.reducer';

export const MedicationCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medicationCategoryEntity = useAppSelector(state => state.medicationCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicationCategoryDetailsHeading">
          <Translate contentKey="healthyMeApp.medicationCategory.detail.title">MedicationCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{medicationCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="healthyMeApp.medicationCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{medicationCategoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="healthyMeApp.medicationCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{medicationCategoryEntity.description}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationCategory.owner">Owner</Translate>
          </dt>
          <dd>{medicationCategoryEntity.owner ? medicationCategoryEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationCategory.medications">Medications</Translate>
          </dt>
          <dd>
            {medicationCategoryEntity.medications
              ? medicationCategoryEntity.medications.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {medicationCategoryEntity.medications && i === medicationCategoryEntity.medications.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/medication-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medication-category/${medicationCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicationCategoryDetail;
