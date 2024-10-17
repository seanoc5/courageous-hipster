import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Analyzer from './analyzer';
import AnalyzerDetail from './analyzer-detail';
import AnalyzerUpdate from './analyzer-update';
import AnalyzerDeleteDialog from './analyzer-delete-dialog';

const AnalyzerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Analyzer />} />
    <Route path="new" element={<AnalyzerUpdate />} />
    <Route path=":id">
      <Route index element={<AnalyzerDetail />} />
      <Route path="edit" element={<AnalyzerUpdate />} />
      <Route path="delete" element={<AnalyzerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AnalyzerRoutes;
