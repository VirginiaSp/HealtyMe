import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Col, Row, ButtonGroup } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './examination-record.reducer';
import { IExaminationRecord } from 'app/shared/model/examination-record.model';
import './examination-record-list.scss';

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
  const title = anyRec.title ?? filename ?? 'Examination Record';
  return { categoryName, date, fileCt, fileData, filename, notes, title };
};

const formatDateOnly = (date: string | Date) => {
  if (!date) return null;
  const d = new Date(date);
  return d.toLocaleDateString('el-GR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

const getCategoryIcon = (categoryName: string) => {
  if (categoryName.includes('Αιματολογικές') || categoryName.includes('αίμα')) return '🩸';
  if (categoryName.includes('Καρδιολογικές') || categoryName.includes('καρδι')) return '❤️';
  if (categoryName.includes('Απεικονιστικές') || categoryName.includes('ακτιν')) return '📷';
  if (categoryName.includes('Γενικές')) return '🏥';
  return '📋';
};

const getCategoryColor = (categoryName: string) => {
  if (categoryName.includes('Αιματολογικές')) return '#e74c3c';
  if (categoryName.includes('Καρδιολογικές')) return '#e91e63';
  if (categoryName.includes('Απεικονιστικές')) return '#9c27b0';
  if (categoryName.includes('Γενικές')) return '#2196f3';
  return '#4a9b8f';
};

export const ExaminationRecord = () => {
  const dispatch = useAppDispatch();

  const examinationRecordList = useAppSelector(state => state.examinationRecord.entities);
  const loading = useAppSelector(state => state.examinationRecord.loading);

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
      const link = document.createElement('a');
      link.href = `data:${fileCt ?? 'application/octet-stream'};base64,${fileData}`;
      link.download = filename;
      link.click();
    }
  };

  const renderExaminationCard = (rec: IExaminationRecord) => {
    const { categoryName, date, fileCt, fileData, filename, notes, title } = getRec(rec);
    const categoryColor = getCategoryColor(categoryName);
    const categoryIcon = getCategoryIcon(categoryName);

    return (
      <div key={rec.id} className="examination-card">
        <div className="examination-card-header">
          <div className="category-info">
            <span
              className="category-badge"
              style={{ backgroundColor: categoryColor + '20', color: categoryColor, borderColor: categoryColor }}
            >
              {categoryIcon} {categoryName}
            </span>
            <span className="examination-date">{formatDateOnly(date)}</span>
          </div>
        </div>

        <div className="examination-card-body">
          <h3 className="examination-title">{title}</h3>

          {fileData && (
            <div className="file-attachment" onClick={() => handleFileClick(fileCt, fileData, filename)}>
              {isImage(fileCt) ? (
                <div className="image-preview">
                  <img src={`data:${fileCt};base64,${fileData}`} alt={filename} className="examination-image" />
                  <div className="image-overlay">
                    <span>🔍 Κάντε κλικ για μεγέθυνση</span>
                  </div>
                </div>
              ) : (
                <div className="file-link">
                  📎 <span>{filename}</span>
                  <small>Κάντε κλικ για λήψη</small>
                </div>
              )}
            </div>
          )}

          {notes && (
            <div className="examination-notes">
              <strong>Σημειώσεις:</strong>
              <p>{notes}</p>
            </div>
          )}
        </div>

        <div className="examination-card-actions">
          <Button tag={Link} to={`/examination-record/${rec.id}`} color="info" size="sm" className="action-btn">
            👁️ Προβολή
          </Button>
          <Button tag={Link} to={`/examination-record/${rec.id}/edit`} color="primary" size="sm" className="action-btn">
            ✏️ Επεξεργασία
          </Button>
          <Button tag={Link} to={`/examination-record/${rec.id}/delete`} color="danger" size="sm" className="action-btn">
            🗑️ Διαγραφή
          </Button>
        </div>
      </div>
    );
  };

  const renderTimelineView = () => (
    <div className="examination-cards-container">{sortedRecords.map(rec => renderExaminationCard(rec))}</div>
  );

  const renderCategorySelection = () => (
    <div className="category-filter-section">
      <h4>Επιλογή Κατηγορίας:</h4>
      <div className="category-filter-buttons">
        {Object.keys(grouped).map(categoryName => {
          const categoryColor = getCategoryColor(categoryName);
          const categoryIcon = getCategoryIcon(categoryName);
          return (
            <Button
              key={categoryName}
              className={`category-filter-btn ${selectedCategory === categoryName ? 'active' : ''}`}
              onClick={() => setSelectedCategory(selectedCategory === categoryName ? null : categoryName)}
              style={{
                backgroundColor: selectedCategory === categoryName ? categoryColor : 'transparent',
                borderColor: categoryColor,
                color: selectedCategory === categoryName ? 'white' : categoryColor,
              }}
            >
              {categoryIcon} {categoryName} ({grouped[categoryName].length})
            </Button>
          );
        })}
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
      <div className="category-records-section">
        <h4 className="category-title">
          {getCategoryIcon(selectedCategory)} {selectedCategory} - Αποτελέσματα
        </h4>
        <div className="examination-cards-container">{categoryRecords.map(rec => renderExaminationCard(rec))}</div>
      </div>
    );
  };

  return (
    <div className="examination-record-page">
      <div className="page-header">
        <h2 className="page-title">Ιστορικό Εξετάσεων</h2>
        <Link to="/examination-record/new" className="create-btn">
          ➕ Νέα Εξέταση
        </Link>
      </div>

      <div className="view-controls">
        <ButtonGroup>
          <Button
            className={`view-toggle ${viewMode === 'timeline' ? 'active' : ''}`}
            onClick={() => {
              setViewMode('timeline');
              setSelectedCategory(null);
            }}
          >
            📅 Χρονολογική Προβολή
          </Button>
          <Button className={`view-toggle ${viewMode === 'category' ? 'active' : ''}`} onClick={() => setViewMode('category')}>
            📂 Προβολή ανά Κατηγορία
          </Button>
        </ButtonGroup>
      </div>

      <Row className="justify-content-center">
        <Col md="12">
          {Object.keys(grouped).length === 0 ? (
            !loading && (
              <div className="empty-state">
                <div className="empty-icon">📋</div>
                <h3>Δεν βρέθηκαν εξετάσεις</h3>
                <p>Προσθέστε την πρώτη σας εξέταση για να ξεκινήσετε</p>
                <Link to="/examination-record/new" className="create-btn">
                  ➕ Νέα Εξέταση
                </Link>
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
