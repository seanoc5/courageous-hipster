import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentFragment from './content-fragment';
import ContentFragmentDetail from './content-fragment-detail';
import ContentFragmentUpdate from './content-fragment-update';
import ContentFragmentDeleteDialog from './content-fragment-delete-dialog';

const ContentFragmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContentFragment />} />
    <Route path="new" element={<ContentFragmentUpdate />} />
    <Route path=":id">
      <Route index element={<ContentFragmentDetail />} />
      <Route path="edit" element={<ContentFragmentUpdate />} />
      <Route path="delete" element={<ContentFragmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentFragmentRoutes;
