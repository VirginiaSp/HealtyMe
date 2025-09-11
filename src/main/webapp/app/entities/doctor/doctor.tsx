import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Row, Col, Input } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './doctor.reducer';
import { IDoctor } from 'app/shared/model/doctor.model';
import './doctors-phonebook.scss';

const getSpecialtyColor = (specialty: string) => {
  const specialtyLower = specialty?.toLowerCase() || '';
  if (specialtyLower.includes('καρδιολογ') || specialtyLower.includes('cardio')) return '#e91e63';
  if (specialtyLower.includes('παθολογ') || specialtyLower.includes('pathol')) return '#2196f3';
  if (specialtyLower.includes('χειρουργ') || specialtyLower.includes('surg')) return '#f44336';
  if (specialtyLower.includes('γυναικολογ') || specialtyLower.includes('gynec')) return '#9c27b0';
  if (specialtyLower.includes('παιδιατρ') || specialtyLower.includes('pediatr')) return '#ff9800';
  if (specialtyLower.includes('οφθαλμ') || specialtyLower.includes('ophthal')) return '#009688';
  if (specialtyLower.includes('δερματολογ') || specialtyLower.includes('dermat')) return '#795548';
  if (specialtyLower.includes('ψυχιατρ') || specialtyLower.includes('psychiatr')) return '#673ab7';
  if (specialtyLower.includes('ουρολογ') || specialtyLower.includes('urol')) return '#607d8b';
  if (specialtyLower.includes('ορθοπεδ') || specialtyLower.includes('orthop')) return '#ff5722';
  return '#4a9b8f';
};

const getSpecialtyIcon = (specialty: string) => {
  const specialtyLower = specialty?.toLowerCase() || '';
  if (specialtyLower.includes('καρδιολογ') || specialtyLower.includes('cardio')) return '❤️';
  if (specialtyLower.includes('παθολογ') || specialtyLower.includes('pathol')) return '🩺';
  if (specialtyLower.includes('χειρουργ') || specialtyLower.includes('surg')) return '🔪';
  if (specialtyLower.includes('γυναικολογ') || specialtyLower.includes('gynec')) return '👩‍⚕️';
  if (specialtyLower.includes('παιδιατρ') || specialtyLower.includes('pediatr')) return '👶';
  if (specialtyLower.includes('οφθαλμ') || specialtyLower.includes('ophthal')) return '👁️';
  if (specialtyLower.includes('δερματολογ') || specialtyLower.includes('dermat')) return '🧴';
  if (specialtyLower.includes('ψυχιατρ') || specialtyLower.includes('psychiatr')) return '🧠';
  if (specialtyLower.includes('ουρολογ') || specialtyLower.includes('urol')) return '🫘';
  if (specialtyLower.includes('ορθοπεδ') || specialtyLower.includes('orthop')) return '🦴';
  return '👨‍⚕️';
};

const formatPhoneNumber = (phone: string) => {
  if (!phone) return '';
  // Basic phone formatting - can be enhanced based on locale
  return phone.replace(/(\d{3})(\d{3})(\d{4})/, '($1) $2-$3');
};

