import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import Doctor from './doctor';
import Visit from './visit';
import Appointment from './appointment';
import MedicationCategory from './medication-category';
import Medication from './medication';
import MedicationPlan from './medication-plan';
import MedicationPlanDose from './medication-plan-dose';
import ExaminationCategory from './examination-category';
import ExaminationRecord from './examination-record';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="doctor/*" element={<Doctor />} />
        <Route path="visit/*" element={<Visit />} />
        <Route path="appointment/*" element={<Appointment />} />
        <Route path="medication-category/*" element={<MedicationCategory />} />
        <Route path="medication/*" element={<Medication />} />
        <Route path="medication-plan/*" element={<MedicationPlan />} />
        <Route path="medication-plan-dose/*" element={<MedicationPlanDose />} />
        <Route path="examination-category/*" element={<ExaminationCategory />} />
        <Route path="examination-record/*" element={<ExaminationRecord />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
