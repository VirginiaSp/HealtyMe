import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MedicationPlanDose from './medication-plan-dose';
import MedicationPlanDoseDetail from './medication-plan-dose-detail';
import MedicationPlanDoseUpdate from './medication-plan-dose-update';
import MedicationPlanDoseDeleteDialog from './medication-plan-dose-delete-dialog';

const MedicationPlanDoseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MedicationPlanDose />} />
    <Route path="new" element={<MedicationPlanDoseUpdate />} />
    <Route path=":id">
      <Route index element={<MedicationPlanDoseDetail />} />
      <Route path="edit" element={<MedicationPlanDoseUpdate />} />
      <Route path="delete" element={<MedicationPlanDoseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicationPlanDoseRoutes;
