import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './appointment.reducer';

export const AppointmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appointmentEntity = useAppSelector(state => state.appointment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appointmentDetailsHeading">
          <Translate contentKey="healthyMeApp.appointment.detail.title">Appointment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.id}</dd>
          <dt>
            <span id="startDateTime">
              <Translate contentKey="healthyMeApp.appointment.startDateTime">Start Date Time</Translate>
            </span>
          </dt>
          <dd>
            {appointmentEntity.startDateTime ? (
              <TextFormat value={appointmentEntity.startDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="durationMinutes">
              <Translate contentKey="healthyMeApp.appointment.durationMinutes">Duration Minutes</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.durationMinutes}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="healthyMeApp.appointment.address">Address</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.address}</dd>
          <dt>
            <span id="specialtySnapshot">
              <Translate contentKey="healthyMeApp.appointment.specialtySnapshot">Specialty Snapshot</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.specialtySnapshot}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.appointment.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.notes}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="healthyMeApp.appointment.status">Status</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.status}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.appointment.owner">Owner</Translate>
          </dt>
          <dd>{appointmentEntity.owner ? appointmentEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.appointment.doctor">Doctor</Translate>
          </dt>
          <dd>{appointmentEntity.doctor ? appointmentEntity.doctor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/appointment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appointment/${appointmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppointmentDetail;
