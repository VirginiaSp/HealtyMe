import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExaminationCategory from './examination-category';
import ExaminationCategoryDetail from './examination-category-detail';
import ExaminationCategoryUpdate from './examination-category-update';
import ExaminationCategoryDeleteDialog from './examination-category-delete-dialog';

const ExaminationCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExaminationCategory />} />
    <Route path="new" element={<ExaminationCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<ExaminationCategoryDetail />} />
      <Route path="edit" element={<ExaminationCategoryUpdate />} />
      <Route path="delete" element={<ExaminationCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExaminationCategoryRoutes;
