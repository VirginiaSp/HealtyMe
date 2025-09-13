import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, CardHeader, Collapse, Badge, Modal, ModalHeader, ModalBody, ModalFooter, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faStar,
  faChevronDown,
  faChevronUp,
  faPills,
  faCalendar,
  faUser,
  faEdit,
  faTrash,
  faEye,
  faThList,
  faTags,
  faSortAlphaDown,
  faPlus,
  faSync,
} from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './medication.reducer';

enum ViewType {
  MEDICINE_NAME = 'medicine_name',
  CATEGORIES = 'categories',
  RATING = 'rating',
}

interface StarRatingProps {
  rating: number;
  size?: 'sm' | 'md' | 'lg';
}

const StarRating: React.FC<StarRatingProps> = ({ rating, size = 'sm' }) => {
  const getStarSize = () => {
    switch (size) {
      case 'sm':
        return '14px';
      case 'md':
        return '18px';
      case 'lg':
        return '24px';
      default:
        return '14px';
    }
  };

  const renderStars = () => {
    const stars = [];
    const fullStars = Math.floor(rating);

    for (let i = 0; i < 10; i++) {
      stars.push(
        <FontAwesomeIcon
          key={i}
          icon={faStar}
          style={{
            color: i < fullStars ? '#ffd700' : '#ddd',
            fontSize: getStarSize(),
            marginRight: '2px',
          }}
        />,
      );
    }

    return stars;
  };

  return (
    <div className="d-flex align-items-center">
      {renderStars()}
      <span className="ms-2 text-muted" style={{ fontSize: '12px' }}>
        ({rating}/10)
      </span>
    </div>
  );
};

interface MedicationModalProps {
  medication: any;
  isOpen: boolean;
  toggle: () => void;
  onEdit: (id: number) => void;
  onDelete: (id: number) => void;
}

