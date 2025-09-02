import { IUser } from 'app/shared/model/user.model';

export interface IExaminationCategory {
  id?: number;
  name?: string;
  description?: string | null;
  owner?: IUser;
}

export const defaultValue: Readonly<IExaminationCategory> = {};