export const DoctorsPhonebook = () => {
  const dispatch = useAppDispatch();

  const doctorList = useAppSelector(state => state.doctor.entities);
  const loading = useAppSelector(state => state.doctor.loading);

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSpecialty, setSelectedSpecialty] = useState<string | null>(null);

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch]);

  const filteredDoctors = doctorList
    .filter(doctor => {
      const matchesSearch =
        doctor.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doctor.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doctor.specialty?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doctor.phone?.includes(searchTerm);

      const matchesSpecialty = !selectedSpecialty || doctor.specialty === selectedSpecialty;

      return matchesSearch && matchesSpecialty;
    })
    .sort((a, b) => {
      // Sort alphabetically by last name, then by first name
      const lastNameComparison = (a.lastName || '').localeCompare(b.lastName || '');
      if (lastNameComparison !== 0) {
        return lastNameComparison;
      }
      return (a.firstName || '').localeCompare(b.firstName || '');
    });

  const uniqueSpecialties = [...new Set(doctorList.map(doctor => doctor.specialty).filter(Boolean))] as string[];

  const handleCall = (phone: string) => {
    if (phone) {
      window.location.href = `tel:${phone}`;
    }
  };

  const renderDoctorCard = (doctor: IDoctor) => {
    const specialtyColor = getSpecialtyColor(doctor.specialty || '');
    const specialtyIcon = getSpecialtyIcon(doctor.specialty || '');

    return (
      <div key={doctor.id} className="doctor-card">
        <div className="doctor-card-header">
          <div className="doctor-avatar">
            <span className="avatar-initials">{(doctor.firstName?.[0] || '') + (doctor.lastName?.[0] || '')}</span>
          </div>
          <div className="doctor-info">
            <h3 className="doctor-name">
              {doctor.firstName} {doctor.lastName}
            </h3>
            <span
              className="specialty-badge"
              style={{
                backgroundColor: specialtyColor + '20',
                color: specialtyColor,
                borderColor: specialtyColor,
              }}
            >
              {specialtyIcon} {doctor.specialty}
            </span>
          </div>
        </div>

        <div className="doctor-card-body">
          {doctor.phone && (
            <div className="contact-item">
              <div className="contact-icon">📞</div>
              <div className="contact-details">
                <label>Τηλέφωνο</label>
                <span className="phone-number" onClick={() => handleCall(doctor.phone)}>
                  {formatPhoneNumber(doctor.phone)}
                </span>
              </div>
            </div>
          )}

          {doctor.address && (
            <div className="contact-item">
              <div className="contact-icon">📍</div>
              <div className="contact-details">
                <label>Διεύθυνση</label>
                <span>{doctor.address}</span>
              </div>
            </div>
          )}

          {doctor.notes && (
            <div className="doctor-notes">
              <label>Σημειώσεις</label>
              <p>{doctor.notes}</p>
            </div>
          )}
        </div>

        <div className="doctor-card-actions">
          <Button tag={Link} to={`/doctor/${doctor.id}`} className="action-btn view-btn">
            👁️ Προβολή
          </Button>
          <Button tag={Link} to={`/doctor/${doctor.id}/edit`} className="action-btn edit-btn">
            ✏️ Επεξεργασία
          </Button>
          <Button tag={Link} to={`/doctor/${doctor.id}/delete`} className="action-btn delete-btn">
            🗑️ Διαγραφή
          </Button>
        </div>
      </div>
    );
  };

  return (
    <div className="doctors-phonebook-page">
      <div className="page-header">
        <h2 className="page-title">📞 Κατάλογος Γιατρών</h2>
        <Link to="/doctor/new" className="create-btn">
          ➕ Νέος Γιατρός
        </Link>
      </div>

      {/* Search and Filter Section */}
      <div className="search-filter-section">
        <Row>
          <Col md="8">
            <div className="search-box">
              <Input
                type="text"
                placeholder="Αναζήτηση γιατρού (όνομα, ειδικότητα, τηλέφωνο)..."
                value={searchTerm}
                onChange={e => setSearchTerm(e.target.value)}
                className="search-input"
              />
              <div className="search-icon">🔍</div>
            </div>
          </Col>
          <Col md="4">
            <div className="specialty-filter">
              <select
                value={selectedSpecialty || ''}
                onChange={e => setSelectedSpecialty(e.target.value || null)}
                className="specialty-select"
              >
                <option value="">Όλες οι ειδικότητες</option>
                {uniqueSpecialties.map(specialty => (
                  <option key={specialty} value={specialty}>
                    {getSpecialtyIcon(specialty)} {specialty}
                  </option>
                ))}
              </select>
            </div>
          </Col>
        </Row>
      </div>

      {/* Results Summary */}
      {searchTerm || selectedSpecialty ? (
        <div className="results-summary">
          Βρέθηκαν {filteredDoctors.length} γιατροί
          {searchTerm && ` για "${searchTerm}"`}
          {selectedSpecialty && ` στην ειδικότητα "${selectedSpecialty}"`}
        </div>
      ) : null}

      {/* Doctors Grid */}
      <Row className="doctors-grid">
        {filteredDoctors.length === 0
          ? !loading && (
              <Col xs="12">
                <div className="empty-state">
                  <div className="empty-icon">👨‍⚕️</div>
                  <h3>Δεν βρέθηκαν γιατροί</h3>
                  <p>
                    {searchTerm || selectedSpecialty
                      ? 'Δοκιμάστε να αλλάξετε τα κριτήρια αναζήτησης'
                      : 'Προσθέστε τον πρώτο γιατρό στον κατάλογό σας'}
                  </p>
                  {!searchTerm && !selectedSpecialty && (
                    <Link to="/doctor/new" className="create-btn">
                      ➕ Προσθήκη Γιατρού
                    </Link>
                  )}
                </div>
              </Col>
            )
          : filteredDoctors.map(doctor => (
              <Col key={doctor.id} lg="6" xl="4" className="doctor-col">
                {renderDoctorCard(doctor)}
              </Col>
            ))}
      </Row>
    </div>
  );
};

export default DoctorsPhonebook;
