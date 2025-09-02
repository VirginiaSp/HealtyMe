import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './examination-record.reducer';

export const ExaminationRecordDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const examinationRecordEntity = useAppSelector(state => state.examinationRecord.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examinationRecordDetailsHeading">
          <Translate contentKey="healthyMeApp.examinationRecord.detail.title">ExaminationRecord</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examinationRecordEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="healthyMeApp.examinationRecord.title">Title</Translate>
            </span>
          </dt>
          <dd>{examinationRecordEntity.title}</dd>
          <dt>
            <span id="examDate">
              <Translate contentKey="healthyMeApp.examinationRecord.examDate">Exam Date</Translate>
            </span>
          </dt>
          <dd>
            {examinationRecordEntity.examDate ? (
              <TextFormat value={examinationRecordEntity.examDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="file">
              <Translate contentKey="healthyMeApp.examinationRecord.file">File</Translate>
            </span>
          </dt>
          <dd>
            {examinationRecordEntity.file ? (
              <div>
                {examinationRecordEntity.fileContentType ? (
                  <a onClick={openFile(examinationRecordEntity.fileContentType, examinationRecordEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {examinationRecordEntity.fileContentType}, {byteSize(examinationRecordEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="originalFilename">
              <Translate contentKey="healthyMeApp.examinationRecord.originalFilename">Original Filename</Translate>
            </span>
          </dt>
          <dd>{examinationRecordEntity.originalFilename}</dd>
          <dt>
            <span id="storedFilename">
              <Translate contentKey="healthyMeApp.examinationRecord.storedFilename">Stored Filename</Translate>
            </span>
          </dt>
          <dd>{examinationRecordEntity.storedFilename}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="healthyMeApp.examinationRecord.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{examinationRecordEntity.notes}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.examinationRecord.owner">Owner</Translate>
          </dt>
          <dd>{examinationRecordEntity.owner ? examinationRecordEntity.owner.login : ''}</dd>
          <dt>
            <Translate contentKey="healthyMeApp.examinationRecord.category">Category</Translate>
          </dt>
          <dd>{examinationRecordEntity.category ? examinationRecordEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/examination-record" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/examination-record/${examinationRecordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExaminationRecordDetail;
