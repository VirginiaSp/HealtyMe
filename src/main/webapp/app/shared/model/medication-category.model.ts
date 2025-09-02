import { IUser } from 'app/shared/model/user.model';
import { IMedication } from 'app/shared/model/medication.model';

export interface IMedicationCategory {
  id?: number;
  name?: string;
  description?: string | null;
  owner?: IUser;
  medications?: IMedication[] | null;
}

export const defaultValue: Readonly<IMedicationCategory> = {};
