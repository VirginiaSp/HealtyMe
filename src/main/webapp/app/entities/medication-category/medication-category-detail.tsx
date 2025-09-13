import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
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
        <dl className="entity-details">
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
            <span id="color">
              <Translate contentKey="healthyMeApp.medicationCategory.color">Color</Translate>
            </span>
          </dt>
          <dd>{medicationCategoryEntity.color}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="healthyMeApp.medicationCategory.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{medicationCategoryEntity.icon}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="healthyMeApp.medicationCategory.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {medicationCategoryEntity.createdDate ? (
              <TextFormat value={medicationCategoryEntity.createdDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationCategory.createdBy">Created By</Translate>
          </dt>
          <dd>{medicationCategoryEntity.createdBy ? medicationCategoryEntity.createdBy.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.medicationCategory.medications">Medications</Translate>
          </dt>
          <dd>
            {medicationCategoryEntity.medications
              ? medicationCategoryEntity.medications.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
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
