import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IDoctor } from 'app/shared/model/doctor.model';
import { AppointmentStatus } from 'app/shared/model/enumerations/appointment-status.model';

export interface IAppointment {
  id?: number;
  startDateTime?: dayjs.Dayjs;
  durationMinutes?: number | null;
  address?: string | null;
  specialtySnapshot?: string | null;
  notes?: string | null;
  status?: keyof typeof AppointmentStatus;
  owner?: IUser;
  doctor?: IDoctor | null;
}

export const defaultValue: Readonly<IAppointment> = {};
