import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMedication } from 'app/shared/model/medication.model';

export interface IMedicationCategory {
  id?: number;
  name?: string;
  description?: string | null;
  color?: string | null;
  icon?: string | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  medications?: IMedication[] | null;
}

export const defaultValue: Readonly<IMedicationCategory> = {};
