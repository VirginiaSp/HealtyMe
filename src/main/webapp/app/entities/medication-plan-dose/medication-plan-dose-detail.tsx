import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'app/shared/component';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medication-plan-dose.reducer';

export const MedicationPlanDoseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medicationPlanDoseEntity = useAppSelector(state => state.medicationPlanDose.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicationPlanDoseDetailsHeading">
          <Translate contentKey="healthyMeApp.medicationPlanDose.detail.title">MedicationPlanDose</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{medicationPlanDoseEntity.id}</dd>
          <dt>
            <span id="timeOfDay">
              <Translate contentKey="healthyMeApp.medicationPlanDose.timeOfDay">Time Of Day</Translate>
            </span>
          </dt>
          <dd>{medicationPlanDoseEntity.timeOfDay}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.medicationPlanDose.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{medicationPlanDoseEntity.notes}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationPlanDose.owner">Owner</Translate>
          </dt>
          <dd>{medicationPlanDoseEntity.owner ? medicationPlanDoseEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationPlanDose.plan">Plan</Translate>
          </dt>
          <dd>{medicationPlanDoseEntity.plan ? medicationPlanDoseEntity.plan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/medication-plan-dose" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medication-plan-dose/${medicationPlanDoseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicationPlanDoseDetail;
