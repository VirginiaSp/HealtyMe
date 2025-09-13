import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'app/shared/component';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './visit.reducer';

export const VisitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const visitEntity = useAppSelector(state => state.visit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="visitDetailsHeading">
          <Translate contentKey="healthyMeApp.visit.detail.title">Visit</Translate>
        </h2>
        <dl className="entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{visitEntity.id}</dd>
          <dt>
            <span id="visitDate">
              <Translate contentKey="healthyMeApp.visit.visitDate">Visit Date</Translate>
            </span>
          </dt>
          <dd>{visitEntity.visitDate ? <TextFormat value={visitEntity.visitDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.visit.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{visitEntity.notes}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.visit.owner">Owner</Translate>
          </dt>
          <dd>{visitEntity.owner ? visitEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.visit.doctor">Doctor</Translate>
          </dt>
          <dd>{visitEntity.doctor ? visitEntity.doctor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/visit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/visit/${visitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VisitDetail;
