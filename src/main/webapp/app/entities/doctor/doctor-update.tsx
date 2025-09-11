import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import './doctor-update.scss';

interface StateProps {
  loading?: boolean;
}

interface DispatchProps {
  updateEntity?: (entity: any) => void;
}

export interface IDoctorUpdateProps extends StateProps, DispatchProps {}

export const DoctorUpdate = (props: IDoctorUpdateProps) => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [phones, setPhones] = useState<string[]>(['']);
  const [addresses, setAddresses] = useState<string[]>(['']);

  const addPhoneField = () => {
    setPhones([...phones, '']);
  };

  const addAddressField = () => {
    setAddresses([...addresses, '']);
  };

  const removePhoneField = (index: number) => {
    if (phones.length > 1) {
      setPhones(phones.filter((_, i) => i !== index));
    }
  };

  const removeAddressField = (index: number) => {
    if (addresses.length > 1) {
      setAddresses(addresses.filter((_, i) => i !== index));
    }
  };

  const updatePhoneField = (index: number, value: string) => {
    const newPhones = [...phones];
    newPhones[index] = value;
    setPhones(newPhones);
  };

  const updateAddressField = (index: number, value: string) => {
    const newAddresses = [...addresses];
    newAddresses[index] = value;
    setAddresses(newAddresses);
  };

  const saveEntity = (values: any) => {
    // Combine multiple phones and addresses
    const entity = {
      ...values,
      phones: phones.filter(phone => phone.trim() !== ''),
      addresses: addresses.filter(address => address.trim() !== ''),
    };

    // Your save logic here - replace this with actual save logic
    if (props.updateEntity) {
      props.updateEntity(entity);
    }

    // Navigate back to doctor list after saving
    navigate('/doctor');
  };

  const defaultValues = () => ({
    firstName: '',
    lastName: '',
    specialty: '',
    notes: '',
  });

  return (
    <div className="modern-doctor-form">
      <div className="form-header">
        <div className="header-content">
          <h1 className="header-title">
            <div className="header-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                <circle cx="9" cy="7" r="4"></circle>
                <path d="m22 21-3-3"></path>
              </svg>
            </div>
            Δημιουργία ή Επεξεργασία Γιατρού
          </h1>
          <p className="header-subtitle">Συμπληρώστε τα στοιχεία του γιατρού</p>
        </div>
      </div>

      <div className="form-content">
        <div className="required-note">
          <strong>Σημείωση:</strong> Τα πεδία με αστερίσκο (*) είναι υποχρεωτικά
        </div>

        <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
          <Row>
            <Col md="6">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  Όνομα <span className="required-indicator">*</span>
                </label>
                <ValidatedField
                  className="custom-input"
                  name="firstName"
                  placeholder="π.χ. Γιάννης"
                  validate={{
                    required: { value: true, message: 'Το πεδίο είναι υποχρεωτικό.' },
                  }}
                />
              </div>
            </Col>

            <Col md="6">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  Επίθετο <span className="required-indicator">*</span>
                </label>
                <ValidatedField
                  className="custom-input"
                  name="lastName"
                  placeholder="π.χ. Παπαδόπουλος"
                  validate={{
                    required: { value: true, message: 'Το πεδίο είναι υποχρεωτικό.' },
                  }}
                />
              </div>
            </Col>

            <Col md="6">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
                  </svg>
                  Ειδικότητα
                </label>
                <ValidatedField className="custom-input" name="specialty" placeholder="π.χ. Καρδιολόγος" />
              </div>
            </Col>

            <Col md="6">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                  </svg>
                  Τηλέφωνα
                </label>
                <div className="multi-field-group">
                  {phones.map((phone, index) => (
                    <div key={index} className={index === 0 ? '' : 'field-with-remove'}>
                      <input
                        type="tel"
                        className="custom-input"
                        value={phone}
                        onChange={e => updatePhoneField(index, e.target.value)}
                        placeholder="π.χ. 210-123-4567"
                      />
                      {index > 0 && (
                        <button type="button" className="remove-btn" onClick={() => removePhoneField(index)}>
                          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                            <line x1="18" y1="6" x2="6" y2="18"></line>
                            <line x1="6" y1="6" x2="18" y2="18"></line>
                          </svg>
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                <button type="button" className="add-more-btn" onClick={addPhoneField}>
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <line x1="12" y1="5" x2="12" y2="19"></line>
                    <line x1="5" y1="12" x2="19" y2="12"></line>
                  </svg>
                  Προσθήκη τηλεφώνου
                </button>
              </div>
            </Col>

            <Col md="6">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                    <circle cx="12" cy="10" r="3"></circle>
                  </svg>
                  Διευθύνσεις
                </label>
                <div className="multi-field-group">
                  {addresses.map((address, index) => (
                    <div key={index} className={index === 0 ? '' : 'field-with-remove'}>
                      <input
                        type="text"
                        className="custom-input"
                        value={address}
                        onChange={e => updateAddressField(index, e.target.value)}
                        placeholder="π.χ. Λεωφ. Αθηνών 123, Αθήνα"
                      />
                      {index > 0 && (
                        <button type="button" className="remove-btn" onClick={() => removeAddressField(index)}>
                          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                            <line x1="18" y1="6" x2="6" y2="18"></line>
                            <line x1="6" y1="6" x2="18" y2="18"></line>
                          </svg>
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                <button type="button" className="add-more-btn" onClick={addAddressField}>
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <line x1="12" y1="5" x2="12" y2="19"></line>
                    <line x1="5" y1="12" x2="19" y2="12"></line>
                  </svg>
                  Προσθήκη διεύθυνσης
                </button>
              </div>
            </Col>

            <Col md="12">
              <div className="custom-field">
                <label className="field-label">
                  <svg className="field-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                    <polyline points="14,2 14,8 20,8"></polyline>
                    <line x1="16" y1="13" x2="8" y2="13"></line>
                    <line x1="16" y1="17" x2="8" y2="17"></line>
                    <polyline points="10,9 9,9 8,9"></polyline>
                  </svg>
                  Σημειώσεις
                </label>
                <ValidatedField
                  component="textarea"
                  className="custom-input custom-textarea"
                  name="notes"
                  placeholder="Προαιρετικές σημειώσεις για τον γιατρό..."
                />
              </div>
            </Col>
          </Row>

          <div className="form-actions">
            <Button className="btn-modern btn-secondary" onClick={() => navigate('/doctor')}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="m15 18-6-6 6-6"></path>
              </svg>
              Πίσω
            </Button>
            <Button type="submit" className="btn-modern btn-primary">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                <polyline points="17,21 17,13 7,13 7,21"></polyline>
                <polyline points="7,3 7,8 15,8"></polyline>
              </svg>
              Αποθήκευση
            </Button>
          </div>
        </ValidatedForm>
      </div>
    </div>
  );
};

export default DoctorUpdate;
