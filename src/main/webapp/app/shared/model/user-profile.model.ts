import { IUser } from 'app/shared/model/user.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IUserProfile {
  id?: number;
  firstName?: string;
  lastName?: string;
  phone?: string | null;
  gender?: keyof typeof Gender | null;
  user?: IUser;
}

export const defaultValue: Readonly<IUserProfile> = {};
