import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { Translate } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './examination-record.reducer';
import { IExaminationRecord } from 'app/shared/model/examination-record.model';

const isImage = (ct?: string) => !!ct && /^image\//i.test(ct);

const getRec = (rec: IExaminationRecord) => {
  const anyRec = rec as any;
  const category = anyRec.category ?? anyRec.examinationCategory ?? anyRec.examCategory;
  const categoryName = category?.name ?? anyRec.examinationCategoryName ?? 'Uncategorized';
  const date = anyRec.examDate ?? anyRec.date ?? anyRec.examinationDate ?? anyRec.createdDate ?? rec['createdDate'];
  const fileCt =
    anyRec.fileContentType ?? anyRec.attachmentContentType ?? anyRec.documentContentType ?? anyRec.binaryContentType ?? anyRec.contentType;
  const fileData = anyRec.file ?? anyRec.attachment ?? anyRec.document ?? anyRec.binary ?? anyRec.content;
  const filename = anyRec.originalFilename ?? anyRec.filename ?? anyRec.originalFileName ?? anyRec.name ?? 'exam';
  const notes = anyRec.notes ?? anyRec.note ?? anyRec.description ?? anyRec.comments ?? '';
  return { categoryName, date, fileCt, fileData, filename, notes };
};

export const ExaminationRecord = () => {
  const dispatch = useAppDispatch();

  const examinationRecordList = useAppSelector(state => state.examinationRecord.entities);
  const loading = useAppSelector(state => state.examinationRecord.loading);

  const [openRowId, setOpenRowId] = useState<number | string | undefined>(undefined);

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch]);

  const grouped = useMemo(() => {
    const map: Record<string, IExaminationRecord[]> = {};
    (examinationRecordList ?? []).forEach(rec => {
      const { categoryName } = getRec(rec);
      if (!map[categoryName]) map[categoryName] = [];
      map[categoryName].push(rec);
    });
    return map;
  }, [examinationRecordList]);

  return (
    <div>
      <h2 id="examination-record-heading" data-cy="ExaminationRecordHeading">
        <Translate contentKey="healthyMeApp.examinationRecord.home.title">Examination Records</Translate>
        <div className="d-flex justify-content-end">
          <Link to="/examination-record/new" className="btn btn-primary jh-create-entity" id="jh-create-entity">
            <Translate contentKey="healthyMeApp.examinationRecord.home.createLabel">Create new Examination Record</Translate>
          </Link>
        </div>
      </h2>

      <Row className="justify-content-center">
        <Col md="12">
          {Object.keys(grouped).length === 0
            ? !loading && (
                <div className="alert alert-warning">
                  <Translate contentKey="healthyMeApp.examinationRecord.home.notFound">No Examination Records found</Translate>
                </div>
              )
            : Object.entries(grouped).map(([cat, items]) => (
                <div key={cat} className="mb-4">
                  <h4 className="mb-3">{cat}</h4>
                  <Table responsive>
                    <thead>
                      <tr>
                        <th>
                          <Translate contentKey="global.field.id">ID</Translate>
                        </th>
                        <th>
                          <Translate contentKey="healthyMeApp.examinationRecord.category">Category</Translate>
                        </th>
                        <th>
                          <Translate contentKey="healthyMeApp.examinationRecord.date">Date</Translate>
                        </th>
                        <th>
                          <Translate contentKey="healthyMeApp.examinationRecord.file">File</Translate>
                        </th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {items.map(rec => {
                        const { categoryName, date, fileCt, fileData, filename, notes } = getRec(rec);
                        return (
                          <React.Fragment key={rec.id}>
                            <tr
                              onClick={() => setOpenRowId(openRowId === rec.id ? undefined : (rec.id as any))}
                              style={{ cursor: 'pointer' }}
                            >
                              <td>
                                <Button tag={Link} to={`/examination-record/${rec.id}`} color="link" size="sm">
                                  {rec.id}
                                </Button>
                              </td>
                              <td>{categoryName}</td>
                              <td>{date ? <TextFormat type="date" value={date} format={APP_DATE_FORMAT} /> : null}</td>
                              <td>
                                {isImage(fileCt) && fileData ? (
                                  <img
                                    src={`data:${fileCt};base64,${fileData}`}
                                    alt={filename}
                                    style={{ maxWidth: 120, maxHeight: 80, objectFit: 'cover', borderRadius: 6 }}
                                    onClick={e => e.stopPropagation()}
                                  />
                                ) : fileData ? (
                                  <a
                                    onClick={e => e.stopPropagation()}
                                    href={`data:${fileCt ?? 'application/octet-stream'};base64,${fileData}`}
                                    download={filename}
                                  >
                                    <Translate contentKey="entity.action.download">Download</Translate>
                                  </a>
                                ) : (
                                  <span>-</span>
                                )}
                              </td>
                              <td className="text-end">
                                <div className="btn-group flex-btn-group-container">
                                  <Button
                                    tag={Link}
                                    to={`/examination-record/${rec.id}`}
                                    color="info"
                                    size="sm"
                                    data-cy="entityDetailsButton"
                                  >
                                    <Translate contentKey="entity.action.view">View</Translate>
                                  </Button>
                                  <Button
                                    tag={Link}
                                    to={`/examination-record/${rec.id}/edit`}
                                    color="primary"
                                    size="sm"
                                    data-cy="entityEditButton"
                                  >
                                    <Translate contentKey="entity.action.edit">Edit</Translate>
                                  </Button>
                                  <Button
                                    tag={Link}
                                    to={`/examination-record/${rec.id}/delete`}
                                    color="danger"
                                    size="sm"
                                    data-cy="entityDeleteButton"
                                  >
                                    <Translate contentKey="entity.action.delete">Delete</Translate>
                                  </Button>
                                </div>
                              </td>
                            </tr>

                            {openRowId === rec.id && !!notes && (
                              <tr>
                                <td colSpan={5}>
                                  <div className="p-3 bg-light rounded">{notes}</div>
                                </td>
                              </tr>
                            )}
                          </React.Fragment>
                        );
                      })}
                    </tbody>
                  </Table>
                </div>
              ))}
        </Col>
      </Row>
    </div>
  );
};

export default ExaminationRecord;
