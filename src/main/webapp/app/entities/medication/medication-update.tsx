import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Card, CardBody, CardHeader, Badge } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar, faPlus, faTimes, faPills, faCheck, faArrowLeft, faSave, faTags } from '@fortawesome/free-solid-svg-icons';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, updateEntity, createEntity, reset } from './medication.reducer';

// Interactive Star Rating Component
interface InteractiveStarRatingProps {
  rating: number;
  onRatingChange: (rating: number) => void;
  disabled?: boolean;
}

const InteractiveStarRating: React.FC<InteractiveStarRatingProps> = ({ rating, onRatingChange, disabled = false }) => {
  const [hoverRating, setHoverRating] = useState(0);

  const handleStarClick = (selectedRating: number) => {
    if (!disabled) {
      onRatingChange(selectedRating);
    }
  };

  const handleStarHover = (hoveredRating: number) => {
    if (!disabled) {
      setHoverRating(hoveredRating);
    }
  };

  const handleMouseLeave = () => {
    setHoverRating(0);
  };

  const renderStars = () => {
    const stars = [];
    const displayRating = hoverRating || rating;

    for (let i = 1; i <= 10; i++) {
      const isFilled = i <= displayRating;
      stars.push(
        <FontAwesomeIcon
          key={i}
          icon={faStar}
          style={{
            color: isFilled ? '#ffd700' : '#ddd',
            cursor: disabled ? 'default' : 'pointer',
            fontSize: '24px',
            marginRight: '4px',
            transition: 'color 0.2s',
          }}
          onClick={() => handleStarClick(i)}
          onMouseEnter={() => handleStarHover(i)}
        />,
      );
    }
    return stars;
  };

  return (
    <div className="star-rating-input d-flex align-items-center" onMouseLeave={handleMouseLeave} style={{ padding: '10px 0' }}>
      <div className="me-3">{renderStars()}</div>
      <div className="d-flex align-items-center">
        <span className="h5 mb-0 text-primary me-2">{rating}</span>
        <span className="text-muted">/10</span>
      </div>
    </div>
  );
};

// Category Selection Component
interface CategorySelectorProps {
  selectedCategories: string[];
  onCategoriesChange: (categories: string[]) => void;
  availableCategories: string[];
}

