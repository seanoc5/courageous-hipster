import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Context from './context';
import ContextDetail from './context-detail';
import ContextUpdate from './context-update';
import ContextDeleteDialog from './context-delete-dialog';

const ContextRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Context />} />
    <Route path="new" element={<ContextUpdate />} />
    <Route path=":id">
      <Route index element={<ContextDetail />} />
      <Route path="edit" element={<ContextUpdate />} />
      <Route path="delete" element={<ContextDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContextRoutes;