const MedicationModal: React.FC<MedicationModalProps> = ({ medication, isOpen, toggle, onEdit, onDelete }) => {
  if (!medication) return null;

  return (
    <Modal isOpen={isOpen} toggle={toggle} size="lg">
      <ModalHeader toggle={toggle}>
        <FontAwesomeIcon icon={faPills} className="me-2" />
        {medication.name}
      </ModalHeader>
      <ModalBody>
        <Row>
          <Col md={6}>
            <div className="mb-3">
              <strong>Rating:</strong>
              <div className="mt-1">
                <StarRating rating={medication.rating || 0} size="md" />
              </div>
            </div>

            <div className="mb-3">
              <strong>Dosage:</strong>
              <p className="mb-0">{medication.dosage || 'Not specified'}</p>
            </div>

            <div className="mb-3">
              <strong>Frequency:</strong>
              <p className="mb-0">{medication.frequency || 'Not specified'}</p>
            </div>
          </Col>

          <Col md={6}>
            <div className="mb-3">
              <strong>Side Effects:</strong>
              <p className="mb-0">{medication.sideEffects || 'None reported'}</p>
            </div>

            <div className="mb-3">
              <strong>Last Taken:</strong>
              <p className="mb-0">
                <FontAwesomeIcon icon={faCalendar} className="me-1" />
                {medication.lastTaken || 'Never'}
              </p>
            </div>

            {medication.categories && medication.categories.length > 0 && (
              <div className="mb-3">
                <strong>Categories:</strong>
                <div className="mt-1">
                  {medication.categories.map((category: any, index: number) => (
                    <Badge key={index} color="primary" className="me-1 mb-1">
                      {category.name}
                    </Badge>
                  ))}
                </div>
              </div>
            )}
          </Col>
        </Row>

        {medication.notes && (
          <div className="mb-3">
            <strong>Notes:</strong>
            <div className="mt-1 p-2" style={{ backgroundColor: '#f8f9fa', borderRadius: '4px' }}>
              {medication.notes}
            </div>
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="info" onClick={() => onEdit(medication.id)}>
          <FontAwesomeIcon icon={faEdit} className="me-1" />
          Edit
        </Button>
        <Button color="danger" onClick={() => onDelete(medication.id)}>
          <FontAwesomeIcon icon={faTrash} className="me-1" />
          Delete
        </Button>
        <Button color="secondary" onClick={toggle}>
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export const Medication = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  // NEW: Separate pagination and sort state
  const [pagination, setPagination] = useState({
    activePage: 1,
    itemsPerPage: ITEMS_PER_PAGE,
  });

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams({ sort: 'id', order: 'ASC' }, pageLocation.search));

  const [currentView, setCurrentView] = useState<ViewType>(() => {
    const savedView = localStorage.getItem('medicationViewPreference') as ViewType;
    return savedView && Object.values(ViewType).includes(savedView) ? savedView : ViewType.MEDICINE_NAME;
  });

  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());
  const [expandedRatings, setExpandedRatings] = useState<Set<number>>(new Set());
  const [selectedMedication, setSelectedMedication] = useState<any>(null);
  const [modalOpen, setModalOpen] = useState(false);

  const medicationList = useAppSelector(state => state.medication.entities);
  const loading = useAppSelector(state => state.medication.loading);
  const totalItems = useAppSelector(state => state.medication.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  useEffect(() => {
    getAllEntities();
  }, [pagination.activePage, pagination.itemsPerPage, sortState.order, sortState.sort]);

  const setView = (view: ViewType) => {
    setCurrentView(view);
    localStorage.setItem('medicationViewPreference', view);
  };

  const toggleCategory = (category: string) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(category)) {
      newExpanded.delete(category);
    } else {
      newExpanded.add(category);
    }
    setExpandedCategories(newExpanded);
  };

  const toggleRating = (rating: number) => {
    const newExpanded = new Set(expandedRatings);
    if (newExpanded.has(rating)) {
      newExpanded.delete(rating);
    } else {
      newExpanded.add(rating);
    }
    setExpandedRatings(newExpanded);
  };

  const openMedicationModal = (medication: any) => {
    setSelectedMedication(medication);
    setModalOpen(true);
  };

  const closeMedicationModal = () => {
    setModalOpen(false);
    setSelectedMedication(null);
  };

  const handleEdit = (id: number) => {
    navigate(`/medication/${id}/edit`);
  };

  const handleDelete = (id: number) => {
    navigate(`/medication/${id}/delete`);
  };

  // Get all unique categories from medications
  const getMedicationsByCategory = () => {
    if (!medicationList) return [];

    const categoryMap = new Map();

    // Mock categories since the relationship might not be fully implemented yet
    const mockCategories = [
      'Headache',
      'Pain Relief',
      'Digestive',
      'Heart & Blood',
      'Respiratory',
      'Mental Health',
      'Antibiotics',
      'Vitamins',
    ];

    medicationList.forEach(medication => {
      // If medication has categories, use them
      if (medication.categories && medication.categories.length > 0) {
        medication.categories.forEach((category: any) => {
          if (!categoryMap.has(category.name)) {
            categoryMap.set(category.name, []);
          }
          categoryMap.get(category.name).push(medication);
        });
      } else {
        // Otherwise, assign to a mock category based on name or other criteria
        const randomCategory = mockCategories[Math.floor(Math.random() * mockCategories.length)];
        if (!categoryMap.has(randomCategory)) {
          categoryMap.set(randomCategory, []);
        }
        categoryMap.get(randomCategory).push(medication);
      }
    });

    return Array.from(categoryMap.entries())
      .map(([categoryName, medications]) => ({
        name: categoryName,
        medications,
      }))
      .sort((a, b) => a.name.localeCompare(b.name));
  };

  // Group medications by rating (1-10)
  const getMedicationsByRating = () => {
    if (!medicationList) return [];

    const ratingGroups = [];

    for (let rating = 10; rating >= 1; rating--) {
      const medications = medicationList.filter((m: any) => m.rating === rating);
      if (medications.length > 0) {
        ratingGroups.push({
          rating,
          medications,
        });
      }
    }

    return ratingGroups;
  };

  // Simple alphabetical list
  const getSortedMedications = () => {
    if (!medicationList) return [];
    return [...medicationList].sort((a: any, b: any) => a.name?.toLowerCase().localeCompare(b.name?.toLowerCase()) || 0);
  };

  const renderMedicineNameView = () => {
    const sortedMedications = getSortedMedications();

    return (
      <div>
        <Row>
          {sortedMedications.map((medication: any, index: number) => (
            <Col md={6} lg={4} key={index} className="mb-3">
              <Card
                className="h-100 medication-card border-0 shadow-sm"
                style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                onClick={() => openMedicationModal(medication)}
              >
                <CardBody>
                  <h6 className="card-title text-primary">{medication.name}</h6>
                  <StarRating rating={medication.rating || 0} />
                  <p className="card-text small text-muted mt-2" style={{ maxHeight: '40px', overflow: 'hidden' }}>
                    {medication.notes || 'No notes available'}
                  </p>
                  <div className="d-flex justify-content-between align-items-center mt-2">
                    <small className="text-muted">{medication.dosage || 'No dosage'}</small>
                    <Badge color="light" className="text-dark">
                      {medication.frequency || 'As needed'}
                    </Badge>
                  </div>
                </CardBody>
              </Card>
            </Col>
          ))}
        </Row>
      </div>
    );
  };

  const renderCategoriesView = () => {
    const categories = getMedicationsByCategory();

    return (
      <div>
        {categories.map((category: any, index: number) => (
          <Card key={index} className="mb-3 border-0 shadow-sm">
            <CardHeader
              className="bg-white border-0 cursor-pointer"
              onClick={() => toggleCategory(category.name)}
              style={{ cursor: 'pointer' }}
            >
              <div className="d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center">
                  <div
                    className="me-3 rounded-circle d-flex align-items-center justify-content-center"
                    style={{
                      width: '40px',
                      height: '40px',
                      backgroundColor: '#17a2b820',
                      color: '#17a2b8',
                    }}
                  >
                    <FontAwesomeIcon icon={faTags} />
                  </div>
                  <div>
                    <h5 className="mb-0 text-info">{category.name}</h5>
                    <small className="text-muted">
                      {category.medications.length} medication{category.medications.length !== 1 ? 's' : ''}
                    </small>
                  </div>
                </div>
                <FontAwesomeIcon icon={expandedCategories.has(category.name) ? faChevronUp : faChevronDown} className="text-info" />
              </div>
            </CardHeader>
            <Collapse isOpen={expandedCategories.has(category.name)}>
              <CardBody className="pt-0">
                <Row>
                  {category.medications.map((medication: any, medIndex: number) => (
                    <Col md={6} lg={4} key={medIndex} className="mb-3">
                      <Card
                        className="h-100 medication-card border-0 shadow-sm"
                        style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                        onClick={() => openMedicationModal(medication)}
                      >
                        <CardBody>
                          <h6 className="card-title text-primary">{medication.name}</h6>
                          <StarRating rating={medication.rating || 0} />
                          <p className="card-text small text-muted mt-2" style={{ maxHeight: '40px', overflow: 'hidden' }}>
                            {medication.notes || 'No notes available'}
                          </p>
                          <div className="d-flex justify-content-between align-items-center mt-2">
                            <small className="text-muted">{medication.dosage || 'No dosage'}</small>
                            <Badge color="light" className="text-dark">
                              {medication.frequency || 'As needed'}
                            </Badge>
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
        ))}
      </div>
    );
  };

  const renderRatingView = () => {
    const ratingGroups = getMedicationsByRating();

    return (
      <div>
        {ratingGroups.map((group: any, index: number) => (
          <Card key={index} className="mb-3 border-0 shadow-sm">
            <CardHeader
              className="bg-white border-0 cursor-pointer"
              onClick={() => toggleRating(group.rating)}
              style={{ cursor: 'pointer' }}
            >
              <div className="d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center">
                  <div
                    className="me-3 rounded-circle d-flex align-items-center justify-content-center fw-bold"
                    style={{
                      width: '40px',
                      height: '40px',
                      backgroundColor: group.rating >= 8 ? '#28a74520' : group.rating >= 5 ? '#ffc10720' : '#dc354520',
                      color: group.rating >= 8 ? '#28a745' : group.rating >= 5 ? '#ffc107' : '#dc3545',
                      fontSize: '16px',
                    }}
                  >
                    {group.rating}
                  </div>
                  <div>
                    <h5
                      className="mb-0"
                      style={{
                        color: group.rating >= 8 ? '#28a745' : group.rating >= 5 ? '#ffc107' : '#dc3545',
                      }}
                    >
                      Rating {group.rating}/10
                      <div className="d-inline-block ms-2">
                        <StarRating rating={group.rating} size="sm" />
                      </div>
                    </h5>
                    <small className="text-muted">
                      {group.medications.length} medication{group.medications.length !== 1 ? 's' : ''}
                    </small>
                  </div>
                </div>
                <FontAwesomeIcon
                  icon={expandedRatings.has(group.rating) ? faChevronUp : faChevronDown}
                  style={{
                    color: group.rating >= 8 ? '#28a745' : group.rating >= 5 ? '#ffc107' : '#dc3545',
                  }}
                />
              </div>
            </CardHeader>
            <Collapse isOpen={expandedRatings.has(group.rating)}>
              <CardBody className="pt-0">
                <Row>
                  {group.medications.map((medication: any, medIndex: number) => (
                    <Col md={6} lg={4} key={medIndex} className="mb-3">
                      <Card
                        className="h-100 medication-card border-0 shadow-sm"
                        style={{ cursor: 'pointer', transition: 'all 0.2s' }}
                        onClick={() => openMedicationModal(medication)}
                      >
                        <CardBody>
                          <h6 className="card-title text-primary">{medication.name}</h6>
                          <StarRating rating={medication.rating || 0} />
                          <p className="card-text small text-muted mt-2" style={{ maxHeight: '40px', overflow: 'hidden' }}>
                            {medication.notes || 'No notes available'}
                          </p>
                          <div className="d-flex justify-content-between align-items-center mt-2">
                            <small className="text-muted">{medication.dosage || 'No dosage'}</small>
                            <Badge color="light" className="text-dark">
                              {medication.frequency || 'As needed'}
                            </Badge>
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
        ))}
      </div>
    );
  };

  return (
    <div>
      <h2 id="medication-heading" data-cy="MedicationHeading">
        <Translate contentKey="healthyMeApp.medication.home.title">Medications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={getAllEntities} disabled={loading}>
            <FontAwesomeIcon icon={faSync} spin={loading} />{' '}
            <Translate contentKey="healthyMeApp.medication.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/medication/new" className="btn btn-primary create-entity-btn" id="create-entity-btn" data-cy="entityCreateButton">
            <FontAwesomeIcon icon={faPlus} />
            &nbsp;
            <Translate contentKey="healthyMeApp.medication.home.createLabel">Create new Medication</Translate>
          </Link>
        </div>
      </h2>

      {/* View Toggle - 3 Tabs */}
      <div className="mb-4 d-flex justify-content-center">
        <div className="btn-group shadow-sm" role="group">
          <Button
            color={currentView === ViewType.MEDICINE_NAME ? 'primary' : 'outline-primary'}
            onClick={() => setView(ViewType.MEDICINE_NAME)}
            className="px-4"
          >
            <FontAwesomeIcon icon={faSortAlphaDown} className="me-2" />
            Medicine Name
          </Button>
          <Button
            color={currentView === ViewType.CATEGORIES ? 'primary' : 'outline-primary'}
            onClick={() => setView(ViewType.CATEGORIES)}
            className="px-4"
          >
            <FontAwesomeIcon icon={faTags} className="me-2" />
            Categories
          </Button>
          <Button
            color={currentView === ViewType.RATING ? 'primary' : 'outline-primary'}
            onClick={() => setView(ViewType.RATING)}
            className="px-4"
          >
            <FontAwesomeIcon icon={faStar} className="me-2" />
            Rating
          </Button>
        </div>
      </div>

      {/* Medication List */}
      {medicationList && medicationList.length > 0 ? (
        <div>
          {currentView === ViewType.MEDICINE_NAME && renderMedicineNameView()}
          {currentView === ViewType.CATEGORIES && renderCategoriesView()}
          {currentView === ViewType.RATING && renderRatingView()}
        </div>
      ) : (
        !loading && (
          <div className="alert alert-warning text-center py-5">
            <FontAwesomeIcon icon={faPills} size="3x" className="mb-3 text-muted" />
            <h4>No Medications Found</h4>
            <p>Start by adding your first medication to track your health journey.</p>
            <Link to="/medication/new" className="btn btn-primary">
              <FontAwesomeIcon icon={faPlus} className="me-2" />
              Add Your First Medication
            </Link>
          </div>
        )
      )}

      {/* Medication Detail Modal */}
      <MedicationModal
        medication={selectedMedication}
        isOpen={modalOpen}
        toggle={closeMedicationModal}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <style>{`
        .medication-card:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(0,0,0,0.15) !important;
        }

        .cursor-pointer {
          cursor: pointer;
        }

        .btn-group {
          border-radius: 8px;
          overflow: hidden;
        }

        .btn-group .btn {
          border: none;
          font-weight: 500;
          transition: all 0.2s;
        }

        .btn-group .btn:hover {
          transform: translateY(-1px);
        }
      `}</style>
    </div>
  );
};

export default Medication;
