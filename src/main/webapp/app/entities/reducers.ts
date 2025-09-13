import userProfile from 'app/entities/user-profile/user-profile.reducer';
import doctor from 'app/entities/doctor/doctor.reducer';
import visit from 'app/entities/visit/visit.reducer';
import appointment from 'app/entities/appointment/appointment.reducer';
import medicationCategory from 'app/entities/medication-category/medication-category.reducer';
import medication from 'app/entities/medication/medication.reducer';
import medicationPlan from 'app/entities/medication-plan/medication-plan.reducer';
import medicationPlanDose from 'app/entities/medication-plan-dose/medication-plan-dose.reducer';
import examinationCategory from 'app/entities/examination-category/examination-category.reducer';
import examinationRecord from 'app/entities/examination-record/examination-record.reducer';
/* Add reducer imports here */

const entitiesReducers = {
  userProfile,
  doctor,
  visit,
  appointment,
  medicationCategory,
  medication,
  medicationPlan,
  medicationPlanDose,
  examinationCategory,
  examinationRecord,
  /* Add new reducers here */
};

export default entitiesReducers;
