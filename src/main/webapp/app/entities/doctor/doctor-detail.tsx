import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doctor.reducer';

export const DoctorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const doctorEntity = useAppSelector(state => state.doctor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="doctorDetailsHeading">
          <Translate contentKey="healthyMeApp.doctor.detail.title">Doctor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="healthyMeApp.doctor.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="healthyMeApp.doctor.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.lastName}</dd>
          <dt>
            <span id="specialty">
              <Translate contentKey="healthyMeApp.doctor.specialty">Specialty</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.specialty}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="healthyMeApp.doctor.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.phone}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="healthyMeApp.doctor.address">Address</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.address}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.doctor.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.notes}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.doctor.owner">Owner</Translate>
          </dt>
          <dd>{doctorEntity.owner ? doctorEntity.owner.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/doctor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doctor/${doctorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DoctorDetail;
