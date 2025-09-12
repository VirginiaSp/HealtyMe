import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Row, Col, Card, CardBody, Badge, Button } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './doctor.reducer';
import axios from 'axios';
import './doctor-detail.scss';

interface IVisit {
  id?: number;
  date: string;
  type: string;
  notes: string;
  doctorId?: number;
}

export const DoctorDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  // Get doctor data from Redux store
  const doctor = useAppSelector(state => state.doctor.entity);
  const loading = useAppSelector(state => state.doctor.loading);

  // All state variables
  const [visitHistory, setVisitHistory] = useState<IVisit[]>([]);
  const [loadingVisits, setLoadingVisits] = useState(false);
  const [showAddVisit, setShowAddVisit] = useState(false);
  const [editingVisit, setEditingVisit] = useState<IVisit | null>(null);
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

  // Function to format date for display
  const formatDateForDisplay = (dateString: string) => {
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('el-GR');
    } catch (e) {
      return dateString;
    }
  };

  // Fetch doctor data when component mounts or ID changes
  useEffect(() => {
    if (id) {
      console.warn('Fetching doctor with ID:', id);
      dispatch(getEntity(id));
    }
  }, [id, dispatch]);

  // Fetch visit history from backend API
  useEffect(() => {
    const fetchVisits = async () => {
      if (id) {
        setLoadingVisits(true);
        try {
          console.warn('Fetching visits for doctor ID:', id);
          const response = await axios.get(`/api/doctors/${id}/visits`);
          setVisitHistory(response.data);
          console.warn('Loaded', response.data.length, 'visits for doctor', id);
        } catch (error) {
          console.error('Error fetching visits:', error);
          setVisitHistory([]);
        } finally {
          setLoadingVisits(false);
        }
      }
    };

    fetchVisits();
  }, [id]);

  // Function to save visit to backend
  const saveVisitToBackend = async (visitData: Omit<IVisit, 'id'>) => {
    if (!id) return null;

    try {
      console.warn('Saving visit to backend for doctor', id, ':', visitData);
      const response = await axios.post(`/api/doctors/${id}/visits`, visitData);
      console.warn('Visit saved successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error saving visit:', error);
      throw error;
    }
  };

  // Function to update visit in backend
  const updateVisitInBackend = async (visitId: number, visitData: Omit<IVisit, 'id'>) => {
    if (!id) return null;

    try {
      console.warn('Updating visit', visitId, 'for doctor', id, ':', visitData);
      const response = await axios.put(`/api/doctors/${id}/visits/${visitId}`, visitData);
      console.warn('Visit updated successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error updating visit:', error);
      throw error;
    }
  };

  // Function to delete visit from backend
  const deleteVisitFromBackend = async (visitId: number) => {
    if (!id) return;

    try {
      console.warn('Deleting visit', visitId, 'for doctor', id);
      await axios.delete(`/api/doctors/${id}/visits/${visitId}`);
      console.warn('Visit deleted successfully');
    } catch (error) {
      console.error('Error deleting visit:', error);
      throw error;
    }
  };

  const handleAddVisit = async () => {
    // Validate form
    const errors = {
      date: !newVisit.date,
      type: !newVisit.type.trim(),
      notes: !newVisit.notes.trim(),
    };

    setFormErrors(errors);

    // If no errors, save the visit to backend
    if (!errors.date && !errors.type && !errors.notes) {
      try {
        const visitData = {
          date: newVisit.date,
          type: newVisit.type,
          notes: newVisit.notes,
        };

        // Save to backend
        const savedVisit = await saveVisitToBackend(visitData);

        if (savedVisit) {
          // Add to local state for immediate UI update
          const newHistory = [savedVisit, ...visitHistory];
          setVisitHistory(newHistory);

          // Clear form
          setNewVisit({ date: '', type: '', notes: '' });
          setFormErrors({ date: false, type: false, notes: false });
          setShowAddVisit(false);

          console.warn('Visit added successfully! Total visits now:', newHistory.length);
        }
      } catch (error) {
        console.error('Failed to save visit:', error);
        alert('Failed to save visit. Please try again.');
      }
    }
  };

  const handleEditVisit = (visit: IVisit) => {
    setEditingVisit(visit);
    setNewVisit({
      date: visit.date,
      type: visit.type,
      notes: visit.notes,
    });
    setShowAddVisit(true);
  };

  const handleUpdateVisit = async () => {
    if (!editingVisit || !editingVisit.id) return;

    // Validate form
    const errors = {
      date: !newVisit.date,
      type: !newVisit.type.trim(),
      notes: !newVisit.notes.trim(),
    };

    setFormErrors(errors);

    // If no errors, update the visit in backend
    if (!errors.date && !errors.type && !errors.notes) {
      try {
        const visitData = {
          date: newVisit.date,
          type: newVisit.type,
          notes: newVisit.notes,
        };

        // Update in backend
        const updatedVisit = await updateVisitInBackend(editingVisit.id, visitData);

        if (updatedVisit) {
          // Update local state
          const newHistory = visitHistory.map(visit => (visit.id === editingVisit.id ? updatedVisit : visit));
          setVisitHistory(newHistory);

          // Clear form
          setNewVisit({ date: '', type: '', notes: '' });
          setFormErrors({ date: false, type: false, notes: false });
          setShowAddVisit(false);
          setEditingVisit(null);

          console.warn('Visit updated successfully!');
        }
      } catch (error) {
        console.error('Failed to update visit:', error);
        alert('Failed to update visit. Please try again.');
      }
    }
  };

  const handleDeleteVisit = async (visit: IVisit) => {
    if (!visit.id) return;

    if (window.confirm('Είστε σίγουροι ότι θέλετε να διαγράψετε αυτήν την επίσκεψη;')) {
      try {
        await deleteVisitFromBackend(visit.id);

        // Remove from local state
        const newHistory = visitHistory.filter(v => v.id !== visit.id);
        setVisitHistory(newHistory);

        console.warn('Visit deleted successfully!');
      } catch (error) {
        console.error('Failed to delete visit:', error);
        alert('Failed to delete visit. Please try again.');
      }
    }
  };

  const handleCancelAdd = () => {
    setNewVisit({ date: '', type: '', notes: '' });
    setFormErrors({ date: false, type: false, notes: false });
    setShowAddVisit(false);
    setEditingVisit(null);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!doctor) {
    return <div>Doctor not found</div>;
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

                  {doctor.phone && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                        </svg>
                        Τηλέφωνο
                      </label>
                      <div className="multi-values">
                        <div className="value-item">
                          <a href={`tel:${doctor.phone}`} className="phone-link">
                            {doctor.phone}
                          </a>
                        </div>
                      </div>
                    </div>
                  )}

                  {doctor.address && (
                    <div className="detail-item">
                      <label className="detail-label">
                        <svg className="detail-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                          <circle cx="12" cy="10" r="3"></circle>
                        </svg>
                        Διεύθυνση
                      </label>
                      <div className="multi-values">
                        <div className="value-item">{doctor.address}</div>
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

                {/* Add/Edit Visit Form */}
                {showAddVisit && (
                  <div className="add-visit-form">
                    <h6 style={{ marginBottom: '15px', color: '#374151' }}>{editingVisit ? 'Επεξεργασία Επίσκεψης' : 'Νέα Επίσκεψη'}</h6>
                    <Row>
                      <Col md="6">
                        <label className="form-label">
                          Ημερομηνία <span className="required">*</span>
                        </label>
                        <input
                          type="date"
                          className={`form-control ${formErrors.date ? 'is-invalid' : ''}`}
                          value={newVisit.date}
                          onChange={e => {
                            setNewVisit({ ...newVisit, date: e.target.value });
                            if (formErrors.date && e.target.value) {
                              setFormErrors({ ...formErrors, date: false });
                            }
                          }}
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
                          onChange={e => {
                            setNewVisit({ ...newVisit, type: e.target.value });
                            if (formErrors.type && e.target.value.trim()) {
                              setFormErrors({ ...formErrors, type: false });
                            }
                          }}
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
                      {formErrors.notes && <div className="error-message">Παρακαλώ συμπληρώστε τις σημειώσεις</div>}
                    </div>
                    <div className="form-actions mt-3">
                      <Button type="button" color="success" size="sm" onClick={editingVisit ? handleUpdateVisit : handleAddVisit}>
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <polyline points="20,6 9,17 4,12"></polyline>
                        </svg>
                        {editingVisit ? 'Ενημέρωση' : 'Αποθήκευση'}
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
                  {loadingVisits ? (
                    <div style={{ textAlign: 'center', padding: '20px', color: '#6b7280' }}>Loading visits...</div>
                  ) : visitHistory.length === 0 ? (
                    <div style={{ textAlign: 'center', padding: '20px', color: '#6b7280' }}>No visits recorded yet.</div>
                  ) : (
                    visitHistory.map(visit => (
                      <div key={visit.id} className="visit-item">
                        <div className="visit-header">
                          <span className="visit-date">{formatDateForDisplay(visit.date)}</span>
                          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <Badge color="secondary" className="visit-type">
                              {visit.type}
                            </Badge>
                            <Button
                              size="sm"
                              color="link"
                              onClick={() => handleEditVisit(visit)}
                              style={{ padding: '2px 6px', fontSize: '12px' }}
                              title="Επεξεργασία"
                            >
                              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                <path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                              </svg>
                            </Button>
                            <Button
                              size="sm"
                              color="link"
                              onClick={() => handleDeleteVisit(visit)}
                              style={{ padding: '2px 6px', fontSize: '12px', color: '#dc3545' }}
                              title="Διαγραφή"
                            >
                              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                <polyline points="3,6 5,6 21,6"></polyline>
                                <path d="M19,6v14a2,2 0 0,1 -2,2H7a2,2 0 0,1 -2,-2V6m3,0V4a2,2 0 0,1 2,-2h4a2,2 0 0,1 2,2v2"></path>
                              </svg>
                            </Button>
                          </div>
                        </div>
                        <p className="visit-notes">{visit.notes}</p>
                      </div>
                    ))
                  )}
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
