import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExaminationRecord from './examination-record';
import ExaminationRecordDetail from './examination-record-detail';
import ExaminationRecordUpdate from './examination-record-update';
import ExaminationRecordDeleteDialog from './examination-record-delete-dialog';

const ExaminationRecordRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExaminationRecord />} />
    <Route path="new" element={<ExaminationRecordUpdate />} />
    <Route path=":id">
      <Route index element={<ExaminationRecordDetail />} />
      <Route path="edit" element={<ExaminationRecordUpdate />} />
      <Route path="delete" element={<ExaminationRecordDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExaminationRecordRoutes;
