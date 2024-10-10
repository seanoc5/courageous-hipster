import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Organization from './organization';
import OrganizationDetail from './organization-detail';
import OrganizationUpdate from './organization-update';
import OrganizationDeleteDialog from './organization-delete-dialog';

const OrganizationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Organization />} />
    <Route path="new" element={<OrganizationUpdate />} />
    <Route path=":id">
      <Route index element={<OrganizationDetail />} />
      <Route path="edit" element={<OrganizationUpdate />} />
      <Route path="delete" element={<OrganizationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrganizationRoutes;
