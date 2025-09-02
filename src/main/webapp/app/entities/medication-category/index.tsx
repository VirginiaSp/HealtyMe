import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MedicationCategory from './medication-category';
import MedicationCategoryDetail from './medication-category-detail';
import MedicationCategoryUpdate from './medication-category-update';
import MedicationCategoryDeleteDialog from './medication-category-delete-dialog';

const MedicationCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MedicationCategory />} />
    <Route path="new" element={<MedicationCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<MedicationCategoryDetail />} />
      <Route path="edit" element={<MedicationCategoryUpdate />} />
      <Route path="delete" element={<MedicationCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicationCategoryRoutes;
