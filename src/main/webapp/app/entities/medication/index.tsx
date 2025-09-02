import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Medication from './medication';
import MedicationDetail from './medication-detail';
import MedicationUpdate from './medication-update';
import MedicationDeleteDialog from './medication-delete-dialog';

const MedicationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Medication />} />
    <Route path="new" element={<MedicationUpdate />} />
    <Route path=":id">
      <Route index element={<MedicationDetail />} />
      <Route path="edit" element={<MedicationUpdate />} />
      <Route path="delete" element={<MedicationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicationRoutes;
