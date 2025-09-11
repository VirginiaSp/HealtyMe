import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Row, Col, Card, CardBody, Badge, Button } from 'reactstrap';
import './doctor-detail.scss';

// You can replace this interface with your actual doctor interface
interface IDoctor {
  id?: number;
  firstName: string;
  lastName: string;
  specialty?: string;
  phones?: string[];
  addresses?: string[];
  notes?: string;
}

interface IVisit {
  id: number;
  date: string;
  type: string;
  notes: string;
}

export const DoctorDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [doctor, setDoctor] = useState<IDoctor | null>(null);
  const [visitHistory, setVisitHistory] = useState<IVisit[]>([
    {
      id: 1,
      date: '2025-09-10',
      type: 'Τακτικός έλεγχος',
      notes: 'Γενική εξέταση - όλα καλά',
    },
    {
      id: 2,
      date: '2025-08-15',
      type: 'Επανεξέταση',
      notes: 'Έλεγχος αποτελεσμάτων εξετάσεων',
    },
    {
      id: 3,
      date: '2025-07-20',
      type: 'Πρώτη επίσκεψη',
      notes: 'Αρχική διάγνωση και σχέδιο θεραπείας',
    },
  ]);
  const [showAddVisit, setShowAddVisit] = useState(false);
  const [newVisit, setNewVisit] = useState({
    date: '',
    type: '',
    notes: '',
  });
  const [formErrors, setFormErrors] = useState({
    date: false,
    type: false,
    notes: false,
  });

  // Mock data - replace with actual API call
  useEffect(() => {
    // Simulate API call
    const mockDoctor: IDoctor = {
      id: parseInt(id || '1', 10),
      firstName: 'Bennett',
      lastName: 'Koepp',
      specialty: 'department always',
      phones: ['460.486.6703'],
      addresses: ['why early truly'],
      notes: 'excitedly',
    };
    setDoctor(mockDoctor);
  }, [id]);

  const handleAddVisit = () => {
    if (newVisit.date && newVisit.type && newVisit.notes) {
      const visit: IVisit = {
        id: visitHistory.length + 1,
        date: newVisit.date,
        type: newVisit.type,
        notes: newVisit.notes,
      };
      setVisitHistory([visit, ...visitHistory]); // Add to beginning for newest first
      setNewVisit({ date: '', type: '', notes: '' });
      setShowAddVisit(false);
    }
  };

  const handleCancelAdd = () => {
    setNewVisit({ date: '', type: '', notes: '' });
    setShowAddVisit(false);
  };

  if (!doctor) {
    return <div>Loading...</div>;
  }

  return (
    <div className="doctor-detail-page">
      {/* Header */}
      <div className="page-header">
        <div className="header-content">
          <div className="doctor-header">
            <div className="doctor-avatar">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
            </div>
            <div className="doctor-info">
              <h2 className="doctor-name">
                Δρ. {doctor.firstName} {doctor.lastName}
              </h2>
              {doctor.specialty && (
                <Badge color="info" className="specialty-badge">
                  {doctor.specialty}
                </Badge>
              )}
            </div>
          </div>
          <div className="header-actions">
            <Button color="secondary" onClick={() => navigate('/doctor')} className="action-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="m15 18-6-6 6-6"></path>
              </svg>
              Πίσω
            </Button>
            <Button color="primary" onClick={() => navigate(`/doctor/${id}/edit`)} className="action-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                <path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
              </svg>
              Επεξεργασία
            </Button>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="page-content">
        <Row>
          {/* Left Column - Doctor Details */}
          <Col lg="6">
            <Card className="detail-card">
              <CardBody>
                <h5 className="section-title">
                  <svg className="section-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
                    <circle cx="9" cy="7" r="4"></circle>
                    <path d="m22 21-3-3"></path>
                  </svg>
                  Στοιχεία Γιατρού
                </h5>

                <div className="detail-section">
                  <div className="detail-item">
                    <label className="detail-label">
                      <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                        <circle cx="12" cy="7" r="4"></circle>
                      </svg>
                      Πλήρες Όνομα
                    </label>
                    <p className="detail-value">
                      {doctor.firstName} {doctor.lastName}
                    </p>
                  </div>

                  {doctor.specialty && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
                        </svg>
                        Ειδικότητα
                      </label>
                      <p className="detail-value">{doctor.specialty}</p>
                    </div>
                  )}

                  {doctor.phones && doctor.phones.length > 0 && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                        </svg>
                        Τηλέφωνα
                      </label>
                      <div className="multi-values">
                        {doctor.phones
                          .filter(phone => phone.trim())
                          .map((phone, index) => (
                            <div key={index} className="value-item">
                              <a href={`tel:${phone}`} className="phone-link">
                                {phone}
                              </a>
                            </div>
                          ))}
                      </div>
                    </div>
                  )}

                  {doctor.addresses && doctor.addresses.length > 0 && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                          <circle cx="12" cy="10" r="3"></circle>
                        </svg>
                        Διευθύνσεις
                      </label>
                      <div className="multi-values">
                        {doctor.addresses
                          .filter(address => address.trim())
                          .map((address, index) => (
                            <div key={index} className="value-item">
                              {address}
                            </div>
                          ))}
                      </div>
                    </div>
                  )}

                  {doctor.notes && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                          <polyline points="14,2 14,8 20,8"></polyline>
                          <line x1="16" y1="13" x2="8" y2="13"></line>
                          <line x1="16" y1="17" x2="8" y2="17"></line>
                        </svg>
                        Σημειώσεις
                      </label>
                      <p className="detail-value notes-value">{doctor.notes}</p>
                    </div>
                  )}
                </div>
              </CardBody>
            </Card>
          </Col>

          {/* Right Column - Medical Information */}
          <Col lg="6">
            {/* Visit History */}
            <Card className="detail-card">
              <CardBody>
                <h5 className="section-title">
                  <svg className="section-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M8 2v4"></path>
                    <path d="M16 2v4"></path>
                    <rect width="18" height="18" x="3" y="4" rx="2"></rect>
                    <path d="M3 10h18"></path>
                  </svg>
                  Ιστορικό Επισκέψεων & Διαγνώσεων
                </h5>

                {/* Add Visit Form */}
                {showAddVisit && (
                  <div className="add-visit-form">
                    <Row>
                      <Col md="6">
                        <label className="form-label">
                          Ημερομηνία <span className="required">*</span>
                        </label>
                        <input
                          type="date"
                          className={`form-control ${formErrors.date ? 'is-invalid' : ''}`}
                          value={newVisit.date}
                          onChange={e => setNewVisit({ ...newVisit, date: e.target.value })}
                        />
                        {formErrors.date && <div className="error-message">Παρακαλώ συμπληρώστε την ημερομηνία</div>}
                      </Col>
                      <Col md="6">
                        <label className="form-label">
                          Τύπος Επίσκεψης <span className="required">*</span>
                        </label>
                        <input
                          type="text"
                          className={`form-control ${formErrors.type ? 'is-invalid' : ''}`}
                          placeholder="π.χ. Τακτικός έλεγχος"
                          value={newVisit.type}
                          onChange={e => setNewVisit({ ...newVisit, type: e.target.value })}
                        />
                        {formErrors.type && <div className="error-message">Παρακαλώ συμπληρώστε τον τύπο επίσκεψης</div>}
                      </Col>
                    </Row>
                    <div className="mt-3">
                      <label className="form-label">
                        Σημειώσεις / Διάγνωση <span className="required">*</span>
                      </label>
                      <textarea
                        className={`form-control ${formErrors.notes ? 'is-invalid' : ''}`}
                        rows={3}
                        placeholder="Περιγραφή επίσκεψης, διάγνωση, συμπτώματα..."
                        value={newVisit.notes}
                        onChange={e => {
                          setNewVisit({ ...newVisit, notes: e.target.value });
                          if (formErrors.notes && e.target.value.trim()) {
                            setFormErrors({ ...formErrors, notes: false });
                          }
                        }}
                      />
                      {formErrors.notes && (
                        <div
                          className="error-message"
                          style={{ display: 'block', color: 'red', backgroundColor: 'yellow', padding: '10px', fontSize: '16px' }}
                        >
                          ⚠ Παρακαλώ συμπληρώστε τις σημειώσεις
                        </div>
                      )}
                    </div>
                    <div className="form-actions mt-3">
                      <Button type="button" color="success" size="sm" onClick={handleAddVisit}>
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <polyline points="20,6 9,17 4,12"></polyline>
                        </svg>
                        Αποθήκευση
                      </Button>
                      <Button type="button" color="secondary" size="sm" onClick={handleCancelAdd}>
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <line x1="18" y1="6" x2="6" y2="18"></line>
                          <line x1="6" y1="6" x2="18" y2="18"></line>
                        </svg>
                        Ακύρωση
                      </Button>
                    </div>
                  </div>
                )}

                <div className="visit-history">
                  {visitHistory.map(visit => (
                    <div key={visit.id} className="visit-item">
                      <div className="visit-header">
                        <span className="visit-date">{visit.date}</span>
                        <Badge color="secondary" className="visit-type">
                          {visit.type}
                        </Badge>
                      </div>
                      <p className="visit-notes">{visit.notes}</p>
                    </div>
                  ))}
                </div>

                {!showAddVisit && (
                  <Button color="link" className="add-visit-btn" onClick={() => setShowAddVisit(true)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                      <line x1="12" y1="5" x2="12" y2="19"></line>
                      <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                    Προσθήκη νέας επίσκεψης
                  </Button>
                )}
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DoctorDetail;
