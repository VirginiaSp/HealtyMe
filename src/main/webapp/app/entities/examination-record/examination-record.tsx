import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Col, Nav, NavItem, NavLink, Row, Table } from 'reactstrap';
import classNames from 'classnames';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getExaminationRecords } from './examination-record.reducer';
import { IExaminationRecord } from 'app/shared/model/examination-record.model';

const ExaminationRecord = () => {
  const dispatch = useAppDispatch();

  // Φόρτωσε τα records
  useEffect(() => {
    dispatch(
      getExaminationRecords({
        page: 0,
        size: 2000,
        sort: 'date,desc',
      }),
    );
  }, [dispatch]);

  // Λίστα από Redux
  const examinationRecordList: IExaminationRecord[] = useAppSelector(state => state.examinationRecord.entities);
  const loading = useAppSelector(state => state.examinationRecord.loading);

  // helper: είναι image;
  const isImage = (ct?: string) => !!ct && ct.startsWith('image/');

  // Group by category
  const byCategory: Record<string, IExaminationRecord[]> = useMemo(() => {
    const map: Record<string, IExaminationRecord[]> = {};
    (examinationRecordList ?? []).forEach(rec => {
      const catName = rec.category?.name ?? 'Uncategorized';
      if (!map[catName]) map[catName] = [];
      map[catName].push(rec);
    });
    return map;
  }, [examinationRecordList]);

  // Tabs κατηγοριών
  const categories: string[] = useMemo(() => ['ALL', ...Object.keys(byCategory).sort()], [byCategory]);

  // Ενεργή κατηγορία (default ALL)
  const [activeCategory, setActiveCategory] = useState<string>('ALL');

  // Ποια λίστα φαίνεται
  const visibleRecords: IExaminationRecord[] = useMemo(() => {
    if (activeCategory === 'ALL') return examinationRecordList ?? [];
    return byCategory[activeCategory] ?? [];
  }, [activeCategory, examinationRecordList, byCategory]);

  // Ανοιχτή γραμμή για εμφάνιση σημειώσεων
  const [openRowId, setOpenRowId] = useState<number | undefined>(undefined);

  return (
    <div>
      <Row className="mb-3">
        <Col>
          <h2>Examination Records</h2>
        </Col>
        <Col className="text-end">
          <Link to="/examination-record/new" className="btn btn-primary">
            <span className="d-none d-md-inline">Add new</span>
          </Link>
        </Col>
      </Row>

      {/* Tabs κατηγοριών */}
      <Nav pills className="mb-3 flex-wrap">
        <NavItem key="ALL">
          <NavLink className={classNames({ active: activeCategory === 'ALL' })} onClick={() => setActiveCategory('ALL')} role="button">
            ALL <span className="badge bg-secondary">{examinationRecordList?.length ?? 0}</span>
          </NavLink>
        </NavItem>

        {categories
          .filter(c => c !== 'ALL')
          .map((cat: string) => (
            <NavItem key={cat}>
              <NavLink className={classNames({ active: activeCategory === cat })} onClick={() => setActiveCategory(cat)} role="button">
                {cat} <span className="badge bg-secondary">{byCategory[cat]?.length ?? 0}</span>
              </NavLink>
            </NavItem>
          ))}
      </Nav>

      {loading ? (
        <div>Loading...</div>
      ) : (
        <div className="table-responsive">
          <Table responsive hover>
            <thead>
              <tr>
                {/* Κατηγορία πρώτα */}
                <th>Category</th>
                <th>Date</th>
                {/* Τίτλος δεν τον χρειάζεσαι → τον αφαιρούμε */}
                {/* <th>Title</th> */}
                {/* owner / stored filename / bytes ΚΡΥΦΑ */}
                {/* <th>Owner</th> */}
                {/* <th>Stored Filename</th> */}
                {/* <th>Bytes</th> */}
                <th>File</th>
                <th className="text-end">Actions</th>
              </tr>
            </thead>
            <tbody>
              {visibleRecords.map(rec => (
                <React.Fragment key={rec.id}>
                  <tr onClick={() => setOpenRowId(openRowId === rec.id ? undefined : rec.id)} style={{ cursor: 'pointer' }}>
                    {/* CATEGORY */}
                    <td>{rec.category?.name ?? 'Uncategorized'}</td>

                    {/* DATE */}

                    {/* FILE (thumbnail ή link) */}
                    {/* FILE (thumbnail ή link) */}
                    <td>
                      {isImage(rec.fileContentType) && rec.file ? (
                        <img
                          src={`data:${rec.fileContentType};base64,${rec.file}`}
                          alt={rec.originalFilename ?? 'exam'}
                          style={{ maxWidth: 120, maxHeight: 80, objectFit: 'cover', borderRadius: 6 }}
                          onClick={e => e.stopPropagation()}
                        />
                      ) : rec.file ? (
                        <a
                          onClick={e => e.stopPropagation()}
                          href={`data:${rec.fileContentType};base64,${rec.file}`}
                          download={rec.originalFilename ?? 'exam'}
                        >
                          Λήψη αρχείου
                        </a>
                      ) : (
                        <span className="text-muted">—</span>
                      )}
                    </td>

                    {/* ACTIONS */}
                    <td className="text-end">
                      <div className="btn-group">
                        <Button
                          tag={Link as any}
                          to={`/examination-record/${rec.id}/edit`}
                          color="primary"
                          size="sm"
                          onClick={e => e.stopPropagation()}
                        >
                          Edit
                        </Button>
                        <Button
                          tag={Link as any}
                          to={`/examination-record/${rec.id}/delete`}
                          color="danger"
                          size="sm"
                          onClick={e => e.stopPropagation()}
                        >
                          Delete
                        </Button>
                      </div>
                    </td>
                  </tr>

                  {/* Notes (εμφάνιση μόνο όταν ανοίγει η γραμμή) */}
                  {openRowId === rec.id && rec.notes && (
                    <tr>
                      <td colSpan={4}>
                        <div className="p-3 bg-light rounded">
                          <strong>Notes</strong>
                          <div className="mt-2" style={{ whiteSpace: 'pre-wrap' }}>
                            {rec.notes}
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))}
            </tbody>
          </Table>

          {!visibleRecords.length && <div className="alert alert-info mt-3">No examination records in this category.</div>}
        </div>
      )}
    </div>
  );
};

export default ExaminationRecord;
