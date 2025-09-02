import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IVisit {
  id?: number;
  visitDate?: dayjs.Dayjs;
  notes?: string | null;
  owner?: IUser;
  doctor?: IDoctor;
}

export const defaultValue: Readonly<IVisit> = {};
