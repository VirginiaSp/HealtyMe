import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'app/shared/component';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './examination-category.reducer';

export const ExaminationCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const examinationCategoryEntity = useAppSelector(state => state.examinationCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examinationCategoryDetailsHeading">
          <Translate contentKey="healthyMeApp.examinationCategory.detail.title">ExaminationCategory</Translate>
        </h2>
        <dl className="entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examinationCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="healthyMeApp.examinationCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{examinationCategoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="healthyMeApp.examinationCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{examinationCategoryEntity.description}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.examinationCategory.owner">Owner</Translate>
          </dt>
          <dd>{examinationCategoryEntity.owner ? examinationCategoryEntity.owner.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/examination-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/examination-category/${examinationCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExaminationCategoryDetail;
