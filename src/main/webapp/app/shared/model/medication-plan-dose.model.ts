import { IUser } from 'app/shared/model/user.model';
import { IMedicationPlan } from 'app/shared/model/medication-plan.model';

export interface IMedicationPlanDose {
  id?: number;
  timeOfDay?: string;
  notes?: string | null;
  owner?: IUser;
  plan?: IMedicationPlan;
}

export const defaultValue: Readonly<IMedicationPlanDose> = {};
