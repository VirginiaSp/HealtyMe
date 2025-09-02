import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medication.reducer';

export const MedicationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medicationEntity = useAppSelector(state => state.medication.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicationDetailsHeading">
          <Translate contentKey="healthyMeApp.medication.detail.title">Medication</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{medicationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="healthyMeApp.medication.name">Name</Translate>
            </span>
          </dt>
          <dd>{medicationEntity.name}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="healthyMeApp.medication.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{medicationEntity.rating}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.medication.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{medicationEntity.notes}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medication.owner">Owner</Translate>
          </dt>
          <dd>{medicationEntity.owner ? medicationEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medication.categories">Categories</Translate>
          </dt>
          <dd>
            {medicationEntity.categories
              ? medicationEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {medicationEntity.categories && i === medicationEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/medication" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medication/${medicationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicationDetail;
