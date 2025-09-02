import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IExaminationCategory } from 'app/shared/model/examination-category.model';

export interface IExaminationRecord {
  id?: number;
  title?: string;
  examDate?: dayjs.Dayjs;
  fileContentType?: string;
  file?: string;
  originalFilename?: string | null;
  storedFilename?: string | null;
  notes?: string | null;
  owner?: IUser;
  category?: IExaminationCategory | null;
}

export const defaultValue: Readonly<IExaminationRecord> = {};
