import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IMedicationPlan {
  id?: number;
  planName?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  medicationName?: string;
  colorHex?: string | null;
  owner?: IUser;
}

export const defaultValue: Readonly<IMedicationPlan> = {};
