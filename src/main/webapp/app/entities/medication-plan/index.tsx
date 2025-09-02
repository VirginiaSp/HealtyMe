import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MedicationPlan from './medication-plan';
import MedicationPlanDetail from './medication-plan-detail';
import MedicationPlanUpdate from './medication-plan-update';
import MedicationPlanDeleteDialog from './medication-plan-delete-dialog';

const MedicationPlanRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MedicationPlan />} />
    <Route path="new" element={<MedicationPlanUpdate />} />
    <Route path=":id">
      <Route index element={<MedicationPlanDetail />} />
      <Route path="edit" element={<MedicationPlanUpdate />} />
      <Route path="delete" element={<MedicationPlanDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicationPlanRoutes;
