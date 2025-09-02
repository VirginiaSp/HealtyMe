import { IUser } from 'app/shared/model/user.model';

export interface IDoctor {
  id?: number;
  firstName?: string;
  lastName?: string;
  specialty?: string;
  phone?: string | null;
  address?: string | null;
  notes?: string | null;
  owner?: IUser;
}

export const defaultValue: Readonly<IDoctor> = {};
