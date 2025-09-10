import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMedicationCategory } from 'app/shared/model/medication-category.model';

export interface IMedication {
  id?: number;
  name?: string;
  rating?: number;
  notes?: string | null;
  dosage?: string | null;
  frequency?: string | null;
  sideEffects?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastTaken?: dayjs.Dayjs | null;
  owner?: IUser | null;
  categories?: IMedicationCategory[] | null;
}

export const defaultValue: Readonly<IMedication> = {};
