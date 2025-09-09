import React, { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, FormGroup, Label, Input } from 'reactstrap';
import { ValidatedField, ValidatedForm, ValidatedBlobField, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import {
  getEntity as getRecord,
  updateEntity as updateRecord,
  createEntity as createRecord,
  reset as resetRecord,
} from './examination-record.reducer';

import {
  getEntities as getCategories,
  createEntity as createCategory,
} from 'app/entities/examination-category/examination-category.reducer';

import { IExaminationRecord } from 'app/shared/model/examination-record.model';
import { IExaminationCategory } from 'app/shared/model/examination-category.model';

export const ExaminationRecordUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const loading = useAppSelector(state => state.examinationRecord.loading);
  const updating = useAppSelector(state => state.examinationRecord.updating);
  const updateSuccess = useAppSelector(state => state.examinationRecord.updateSuccess);
  const examinationRecordEntity = useAppSelector(state => state.examinationRecord.entity);

  const categories = useAppSelector(state => state.examinationCategory.entities);
  const account = useAppSelector(state => state.authentication.account);

  useEffect(() => {
    if (!isNew) {
      dispatch(getRecord(id));
    } else {
      dispatch(resetRecord());
    }
    dispatch(getCategories({}));
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      navigate('/examination-record');
    }
  }, [updateSuccess, navigate]);

  // --- Category autocomplete helpers ---
  const categoryNames: string[] = useMemo(
    () =>
      Array.from(new Set<string>((categories ?? []).map(c => (c.name ?? '').trim()).filter(Boolean))).sort((a, b) =>
        a.localeCompare(b, 'el', { sensitivity: 'base' }),
      ),
    [categories],
  );

  const [categoryName, setCategoryName] = useState<string>('');
  const [showCatList, setShowCatList] = useState(false);

  // FIXED: Set category name when editing existing record
  useEffect(() => {
    if (!isNew && examinationRecordEntity?.category?.name) {
      setCategoryName(examinationRecordEntity.category.name.trim());
    }
  }, [examinationRecordEntity, isNew]);

  const catSuggestions: string[] = useMemo(
    () => (categoryNames ?? []).filter(n => n.toLowerCase().includes((categoryName ?? '').toLowerCase())).slice(0, 8),
    [categoryNames, categoryName],
  );

  // FIXED: Save handler with proper error handling and debugging
  const saveEntity = async (values: IExaminationRecord) => {
    try {
      // 1) resolve/create category from typed name
      const typed = (categoryName ?? '').trim();
      let category: IExaminationCategory | undefined;

      console.warn('Typed category name:', typed);
      console.warn('Available categories:', categories);

      if (typed) {
        const existing = (categories ?? []).find(c => (c.name ?? '').trim().toLowerCase() === typed.toLowerCase());
        if (existing) {
          category = { id: existing.id, name: existing.name };
          console.warn('Using existing category:', category);
        } else {
          // create new category
          console.warn('Creating new category:', typed);
          const action = await dispatch(
            createCategory({
              name: typed,
              owner: account?.id ? ({ id: account.id } as any) : undefined,
            } as IExaminationCategory),
          );

          // FIXED: Proper handling of created category
          if (action.meta.requestStatus === 'fulfilled') {
            const created = (action.payload as any)?.data;
            category = created ? { id: created.id, name: created.name } : { name: typed };
            console.warn('Created category result:', category);
          } else {
            console.error('Failed to create category:', action);
            category = { name: typed };
          }
        }
      }

      // 2) auto title (backend has @NotNull)
      const computedTitle =
        (values as any)?.title ??
        examinationRecordEntity?.title ??
        (category?.name
          ? `${category.name}${values?.examDate ? ` - ${values.examDate}` : ''}`
          : `Examination${values?.examDate ? ` - ${values.examDate}` : ''}`);

      // 3) build entity
      const entity: IExaminationRecord = {
        ...examinationRecordEntity,
        ...values,
        title: computedTitle,
        category,
      };

      // DEBUG: See what's being saved
      console.warn('Saving entity:', entity);
      console.warn('Category being saved:', category);

      // 4) save
      if (isNew) {
        await dispatch(createRecord(entity));
      } else {
        await dispatch(updateRecord(entity));
      }
    } catch (error) {
      console.error('Error saving examination record:', error);
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          id: examinationRecordEntity.id,
          examDate: examinationRecordEntity.examDate,
          notes: examinationRecordEntity.notes,
          file: examinationRecordEntity.file,
          fileContentType: examinationRecordEntity.fileContentType,
        };

  return (
    <div>
      <h2 id="healthyMeApp.examinationRecord.home.createOrEditLabel" data-cy="ExaminationRecordCreateUpdateHeading">
        {isNew ? translate('healthyMeApp.examinationRecord.home.createLabel') : translate('healthyMeApp.examinationRecord.home.editLabel')}
      </h2>

      {loading ? (
        <p>Loading…</p>
      ) : (
        <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
          {!isNew && (
            <ValidatedField
              name="id"
              required
              readOnly
              id="examination-record-id"
              label={translate('global.field.id')}
              validate={{ required: true }}
            />
          )}

          {/* CATEGORY (typeahead + dropdown) */}
          <FormGroup>
            <Label for="examination-record-categoryName">Category</Label>
            <div className="position-relative">
              <Input
                id="examination-record-categoryName"
                name="categoryName"
                type="text"
                autoComplete="off"
                value={categoryName}
                onChange={e => {
                  setCategoryName(e.target.value);
                  setShowCatList(true);
                }}
                onFocus={() => setShowCatList(true)}
                onBlur={() => setTimeout(() => setShowCatList(false), 150)}
                placeholder="Type category (e.g. Blood Tests, Cardiology)"
              />
              {showCatList && (catSuggestions?.length ?? 0) > 0 && (
                <div className="dropdown-menu show w-100 mt-1" style={{ maxHeight: 240, overflowY: 'auto' }} role="listbox">
                  {catSuggestions.map(s => (
                    <button
                      type="button"
                      key={s}
                      className="dropdown-item"
                      onMouseDown={e => e.preventDefault()}
                      onClick={() => {
                        setCategoryName(s);
                        setShowCatList(false);
                      }}
                    >
                      {s}
                    </button>
                  ))}
                </div>
              )}
            </div>
            <small className="form-text text-muted">Current category: {categoryName || 'None selected'}</small>
          </FormGroup>

          {/* FILE */}
          <ValidatedBlobField
            label="File"
            id="examination-record-file"
            name="file"
            data-cy="file"
            isImage
            accept="image/*,application/pdf"
          />

          {/* EXAM DATE */}
          <ValidatedField label="Exam Date" id="examination-record-examDate" name="examDate" data-cy="examDate" type="date" />

          {/* NOTES */}
          <ValidatedField label="Notes" id="examination-record-notes" name="notes" data-cy="notes" type="textarea" />

          <div className="d-flex justify-content-end gap-2 mt-3">
            <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/examination-record" replace color="secondary">
              Cancel
            </Button>
            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
              Save
            </Button>
          </div>
        </ValidatedForm>
      )}
    </div>
  );
};

export default ExaminationRecordUpdate;
