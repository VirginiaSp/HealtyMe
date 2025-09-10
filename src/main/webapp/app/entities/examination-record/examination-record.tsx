import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Col, Row, Table, ButtonGroup } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { Translate } from 'react-jhipster';
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

// Custom date formatter - removes time and shows full year
const formatDateOnly = (date: string | Date) => {
  if (!date) return null;
  const d = new Date(date);
  return d.toLocaleDateString('en-GB', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

export const ExaminationRecord = () => {
  const dispatch = useAppDispatch();

  const examinationRecordList = useAppSelector(state => state.examinationRecord.entities);
  const loading = useAppSelector(state => state.examinationRecord.loading);

  const [openRowId, setOpenRowId] = useState<number | string | undefined>(undefined);
  const [viewMode, setViewMode] = useState<'timeline' | 'category'>('timeline');
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);

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

  const sortedRecords = useMemo(() => {
    return [...(examinationRecordList ?? [])].sort((a, b) => {
      const { date: dateA } = getRec(a);
      const { date: dateB } = getRec(b);
      return new Date(dateB || 0).getTime() - new Date(dateA || 0).getTime();
    });
  }, [examinationRecordList]);

  const handleFileClick = (fileCt: string, fileData: string, filename: string) => {
    if (isImage(fileCt)) {
      // Open image in new window/tab for full view
      const newWindow = window.open();
      if (newWindow) {
        newWindow.document.write(`
          <html>
            <head><title>${filename}</title></head>
            <body style="margin:0; display:flex; justify-content:center; align-items:center; min-height:100vh; background:#f5f5f5;">
              <img src="data:${fileCt};base64,${fileData}" alt="${filename}" style="max-width:100%; max-height:100%; object-fit:contain;" />
            </body>
          </html>
        `);
      }
    } else {
      // Download non-image files
      const link = document.createElement('a');
      link.href = `data:${fileCt ?? 'application/octet-stream'};base64,${fileData}`;
      link.download = filename;
      link.click();
    }
  };

  const renderTimelineView = () => (
    <Table responsive>
      <thead>
        <tr>
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
        {sortedRecords.map(rec => {
          const { categoryName, date, fileCt, fileData, filename, notes } = getRec(rec);
          return (
            <React.Fragment key={rec.id}>
              <tr onClick={() => setOpenRowId(openRowId === rec.id ? undefined : rec.id)} style={{ cursor: 'pointer' }}>
                <td>{categoryName}</td>
                <td>{formatDateOnly(date)}</td>
                <td>
                  {fileData ? (
                    <div
                      onClick={e => {
                        e.stopPropagation();
                        handleFileClick(fileCt, fileData, filename);
                      }}
                      style={{ cursor: 'pointer' }}
                    >
                      {isImage(fileCt) ? (
                        <img
                          src={`data:${fileCt};base64,${fileData}`}
                          alt={filename}
                          style={{ maxWidth: 120, maxHeight: 80, objectFit: 'cover', borderRadius: 6 }}
                        />
                      ) : (
                        <Button color="link" size="sm">
                          📎 {filename}
                        </Button>
                      )}
                    </div>
                  ) : (
                    <span>-</span>
                  )}
                </td>
                <td className="text-end">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`/examination-record/${rec.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </Button>
                    <Button tag={Link} to={`/examination-record/${rec.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </Button>
                    <Button tag={Link} to={`/examination-record/${rec.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                      <Translate contentKey="entity.action.delete">Delete</Translate>
                    </Button>
                  </div>
                </td>
              </tr>

              {openRowId === rec.id && !!notes && (
                <tr>
                  <td colSpan={4}>
                    <div className="p-3 bg-light rounded">{notes}</div>
                  </td>
                </tr>
              )}
            </React.Fragment>
          );
        })}
      </tbody>
    </Table>
  );

  const renderCategorySelection = () => (
    <div className="mb-4">
      <h4>Select a Category:</h4>
      <div className="d-flex flex-wrap gap-2">
        {Object.keys(grouped).map(categoryName => (
          <Button
            key={categoryName}
            color={selectedCategory === categoryName ? 'primary' : 'outline-primary'}
            onClick={() => setSelectedCategory(selectedCategory === categoryName ? null : categoryName)}
            className="mb-2"
          >
            {categoryName} ({grouped[categoryName].length})
          </Button>
        ))}
      </div>
    </div>
  );

  const renderCategoryRecords = () => {
    if (!selectedCategory || !grouped[selectedCategory]) return null;

    const categoryRecords = grouped[selectedCategory].sort((a, b) => {
      const { date: dateA } = getRec(a);
      const { date: dateB } = getRec(b);
      return new Date(dateB || 0).getTime() - new Date(dateA || 0).getTime();
    });

    return (
      <div>
        <h4 className="mb-3">{selectedCategory} Records</h4>
        <Table responsive>
          <thead>
            <tr>
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
            {categoryRecords.map(rec => {
              const { date, fileCt, fileData, filename, notes } = getRec(rec);
              return (
                <React.Fragment key={rec.id}>
                  <tr onClick={() => setOpenRowId(openRowId === rec.id ? undefined : (rec.id as any))} style={{ cursor: 'pointer' }}>
                    <td>{formatDateOnly(date)}</td>
                    <td>
                      {fileData ? (
                        <div
                          onClick={e => {
                            e.stopPropagation();
                            handleFileClick(fileCt, fileData, filename);
                          }}
                          style={{ cursor: 'pointer' }}
                        >
                          {isImage(fileCt) ? (
                            <img
                              src={`data:${fileCt};base64,${fileData}`}
                              alt={filename}
                              style={{ maxWidth: 120, maxHeight: 80, objectFit: 'cover', borderRadius: 6 }}
                            />
                          ) : (
                            <Button color="link" size="sm">
                              📎 {filename}
                            </Button>
                          )}
                        </div>
                      ) : (
                        <span>-</span>
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/examination-record/${rec.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </Button>
                        <Button tag={Link} to={`/examination-record/${rec.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
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
                      <td colSpan={3}>
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
    );
  };

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

      {/* View Mode Toggle */}
      <div className="mb-4">
        <ButtonGroup>
          <Button
            color={viewMode === 'timeline' ? 'primary' : 'outline-primary'}
            onClick={() => {
              setViewMode('timeline');
              setSelectedCategory(null);
            }}
          >
            Timeline View
          </Button>
          <Button color={viewMode === 'category' ? 'primary' : 'outline-primary'} onClick={() => setViewMode('category')}>
            Category View
          </Button>
        </ButtonGroup>
      </div>

      <Row className="justify-content-center">
        <Col md="12">
          {Object.keys(grouped).length === 0 ? (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="healthyMeApp.examinationRecord.home.notFound">No Examination Records found</Translate>
              </div>
            )
          ) : (
            <div>
              {viewMode === 'timeline' && renderTimelineView()}
              {viewMode === 'category' && (
                <div>
                  {renderCategorySelection()}
                  {renderCategoryRecords()}
                </div>
              )}
            </div>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExaminationRecord;
