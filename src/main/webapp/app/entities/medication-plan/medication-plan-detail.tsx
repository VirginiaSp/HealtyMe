import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medication-plan.reducer';

export const MedicationPlanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medicationPlanEntity = useAppSelector(state => state.medicationPlan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicationPlanDetailsHeading">
          <Translate contentKey="healthyMeApp.medicationPlan.detail.title">MedicationPlan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{medicationPlanEntity.id}</dd>
          <dt>
            <span id="planName">
              <Translate contentKey="healthyMeApp.medicationPlan.planName">Plan Name</Translate>
            </span>
          </dt>
          <dd>{medicationPlanEntity.planName}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="healthyMeApp.medicationPlan.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {medicationPlanEntity.startDate ? (
              <TextFormat value={medicationPlanEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="healthyMeApp.medicationPlan.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {medicationPlanEntity.endDate ? (
              <TextFormat value={medicationPlanEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="medicationName">
              <Translate contentKey="healthyMeApp.medicationPlan.medicationName">Medication Name</Translate>
            </span>
          </dt>
          <dd>{medicationPlanEntity.medicationName}</dd>
          <dt>
            <span id="colorHex">
              <Translate contentKey="healthyMeApp.medicationPlan.colorHex">Color Hex</Translate>
            </span>
          </dt>
          <dd>{medicationPlanEntity.colorHex}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationPlan.owner">Owner</Translate>
          </dt>
          <dd>{medicationPlanEntity.owner ? medicationPlanEntity.owner.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/medication-plan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medication-plan/${medicationPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicationPlanDetail;