const CategorySelector: React.FC<CategorySelectorProps> = ({ selectedCategories, onCategoriesChange, availableCategories }) => {
  const [newCategory, setNewCategory] = useState('');
  const [showAddCategory, setShowAddCategory] = useState(false);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [filteredSuggestions, setFilteredSuggestions] = useState<string[]>([]);
  const [errorMessage, setErrorMessage] = useState('');

  const handleCategoryToggle = (category: string) => {
    const isSelected = selectedCategories.includes(category);
    if (isSelected) {
      onCategoriesChange(selectedCategories.filter(c => c !== category));
    } else {
      onCategoriesChange([...selectedCategories, category]);
    }
  };

  const handleAddNewCategory = () => {
    const trimmedCategory = newCategory.trim();

    setErrorMessage('');

    if (!trimmedCategory) {
      setErrorMessage('Category name cannot be empty');
      return;
    }

    const categoryExists = availableCategories.some(cat => cat.toLowerCase() === trimmedCategory.toLowerCase());

    if (categoryExists) {
      setErrorMessage(`Category "${trimmedCategory}" already exists. Please select it from the list above or choose a different name.`);
      return;
    }

    const alreadySelected = selectedCategories.some(cat => cat.toLowerCase() === trimmedCategory.toLowerCase());

    if (alreadySelected) {
      setErrorMessage(`Category "${trimmedCategory}" is already selected.`);
      return;
    }

    const updatedCategories = [...selectedCategories, trimmedCategory];
    onCategoriesChange(updatedCategories);
    setNewCategory('');
    setShowAddCategory(false);
    setShowSuggestions(false);
    setErrorMessage('');
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setNewCategory(value);
    setErrorMessage('');

    if (value.trim().length > 0) {
      const filtered = availableCategories.filter(
        category => category.toLowerCase().includes(value.toLowerCase()) && !selectedCategories.includes(category),
      );
      setFilteredSuggestions(filtered);
      setShowSuggestions(filtered.length > 0);
    } else {
      setShowSuggestions(false);
    }
  };

  const handleInputFocus = () => {
    setErrorMessage('');

    if (newCategory.trim().length > 0) {
      const filtered = availableCategories.filter(
        category => category.toLowerCase().includes(newCategory.toLowerCase()) && !selectedCategories.includes(category),
      );
      setFilteredSuggestions(filtered);
      setShowSuggestions(filtered.length > 0);
    } else {
      const unselected = availableCategories.filter(category => !selectedCategories.includes(category));
      setFilteredSuggestions(unselected);
      setShowSuggestions(unselected.length > 0);
    }
  };

  const handleSuggestionClick = (suggestion: string) => {
    setNewCategory(suggestion);
    setShowSuggestions(false);
    setErrorMessage('');
    const updatedCategories = [...selectedCategories, suggestion];
    onCategoriesChange(updatedCategories);
    setNewCategory('');
    setShowAddCategory(false);
  };

  const handleInputBlur = () => {
    setTimeout(() => setShowSuggestions(false), 200);
  };

  const handleCancel = () => {
    setShowAddCategory(false);
    setNewCategory('');
    setShowSuggestions(false);
    setErrorMessage('');
  };

  return (
    <div>
      <div className="mb-3">
        <h6 className="text-muted mb-2">Select Categories:</h6>
        <div className="d-flex flex-wrap gap-2">
          {availableCategories.map((category, index) => (
            <Badge
              key={index}
              color={selectedCategories.includes(category) ? 'primary' : 'outline-secondary'}
              className="p-2 cursor-pointer user-select-none"
              style={{
                cursor: 'pointer',
                fontSize: '14px',
                transition: 'all 0.2s',
              }}
              onClick={() => handleCategoryToggle(category)}
            >
              {category}
              {selectedCategories.includes(category) && <FontAwesomeIcon icon={faCheck} className="ms-2" />}
            </Badge>
          ))}
        </div>
      </div>

      {!showAddCategory ? (
        <Button color="outline-success" size="sm" onClick={() => setShowAddCategory(true)}>
          <FontAwesomeIcon icon={faPlus} className="me-2" />
          Add New Category
        </Button>
      ) : (
        <div className="position-relative">
          <div className="d-flex gap-2 align-items-center">
            <div className="flex-grow-1 position-relative">
              <ValidatedField
                name="newCategory"
                placeholder="Enter new category name or select from suggestions"
                value={newCategory}
                onChange={handleInputChange}
                onFocus={handleInputFocus}
                onBlur={handleInputBlur}
                className={`w-100 ${errorMessage ? 'is-invalid' : ''}`}
                autoComplete="off"
              />

              {errorMessage && (
                <div className="invalid-feedback d-block">
                  <FontAwesomeIcon icon={faTimes} className="me-1" />
                  {errorMessage}
                </div>
              )}

              {showSuggestions && filteredSuggestions.length > 0 && (
                <div
                  className="position-absolute w-100 bg-white border rounded shadow-lg"
                  style={{
                    top: '100%',
                    zIndex: 1000,
                    maxHeight: '200px',
                    overflowY: 'auto',
                  }}
                >
                  {filteredSuggestions.map((suggestion, index) => (
                    <div
                      key={index}
                      className="p-2 cursor-pointer border-bottom"
                      style={{
                        cursor: 'pointer',
                        transition: 'background-color 0.2s',
                      }}
                      onMouseDown={() => handleSuggestionClick(suggestion)}
                      onMouseEnter={e => ((e.target as HTMLElement).style.backgroundColor = '#f8f9fa')}
                      onMouseLeave={e => ((e.target as HTMLElement).style.backgroundColor = 'white')}
                    >
                      <FontAwesomeIcon icon={faTags} className="me-2 text-muted" />
                      {suggestion}
                    </div>
                  ))}
                </div>
              )}
            </div>

            <Button color="success" size="sm" onClick={handleAddNewCategory} disabled={!newCategory.trim()}>
              <FontAwesomeIcon icon={faCheck} />
            </Button>
            <Button color="outline-secondary" size="sm" onClick={handleCancel}>
              <FontAwesomeIcon icon={faTimes} />
            </Button>
          </div>
        </div>
      )}

      {selectedCategories.length > 0 && (
        <div className="mt-3">
          <h6 className="text-muted mb-2">Selected Categories:</h6>
          <div className="d-flex flex-wrap gap-2">
            {selectedCategories.map((category, index) => (
              <Badge key={index} color="primary" className="p-2">
                {category}
                <FontAwesomeIcon
                  icon={faTimes}
                  className="ms-2 cursor-pointer"
                  onClick={() => handleCategoryToggle(category)}
                  style={{ cursor: 'pointer' }}
                />
              </Badge>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export const MedicationUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const [rating, setRating] = useState(5);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);

  // Available categories (these could come from an API call)
  const availableCategories = [
    'Headache',
    'Pain Relief',
    'Digestive',
    'Heart & Blood',
    'Respiratory',
    'Mental Health',
    'Antibiotics',
    'Vitamins',
    'Bone & Joint',
    'Skin Care',
    'Eye Care',
    'Sleep Aid',
  ];

  const medicationEntity = useAppSelector(state => state.medication.entity);
  const loading = useAppSelector(state => state.medication.loading);
  const updating = useAppSelector(state => state.medication.updating);
  const updateSuccess = useAppSelector(state => state.medication.updateSuccess);

  const handleClose = () => {
    navigate('/medication');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      setRating(5);
      setSelectedCategories([]);
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (medicationEntity && !isNew) {
      setRating(medicationEntity.rating || 5);
      if (medicationEntity.categories) {
        setSelectedCategories(medicationEntity.categories.map((cat: any) => cat.name));
      }
    }
  }, [medicationEntity, isNew]);

  const saveEntity = values => {
    const entity = {
      ...medicationEntity,
      ...values,
      rating,
      // categories: selectedCategories.map(name => ({ name })), // Uncomment to send categories if backend supports
      createdDate: isNew ? new Date().toISOString().split('T')[0] : medicationEntity.createdDate,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          rating: 5,
        }
      : {
          ...medicationEntity,
          createdDate: medicationEntity?.createdDate,
          lastTaken: medicationEntity?.lastTaken,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md={8}>
          <Card className="shadow-sm">
            <CardHeader className="bg-primary text-white">
              <h3 className="mb-0">
                <FontAwesomeIcon icon={faPills} className="me-2" />
                {isNew ? 'Create a new Medication' : 'Edit Medication'}
              </h3>
            </CardHeader>
            <CardBody>
              {loading ? (
                <p>Loading...</p>
              ) : (
                <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                  {!isNew ? (
                    <ValidatedField
                      name="id"
                      required
                      readOnly
                      id="medication-id"
                      label="ID"
                      validate={{ required: true }}
                      style={{ display: 'none' }}
                    />
                  ) : null}

                  {/* Medicine Name */}
                  <ValidatedField
                    label="Medicine Name"
                    id="medication-name"
                    name="name"
                    data-cy="name"
                    type="text"
                    className="form-control-lg"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                    }}
                  />

                  {/* Interactive Rating */}
                  <div className="mb-3">
                    <label className="form-label">
                      <strong>Rating (1-10)</strong>
                    </label>
                    <InteractiveStarRating rating={rating} onRatingChange={setRating} />
                    <FormText>Rate this medication from 1 (least effective) to 10 (most effective)</FormText>
                  </div>

                  {/* Categories */}
                  <div className="mb-4">
                    <label className="form-label">
                      <strong>Categories</strong>
                    </label>
                    <CategorySelector
                      selectedCategories={selectedCategories}
                      onCategoriesChange={setSelectedCategories}
                      availableCategories={availableCategories}
                    />
                    <FormText>Select or create categories that apply to this medication</FormText>
                  </div>

                  {/* Notes */}
                  <ValidatedField label="Notes" id="medication-notes" name="notes" data-cy="notes" type="textarea" rows={4} />

                  {/* Dosage */}
                  <ValidatedField
                    label="Dosage"
                    id="medication-dosage"
                    name="dosage"
                    data-cy="dosage"
                    type="text"
                    validate={{
                      maxLength: { value: 200, message: 'This field cannot be longer than 200 characters.' },
                    }}
                    placeholder="e.g., 500mg, 1 tablet, 2 capsules"
                  />

                  {/* Frequency */}
                  <ValidatedField
                    label="Frequency"
                    id="medication-frequency"
                    name="frequency"
                    data-cy="frequency"
                    type="text"
                    validate={{
                      maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                    }}
                    placeholder="e.g., Twice daily, As needed, Every 6 hours"
                  />

                  {/* Side Effects */}
                  <ValidatedField
                    label="Side Effects"
                    id="medication-sideEffects"
                    name="sideEffects"
                    data-cy="sideEffects"
                    type="textarea"
                    rows={3}
                    validate={{
                      maxLength: { value: 500, message: 'This field cannot be longer than 500 characters.' },
                    }}
                    placeholder="List any side effects you've experienced"
                  />

                  {/* Last Taken */}
                  <ValidatedField label="Last Taken" id="medication-lastTaken" name="lastTaken" data-cy="lastTaken" type="date" />

                  {/* Action Buttons */}
                  <div className="d-flex justify-content-between pt-4">
                    <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/medication" replace color="info" size="lg">
                      <FontAwesomeIcon icon={faArrowLeft} />
                      &nbsp;
                      <span className="d-none d-md-inline">Back</span>
                    </Button>
                    <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating} size="lg">
                      <FontAwesomeIcon icon={faSave} />
                      &nbsp;
                      {isNew ? 'Create' : 'Save'}
                    </Button>
                  </div>
                </ValidatedForm>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>

      <style>{`
        .cursor-pointer {
          cursor: pointer;
        }

        .user-select-none {
          user-select: none;
        }

        .star-rating-input .fa-star:hover {
          color: #ffed4e !important;
        }

        .badge.cursor-pointer:hover {
          transform: translateY(-1px);
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .form-control-lg {
          font-size: 1.1rem;
          padding: 0.75rem;
        }
      `}</style>
    </div>
  );
};

export default MedicationUpdate;
