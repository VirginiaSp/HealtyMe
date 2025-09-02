import { IUser } from 'app/shared/model/user.model';
import { IMedicationCategory } from 'app/shared/model/medication-category.model';

export interface IMedication {
  id?: number;
  name?: string;
  rating?: number | null;
  notes?: string | null;
  owner?: IUser;
  categories?: IMedicationCategory[] | null;
}

export const defaultValue: Readonly<IMedication> = {};
