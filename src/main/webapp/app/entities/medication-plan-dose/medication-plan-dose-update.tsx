import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'app/shared/component';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getMedicationPlans } from 'app/entities/medication-plan/medication-plan.reducer';
import { createEntity, getEntity, reset, updateEntity } from './medication-plan-dose.reducer';

export const MedicationPlanDoseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const medicationPlans = useAppSelector(state => state.medicationPlan.entities);
  const medicationPlanDoseEntity = useAppSelector(state => state.medicationPlanDose.entity);
  const loading = useAppSelector(state => state.medicationPlanDose.loading);
  const updating = useAppSelector(state => state.medicationPlanDose.updating);
  const updateSuccess = useAppSelector(state => state.medicationPlanDose.updateSuccess);

  const handleClose = () => {
    navigate('/medication-plan-dose');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getMedicationPlans({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...medicationPlanDoseEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner?.toString()),
      plan: medicationPlans.find(it => it.id.toString() === values.plan?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...medicationPlanDoseEntity,
          owner: medicationPlanDoseEntity?.owner?.id,
          plan: medicationPlanDoseEntity?.plan?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="healthyMeApp.medicationPlanDose.home.createOrEditLabel" data-cy="MedicationPlanDoseCreateUpdateHeading">
            <Translate contentKey="healthyMeApp.medicationPlanDose.home.createOrEditLabel">Create or edit a MedicationPlanDose</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="medication-plan-dose-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('healthyMeApp.medicationPlanDose.timeOfDay')}
                id="medication-plan-dose-timeOfDay"
                name="timeOfDay"
                data-cy="timeOfDay"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /[0-2]\d:[0-5]\d/, message: translate('entity.validation.pattern', { pattern: '[0-2]\\d:[0-5]\\d' }) },
                }}
              />
              <ValidatedField
                label={translate('healthyMeApp.medicationPlanDose.notes')}
                id="medication-plan-dose-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                id="medication-plan-dose-owner"
                name="owner"
                data-cy="owner"
                label={translate('healthyMeApp.medicationPlanDose.owner')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="medication-plan-dose-plan"
                name="plan"
                data-cy="plan"
                label={translate('healthyMeApp.medicationPlanDose.plan')}
                type="select"
                required
              >
                <option value="" key="0" />
                {medicationPlans
                  ? medicationPlans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/medication-plan-dose" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MedicationPlanDoseUpdate;
