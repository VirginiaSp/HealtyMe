import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'app/shared/component';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset as resetAppointment, updateEntity } from './appointment.reducer';
import { getEntities as getDoctors } from 'app/entities/doctor/doctor.reducer';
import { IDoctor } from 'app/shared/model/doctor.model';
import { IAppointment } from 'app/shared/model/appointment.model';
import { AppointmentStatus } from 'app/shared/model/enumerations/appointment-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

export const AppointmentUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doctors: IDoctor[] = useAppSelector(state => state.doctor.entities);
  const appointmentEntity = useAppSelector(state => state.appointment.entity);
  const loading = useAppSelector(state => state.appointment.loading);
  const updating = useAppSelector(state => state.appointment.updating);
  const updateSuccess = useAppSelector(state => state.appointment.updateSuccess);

  const qs = new URLSearchParams(location.search);
  const prefillDate = qs.get('date');

  const toLocalInputFromDateOnly = (dateStr?: string, hour = 9, minute = 0) => {
    if (!dateStr) return undefined;
    const d = new Date(`${dateStr}T00:00:00`);
    d.setHours(hour, minute, 0, 0);
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  };

  useEffect(() => {
    if (isNew) {
      dispatch(resetAppointment());
    } else if (id !== undefined) {
      dispatch(getEntity(id));
    }
    dispatch(getDoctors({}));
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) handleClose();
  }, [updateSuccess]);

  const handleClose = () => navigate('/appointment');

  const saveEntity = (values: any) => {
    const entity: IAppointment = {
      ...appointmentEntity,
      ...values,
      startDateTime: convertDateTimeToServer(values.startDateTime),
      doctor: values.doctor ? ({ id: values.doctor } as IDoctor) : undefined,
    };
    if (isNew) dispatch(createEntity(entity));
    else dispatch(updateEntity(entity));
  };

  const defaultValues = () =>
    isNew
      ? {
          startDateTime: prefillDate ? toLocalInputFromDateOnly(prefillDate, 9, 0) : displayDefaultDateTime(),
          status: AppointmentStatus.PENDING,
        }
      : {
          ...appointmentEntity,
          startDateTime: convertDateTimeFromServer(appointmentEntity.startDateTime),
          doctor: appointmentEntity?.doctor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="healthyMeApp.appointment.home.createOrEditLabel" data-cy="AppointmentCreateUpdateHeading">
            <Translate contentKey="healthyMeApp.appointment.home.createOrEditLabel">Create or edit a Appointment</Translate>
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
                  id="appointment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('healthyMeApp.appointment.startDateTime')}
                id="appointment-startDateTime"
                name="startDateTime"
                data-cy="startDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                required
              />
              <ValidatedField
                label={translate('healthyMeApp.appointment.durationMinutes')}
                id="appointment-durationMinutes"
                name="durationMinutes"
                data-cy="durationMinutes"
                type="number"
                validate={{ min: { value: 1, message: translate('entity.validation.min', { min: 1 }) } }}
              />
              <ValidatedField
                label={translate('healthyMeApp.appointment.address')}
                id="appointment-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('healthyMeApp.appointment.specialtySnapshot')}
                id="appointment-specialtySnapshot"
                name="specialtySnapshot"
                data-cy="specialtySnapshot"
                type="text"
              />
              <ValidatedField
                label={translate('healthyMeApp.appointment.notes')}
                id="appointment-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                label={translate('healthyMeApp.appointment.status')}
                id="appointment-status"
                name="status"
                data-cy="status"
                type="select"
                required
              >
                {Object.keys(AppointmentStatus).map(key => (
                  <option value={key} key={key}>
                    {key}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="appointment-doctor"
                name="doctor"
                data-cy="doctor"
                label={translate('healthyMeApp.appointment.doctor')}
                type="select"
              >
                <option value="" />
                {doctors?.map(d => (
                  <option value={d.id} key={d.id}>
                    {d.firstName} {d.lastName}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/appointment" replace color="secondary">
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>{' '}
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AppointmentUpdate;
